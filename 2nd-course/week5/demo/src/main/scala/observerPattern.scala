object observerPattern {

  trait Subscriber {
    def handler(pub:Publisher)
  }

  trait Publisher {
    private var subscribers: Set[Subscriber] = Set()

    def subscribe(subscriber:Subscriber): Unit =
      subscribers += subscriber

    def unsubscribe(subscriber:Subscriber): Unit =
      subscribers -= subscriber

    def publish():Unit =
      subscribers.foreach(_.handler(this))
  }

  class BankAccount extends Publisher {
    private var balance = 0

    def currentBalance: Int = balance

    def deposit(amount:Int): Unit =
      if(amount>0){
        balance += amount
        publish()
      }

    def withdraw(amount:Int):Unit =
      if(0<amount && amount <= balance) {
        balance -= amount
        publish()
      } else throw new Error("insufficient funds")

  }

  // a subscriber to maintain the total balance of a list of accounts
  class Consolidator(observed: List[BankAccount]) extends Subscriber {

    observed.foreach(_.subscribe(this))
    sum()

    private var total: Int = 0

    private def sum() =
      total = observed.map(_.currentBalance).sum

    def handler(pub: Publisher): Unit = sum()

    def totalBalance: Int = total

  }


  def main(args: Array[String]): Unit = {
    val a = new BankAccount
    val b = new BankAccount
    val c = new Consolidator(List(a,b))
    println(c.totalBalance)
    a.deposit(20)
    b deposit 30
    println(c.totalBalance)



  }


}
