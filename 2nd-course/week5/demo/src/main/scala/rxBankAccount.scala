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

object rxBankAccount {

  class StackableVariable[T](init: T) {
    private var values: List[T] = List(init)
    def value: T = values.head
    def withValue[R](newValue: T)(op: => R):R = {
      values = newValue :: values
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
      myExpr = () => expr
      computeValue()
    }
    protected def computeValue(): Unit = {
      val newValue = caller.withValue(this)(myExpr())
      if(myValue != newValue){
        myValue = newValue
        val obs = observers
        observers = Set()
        obs.foreach(_.computeValue())
      }
    }
    def apply():T = {
      observers += caller.value
      println("Class Signal()")
      println("obs", observers)
      println("caller", caller)
      assert(!caller.value.observers.contains(this), "cyclic signal definition")
      myValue
    }
  }

  object Signal {
    val caller = new StackableVariable[Signal[_]](NoSignal)
    def apply[T](expr: => T) = new Signal(expr)
  }

  object NoSignal extends Signal[Nothing](???) { // (???) means no implementation
    // computeValue needs to be disabled for NoSignal
    // we can't evaluate an expression of type Nothing
    override def computeValue(): Unit = ()
  }

  class Var[T](expr: => T) extends Signal[T](expr) {
    override def update(expr: => T): Unit = super.update(expr)
  }

  object Var {
    def apply[T](expr: => T) = new Var(expr)
  }


  class BankAccount {
    val balance = Var(0)
    def deposit(amount: Int): Unit =
      if(amount > 0) {
        // avoid cyclic definition
        // [balance() = balance() + amount] will create cyclic definition
        val b = balance()
        balance() = b + amount
      }
    def withdraw(amount: Int): Unit =
      if(0<amount && amount <= balance()){
        val b = balance()
        balance() = b - amount
      } else throw new Error("insufficient funds")
  }

  def consolidate(accts: List[BankAccount]): Signal[Int] = {
    println(accts.map(_.balance()).sum)
    Signal(accts.map(_.balance()).sum)
  }

  def main(args: Array[String]): Unit = {
    val a = new BankAccount()
    val b = new BankAccount()
    println("consolidate")
    val c = consolidate(List(a,b))
    println(c())
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
