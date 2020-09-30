package stream

object squareRoots {

  def sqrtStream(x:Double): LazyList[Double] = {
    def improve(guess: Double) = (guess+x/guess) / 2
    lazy val guesses: LazyList[Double] = 1 #:: (guesses map improve)
    guesses
  }

  def isGoodEnough(guess: Double, x: Double) =
    math.abs((guess * guess - x)/x) < 0.0001

  def main(args: Array[String]): Unit = {
    println(sqrtStream(4).filter(isGoodEnough(_,4)).take(10).toList)
    // List(2.0000000929222947, 2.000000000000002, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0)
  }
}


/* Consider two ways to express the infinite stream of multiples of a given number N
val xs = from(1) map (_ * N)
val ys = from(1) filter (_ % N == 0)

Which one generates its result faster?
map
- map: immediately multiply
1  2  3  4  5 ...
3  6  9 12 15 ...
- filter: choose after generating
1  2  3  4  5 ...
      3
 */