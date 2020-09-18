package decomposition

object demo3 {

  trait Expr {
    def eval :Int = this match {
      case Var(e) => throw new UnsupportedOperationException("Eval Var: " + e)
      case Number(n) => n
      case Sum(e1, e2) => e1.eval + e2.eval
      case Product(e1, e2) => e1.eval * e2.eval
    }
    def show :String = this match {
      case Var(e) => e
      case Number(n) => n.toString
      case Sum(e1, e2) => e1.show + "+" + e2.show
      case Product(e1, e2) => matchingSumInProd(e1) + "*" + matchingSumInProd(e2)
    }
  }
  def matchingSumInProd(e: Expr):String = e match { case Sum(l, r) => "(" + e.show + ")" case _ => e.show }
  case class Var(e :String) extends Expr {}
  case class Number(n :Int) extends Expr {}
  case class Sum(e1 :Expr, e2 :Expr) extends Expr {}
  case class Product(e1: Expr, e2 :Expr) extends Expr {}

  def main(args: Array[String]): Unit = {
    println(Sum(Product(Number(2), Var("x")), Var("y")).show)
    println(Product(Sum(Number(2), Var("x")), Var("y")).show)
  }
}
