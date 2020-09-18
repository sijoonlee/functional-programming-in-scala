package decomposition

// Classification and access method
object demo1 {

  trait Expr {
    def isNumber :Boolean
    def isSum :Boolean
    def numValue :Int
    def leftOp :Expr
    def rightOp :Expr
  }

  class Number(n: Int) extends Expr {
    def isNumber :Boolean = true
    def isSum :Boolean = false
    def numValue :Int = n
    def leftOp :Expr = throw new Error("Number.leftOp")
    def rightOp :Expr = throw new Error("Number.rightOp")
  }

  class Sum(e1 :Expr, e2 :Expr) extends Expr {
    def isNumber :Boolean = false
    def isSum :Boolean = true
    def numValue :Int = throw new Error("Sum.numValue")
    def leftOp :Expr = e1
    def rightOp :Expr = e2
  }

  def eval(e:Expr) :Int = {
    if (e.isNumber) e.numValue
    else if (e.isSum) eval(e.leftOp) + eval(e.rightOp)
    else throw new Error("Unknown expression " + e)
  }

  def main(args: Array[String]): Unit = {
    print( eval(new Sum(new Number(1), new Number(2))) )
  }

  /*
  this approach has an issue
  you have to make members a lot
  They would increase qudratically as new sub-class is added
   */


}
