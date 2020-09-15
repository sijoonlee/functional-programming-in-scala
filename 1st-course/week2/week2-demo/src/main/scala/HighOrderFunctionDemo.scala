object HighOrderFunctionDemo {

  def sum(f:Int => Int, a: Int, b: Int): Int =
    if(a>b) 0
    else f(a) + sum(f, a+1, b)

  def sumInts(a:Int, b:Int) = sum(id, a, b)
  def sumCubes(a: Int, b: Int) = sum(cube, a, b)
  def sumCubesAnonymous(a: Int, b: Int) = sum( (x:Int) => x*x*x, a, b)
  def sumFactorials(a: Int, b: Int) = sum(fact, a, b)

  def id(x:Int): Int = x
  def cube(x: Int): Int = x * x * x
  def fact(x: Int): Int = if(x==0) 1 else x * fact(x-1)

  def sumTailRec(f:Int => Int, a:Int, b:Int) = {
    def loop(a:Int, acc:Int):Int = {
      if(a>b) acc
      else loop(a+1, f(a) + acc)
    }
    loop(a, 0)
  }

  def sumHighOrder(f:Int => Int): (Int,Int) => Int = {
    def sumF(a:Int, b:Int):Int = {
      if(a>b) 0
      else f(a) + sumF(a+1, b)
    }
    sumF
  }

  // --------------------------------------------
  def mapReduce(f:Int=>Int, combine:(Int, Int)=>Int, zero:Int) (a:Int,b:Int):Int =
    if(a>b) zero
    else combine(f(a), mapReduce(f, combine, zero)(a+1, b))

  def product(f:Int => Int)(a: Int, b:Int):Int = mapReduce(f, (x, y)=> x*y, 1)(a,b)

  def factorial(n: Int) = product( x=> x )(1, n)

  // --------------------------------------------
  // Finding fixed points

  def fixedPointDemo(f: Double => Double, firstGuess: Int) = {
    val tolerance = 0.0001
    def abs(d: Double): Double ={
      if (d < 0) -d
      else d
    }
    def isCloseEnough(x:Double, y:Double) =
      abs( (x-y) / x ) / x < tolerance
    def fixedPoint(f: Double => Double) = {
      def iterate(guess:Double): Double = {
        var next = f(guess)
        if(isCloseEnough(guess, next)) next
        else iterate(next)
      }
      iterate(firstGuess)
    }
    fixedPoint(f)
  }

  def main(args:Array[String]):Unit = {
    println(sumInts(1,5)) // 1 + 2 + 3 + 4 + 5
    println(sumCubes(1,5)) // 1^3 + 2^3 + 3^3 + 4^3 + 5^3
    println(sumFactorials(1,5)) // 1! + 2! + 3! + 4! + 5!
    println(sumTailRec( x=>x*x, 1, 5))
    println(sumHighOrder(x=>x*x)(1,5))
    println("--------------------------------")
    println(product(x=>x*x)(3, 4))
    println(factorial(5))
    println("--------------------------------")
    println(fixedPointDemo( x => 1 + x*1/2, 1))
    // this is to find square root of 2 ( x = 2^(1/2), x * x = 2, x = 2/x )
    // println(fixedPointDemo( x => 2/x, 1)) // infinite loop since guess is oscillating between 1 and 2
    println(fixedPointDemo( x => (2/x + x)/2, 1)) // average damping, (x + f(x))/2
  }
}
