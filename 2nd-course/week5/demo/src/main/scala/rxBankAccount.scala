/* Implementation Idea
*
* Each signal maintains
* - its current value
* - the current expression that defines the signal value
* - a set of observers: the other signals that depend on its value
* If the signal changes, all observers need to be re-evaluated
*
* How do we record dependencies in observers?
* - When evaluating a signal-valued expression, need to know
*   which signal caller gets defined or updated by the expression
* - If we know that, then executing a sig() means adding caller to the observers of sig
* - When signal sig's value changes, all previously observing signals are
*   re-evaluated and the set sig.observers is cleared
* - Re-evaluation will re-enter a calling signal caller in sig.observers
*   as long as caller's value still depends on sig.
*
* How do we find out on whose behalf a signal expression is evaluated?
* - By maintaining a global data structure referring to the current caller
* - The data structure is accessed in a stack-like fashion
*   because one evaluation of a signal might trigger others
* */


/**
 * https://stackoverflow.com/questions/32772084/what-does-mean-in-scala
 *
 * () can meean a few things, depending on context.
 *
 * As a value, it is an empty tuple, or the singleton type. It's type is Unit.
 *
 * It can denote a function or method that takes no parameters, such as:
 *
 * def foo() = "Hello world"
 * Note that when writing an anonymous function, the () is by itself but still means a function with no parameters.
 *
 * val x = () => 2
 * The type of x is () => Int, a function taking no parameters and returning an int.
 *
 * As a source of infinite confusion, you can get examples like this:
 *
 * val y = () => ()
 * The type of y here is () => Unit, a function of no parameters returning Unit, not Unit => Unit, which would be writen as val z = (x:Unit) => (), and called like z(())
 *
 * The unit vs empty parameter distinction has been awkward for me in the past so hopefully that demystifies some of it.
 */
object rxBankAccount {

  class StackableVariable[T](init: T) {
    private var values: List[T] = List(init)
    def value: T = values.head // value is Singal here
    def withValue[R](newValue: T)(op: => R):R = {
      println("StackableVariable: withValue", this)
      values = newValue :: values
      println("StackableVariable: try op here")
      println("StackableVariable: op", op)
      println("StackableVariable: value popped", values.head)
      try op finally values = values.tail
    }
  }
  /* How to access it
  * val caller = new StackableVariable(initialSig)
  * caller.withValue(otherSig) { ... }
  * ... caller.value ...
  * */

  class Signal[T](expr: => T){
    import Signal._
    private var myExpr: () => T = _
    private var myValue: T = _
    private var observers: Set[Signal[_]] = Set()
    update(expr)
    protected def update(expr: => T): Unit = {
      println("Signal: update", this)
      myExpr = () => expr
      computeValue()
    }
    protected def computeValue(): Unit = {
      println("Signal: computeValue", this)
      val newValue = caller.withValue(this)(myExpr()) // pass Signal itself
      if(myValue != newValue){
        println("Signal: computeValue - myValue != newValue", myValue, newValue)
        println("Signal: computeValue - obs", observers)
        myValue = newValue
        val obs = observers
        observers = Set()
        println("Signal: computeValue - obs foreach computeValue")
        obs.foreach(_.computeValue())
      }
    }
    def apply():T = {
      println("Signal: apply", this)
      observers += caller.value
      println("Signal: apply - caller added", caller.value)
      println("Signal: apply - observers", observers)
      assert(!caller.value.observers.contains(this), "cyclic signal definition")
      // cyclic definition example
      //    s() = s() + 1
      // you need it like below
      //    val a = s()
      //    s() = a + 1
      myValue
    }
  }
  object Signal {
    val caller = new StackableVariable[Signal[_]](NoSignal)
    def apply[T](expr: => T) = {
      println("object Signal", this)
      new Signal(expr)
    }
  }

  object NoSignal extends Signal[Nothing](???) { // (???) means no implementation
    // computeValue needs to be disabled for NoSignal
    // we can't evaluate an expression of type Nothing
    override def computeValue(): Unit = ()
  }

  class Var[T](expr: => T) extends Signal[T](expr) {
    override def update(expr: => T): Unit = {
      println("Var: update", this)
      super.update(expr)
    }
    /* if this override method is deleted
    Error:(99, 9) method update in class Signal cannot be accessed
    as a member of rxBankAccount.
    Var[Int] from class BankAccount in object rxBankAccount Access to protected method update not permitted
    because enclosing class BankAccount in object rxBankAccount is not a subclass of
    class Signal in object rxBankAccount where target is defined balance() = b + amount
     */
  }

