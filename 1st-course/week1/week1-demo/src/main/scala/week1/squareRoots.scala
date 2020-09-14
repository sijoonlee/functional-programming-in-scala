package week1

object squareRoots {
  def abs(x:Double) = if (x<0) -x else x

  def sqrt(x:Double) = { // Limit the Scope of helper functions by using Block {}
    def isGoodEnough(guess: Double):Boolean =
      abs(guess * guess - x) < 0.001

    def isGoodEnoughNewVersion(guess: Double):Boolean =
      abs(guess * guess - x)/x < 0.0001

    def improve(guess:Double):Double =
      (guess + x / guess) / 2

    def sqrtIter(guess:Double):Double =
      if (isGoodEnoughNewVersion(guess)) guess
      else sqrtIter(improve(guess))

    sqrtIter(1.0) // return value comes last
  }

  def main(args:Array[String]):Unit = {
    println(sqrt(2))
    println(sqrt(4))
    println(sqrt(0.01))
    println(sqrt(0.1e-20))
    println(sqrt(0.1e20))
  }
}
/* 1. The isGoodEnough test is not precise for small numbers
*     It can lead to non-termination for very large numbers - Explain why
*     - The threshold would be too big for the guess
*     - The squared large number is even more large, it may not be able to reach the threshold
*
*  2. Design a different version of isGoodEnough that does not have these problem
*     - check if the difference is relatively small
*     - condition: diff / trueValue < threshold
*
*  3. Test with below numbers
*     0.001
*     0.1e-20
*     1.0e20
*     1.0e50
* */
