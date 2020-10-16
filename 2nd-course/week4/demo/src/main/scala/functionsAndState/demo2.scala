package functionsAndState

object demo2 {

  /*
  def cons[T](hd:T, tl: => LazyList[T]) = new LazyList[T]{

    override def head = hd
    private var tlOpt: Option[LazyList[T]] = None
    override def tail:LazyList[T] = tlOpt match {
      case Some(x) => x
      case None => tlOpt = Some(tl); tail
    }
  }
  is this stateful?
  --> both Yes/No possible
   */

  /*
  class BankAccountProxy(ba: BankAccount) {
    def deposit(amount: Int): Unit = ba.deposit(amount)
    def withdraw(amount: Int): Int = ba.withdraw(amount)
  }

  instance of BankAccountProxy is stateful?
  --> Yes
  the result is depend on history
   */

}