  object Var {
    def apply[T](expr: => T) = {
      println("object Var", this)
      new Var(expr)
    }
  }


  class BankAccount {
    val balance = Var(0)
    def deposit(amount: Int): Unit = {
      if(amount > 0) {
        // avoid cyclic definition
        // [balance() = balance() + amount] will create cyclic definition
        println("BankAccount: balance", balance)
        println("BankAccount: val b = balance()")
        val b = balance()
        println("BankAccount: balance() =  b + amount")
        balance() = b + amount
      }
    }

    def withdraw(amount: Int): Unit =
      if(0<amount && amount <= balance()){
        val b = balance()
        balance() = b - amount
      } else throw new Error("insufficient funds")
  }

  def consolidate(accts: List[BankAccount]): Signal[Int] = {
    Signal(accts.map(_.balance()).sum)
  }

  def main(args: Array[String]): Unit = {
    println("val a = new BankAccount()")
    val a = new BankAccount()
     println("val b = new BankAccount()")
    val b = new BankAccount()
    println("consolidate")
    val c = consolidate(List(a,b))
    println(c()) // 0
    println("a deposit 20")
    a deposit 20
    println("b deposit 30")
    b deposit 30
    println("b deposit 20")
    b deposit 20
    println(c())

//    val rate = Signal(246.0)
//    val inDollar = Signal(c() * rate())
//    println(inDollar())
//
//    a deposit 10
//    println(c())
//    println(inDollar())
  }

}

