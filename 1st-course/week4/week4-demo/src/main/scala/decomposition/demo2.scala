package decomposition

// Object-Oriented Decomposition
object demo2 {
  trait Expr {
    def eval: Int
    def show: String
  }
  class Number(n :Int) extends Expr {
    def eval :Int = n
    def show :String = "some"
  }
  class Sum(e1 :Expr, e2 :Expr) extends Expr {
    def eval :Int = e1.eval + e2.eval
    def show :String = "some"
  }
}
/*
this approach is good in decreasing the number of methods
but you have to define eval, show in each class

and there's big limitation
What if we want to simplify the expression
a * b + a * c -> a * ( b + c)
- this is a non-local simplification, it cannot occur in single object

 */