/**
 * val a = new BankAccount()
 * (object Var,rxBankAccount$Var$@75412c2f)
 * (Var: update,rxBankAccount$Var@66048bfd)
 * (Signal: update,rxBankAccount$Var@66048bfd)
 * (Signal: computeValue,rxBankAccount$Var@66048bfd)
 * (Signal: update,rxBankAccount$NoSignal$@61a52fbd)
 * (StackableVariable: withValue,rxBankAccount$StackableVariable@35bbe5e8)
 * StackableVariable: try op here
 * (StackableVariable: op,0)
 * (StackableVariable: value popped,rxBankAccount$Var@66048bfd)
 * (Signal: computeValue - myValue != newValue,null,0)
 * (Signal: computeValue - obs,Set())
 * Signal: computeValue - obs foreach computeValue
 *
 * val b = new BankAccount()
 * (object Var,rxBankAccount$Var$@75412c2f)
 * (Var: update,rxBankAccount$Var@1b0375b3)
 * (Signal: update,rxBankAccount$Var@1b0375b3)
 * (Signal: computeValue,rxBankAccount$Var@1b0375b3)
 * (StackableVariable: withValue,rxBankAccount$StackableVariable@35bbe5e8)
 * StackableVariable: try op here
 * (StackableVariable: op,0)
 * (StackableVariable: value popped,rxBankAccount$Var@1b0375b3)
 * (Signal: computeValue - myValue != newValue,null,0)
 * (Signal: computeValue - obs,Set())
 * Signal: computeValue - obs foreach computeValue
 *
 * consolidate
 * (object Signal,rxBankAccount$Signal$@2752f6e2)
 * (Signal: update,rxBankAccount$Signal@e580929)
 * (Signal: computeValue,rxBankAccount$Signal@e580929)
 * (StackableVariable: withValue,rxBankAccount$StackableVariable@35bbe5e8)
 * StackableVariable: try op here
 * (Signal: apply,rxBankAccount$Var@66048bfd)
 * (Signal: apply - caller added,rxBankAccount$Signal@e580929)
 * (Signal: apply - observers,Set(rxBankAccount$Signal@e580929))
 * (Signal: apply,rxBankAccount$Var@1b0375b3)
 * (Signal: apply - caller added,rxBankAccount$Signal@e580929)
 * (Signal: apply - observers,Set(rxBankAccount$Signal@e580929))
 * (StackableVariable: op,0)
 * (StackableVariable: value popped,rxBankAccount$Signal@e580929)
 * (Signal: apply,rxBankAccount$Var@66048bfd)
 * (Signal: apply - caller added,rxBankAccount$Signal@e580929)
 * (Signal: apply - observers,Set(rxBankAccount$Signal@e580929))
 * (Signal: apply,rxBankAccount$Var@1b0375b3)
 * (Signal: apply - caller added,rxBankAccount$Signal@e580929)
 * (Signal: apply - observers,Set(rxBankAccount$Signal@e580929))
 * (Signal: computeValue - myValue != newValue,null,0)
 * (Signal: computeValue - obs,Set())
 * Signal: computeValue - obs foreach computeValue
 * (Signal: apply,rxBankAccount$Signal@e580929)
 * (Signal: apply - caller added,rxBankAccount$NoSignal$@61a52fbd)
 * (Signal: apply - observers,Set(rxBankAccount$NoSignal$@61a52fbd))
 * 0
 *
 * a deposit 20
 * (BankAccount: balance,rxBankAccount$Var@66048bfd)
 * BankAccount: val b = balance()
 * (Signal: apply,rxBankAccount$Var@66048bfd)
 * (Signal: apply - caller added,rxBankAccount$NoSignal$@61a52fbd)
 * (Signal: apply - observers,Set(rxBankAccount$Signal@e580929, rxBankAccount$NoSignal$@61a52fbd))
 * BankAccount: balance() =  b + amount
 * (Var: update,rxBankAccount$Var@66048bfd)
 * (Signal: update,rxBankAccount$Var@66048bfd)
 * (Signal: computeValue,rxBankAccount$Var@66048bfd)
 * (StackableVariable: withValue,rxBankAccount$StackableVariable@35bbe5e8)
 * StackableVariable: try op here
 * (StackableVariable: op,20)
 * (StackableVariable: value popped,rxBankAccount$Var@66048bfd)
 * (Signal: computeValue - myValue != newValue,0,20)
 * (Signal: computeValue - obs,Set(rxBankAccount$Signal@e580929, rxBankAccount$NoSignal$@61a52fbd))
 * Signal: computeValue - obs foreach computeValue
 * (Signal: computeValue,rxBankAccount$Signal@e580929)
 * (StackableVariable: withValue,rxBankAccount$StackableVariable@35bbe5e8)
 * StackableVariable: try op here
 * (Signal: apply,rxBankAccount$Var@66048bfd)
 * (Signal: apply - caller added,rxBankAccount$Signal@e580929)
 * (Signal: apply - observers,Set(rxBankAccount$Signal@e580929))
 * (Signal: apply,rxBankAccount$Var@1b0375b3)
 * (Signal: apply - caller added,rxBankAccount$Signal@e580929)
 * (Signal: apply - observers,Set(rxBankAccount$Signal@e580929))
 * (StackableVariable: op,20)
 * (StackableVariable: value popped,rxBankAccount$Signal@e580929)
 * (Signal: apply,rxBankAccount$Var@66048bfd)
 * (Signal: apply - caller added,rxBankAccount$Signal@e580929)
 * (Signal: apply - observers,Set(rxBankAccount$Signal@e580929))
 * (Signal: apply,rxBankAccount$Var@1b0375b3)
 * (Signal: apply - caller added,rxBankAccount$Signal@e580929)
 * (Signal: apply - observers,Set(rxBankAccount$Signal@e580929))
 * (Signal: computeValue - myValue != newValue,0,20)
 * (Signal: computeValue - obs,Set(rxBankAccount$NoSignal$@61a52fbd))
 * Signal: computeValue - obs foreach computeValue
 * b deposit 30
 * (BankAccount: balance,rxBankAccount$Var@1b0375b3)
 * BankAccount: val b = balance()
 * (Signal: apply,rxBankAccount$Var@1b0375b3)
 * (Signal: apply - caller added,rxBankAccount$NoSignal$@61a52fbd)
 * (Signal: apply - observers,Set(rxBankAccount$Signal@e580929, rxBankAccount$NoSignal$@61a52fbd))
 * BankAccount: balance() =  b + amount
 * (Var: update,rxBankAccount$Var@1b0375b3)
 * (Signal: update,rxBankAccount$Var@1b0375b3)
 * (Signal: computeValue,rxBankAccount$Var@1b0375b3)
 * (StackableVariable: withValue,rxBankAccount$StackableVariable@35bbe5e8)
 * StackableVariable: try op here
 * (StackableVariable: op,30)
 * (StackableVariable: value popped,rxBankAccount$Var@1b0375b3)
 * (Signal: computeValue - myValue != newValue,0,30)
 * (Signal: computeValue - obs,Set(rxBankAccount$Signal@e580929, rxBankAccount$NoSignal$@61a52fbd))
 * Signal: computeValue - obs foreach computeValue
 * (Signal: computeValue,rxBankAccount$Signal@e580929)
 * (StackableVariable: withValue,rxBankAccount$StackableVariable@35bbe5e8)
 * StackableVariable: try op here
 * (Signal: apply,rxBankAccount$Var@66048bfd)
 * (Signal: apply - caller added,rxBankAccount$Signal@e580929)
 * (Signal: apply - observers,Set(rxBankAccount$Signal@e580929))
 * (Signal: apply,rxBankAccount$Var@1b0375b3)
 * (Signal: apply - caller added,rxBankAccount$Signal@e580929)
 * (Signal: apply - observers,Set(rxBankAccount$Signal@e580929))
 * (StackableVariable: op,50)
 * (StackableVariable: value popped,rxBankAccount$Signal@e580929)
 * (Signal: apply,rxBankAccount$Var@66048bfd)
 * (Signal: apply - caller added,rxBankAccount$Signal@e580929)
 * (Signal: apply - observers,Set(rxBankAccount$Signal@e580929))
 * (Signal: apply,rxBankAccount$Var@1b0375b3)
 * (Signal: apply - caller added,rxBankAccount$Signal@e580929)
 * (Signal: apply - observers,Set(rxBankAccount$Signal@e580929))
 * (Signal: computeValue - myValue != newValue,20,50)
 * (Signal: computeValue - obs,Set())
 * Signal: computeValue - obs foreach computeValue
 * b deposit 20
 * (BankAccount: balance,rxBankAccount$Var@1b0375b3)
 * BankAccount: val b = balance()
 * (Signal: apply,rxBankAccount$Var@1b0375b3)
 * (Signal: apply - caller added,rxBankAccount$NoSignal$@61a52fbd)
 * (Signal: apply - observers,Set(rxBankAccount$Signal@e580929, rxBankAccount$NoSignal$@61a52fbd))
 * BankAccount: balance() =  b + amount
 * (Var: update,rxBankAccount$Var@1b0375b3)
 * (Signal: update,rxBankAccount$Var@1b0375b3)
 * (Signal: computeValue,rxBankAccount$Var@1b0375b3)
 * (StackableVariable: withValue,rxBankAccount$StackableVariable@35bbe5e8)
 * StackableVariable: try op here
 * (StackableVariable: op,50)
 * (StackableVariable: value popped,rxBankAccount$Var@1b0375b3)
 * (Signal: computeValue - myValue != newValue,30,50)
 * (Signal: computeValue - obs,Set(rxBankAccount$Signal@e580929, rxBankAccount$NoSignal$@61a52fbd))
 * Signal: computeValue - obs foreach computeValue
 * (Signal: computeValue,rxBankAccount$Signal@e580929)
 * (StackableVariable: withValue,rxBankAccount$StackableVariable@35bbe5e8)
 * StackableVariable: try op here
 * (Signal: apply,rxBankAccount$Var@66048bfd)
 * (Signal: apply - caller added,rxBankAccount$Signal@e580929)
 * (Signal: apply - observers,Set(rxBankAccount$Signal@e580929))
 * (Signal: apply,rxBankAccount$Var@1b0375b3)
 * (Signal: apply - caller added,rxBankAccount$Signal@e580929)
 * (Signal: apply - observers,Set(rxBankAccount$Signal@e580929))
 * (StackableVariable: op,70)
 * (StackableVariable: value popped,rxBankAccount$Signal@e580929)
 * (Signal: apply,rxBankAccount$Var@66048bfd)
 * (Signal: apply - caller added,rxBankAccount$Signal@e580929)
 * (Signal: apply - observers,Set(rxBankAccount$Signal@e580929))
 * (Signal: apply,rxBankAccount$Var@1b0375b3)
 * (Signal: apply - caller added,rxBankAccount$Signal@e580929)
 * (Signal: apply - observers,Set(rxBankAccount$Signal@e580929))
 * (Signal: computeValue - myValue != newValue,50,70)
 * (Signal: computeValue - obs,Set())
 * Signal: computeValue - obs foreach computeValue
 * (Signal: apply,rxBankAccount$Signal@e580929)
 * (Signal: apply - caller added,rxBankAccount$NoSignal$@61a52fbd)
 * (Signal: apply - observers,Set(rxBankAccount$NoSignal$@61a52fbd))
 * 70
 */





/*
* val num = Var(1)
* val twice = Signal(num()*2)
* num() = 2 // the existing signal is updated
* --> twice is 4
*
*
* var num = Var(1)
* val twice = Signal(num()*2)
* num = Var(2) // this will assign new Signal to num
* --> twice is 2
*
* */
