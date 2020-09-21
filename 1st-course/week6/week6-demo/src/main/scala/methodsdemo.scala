object methodsdemo {


  def scalarProduct(xs: Vector[Double], ys: Vector[Double]): Double =
    (xs zip ys).map(xy=>xy._1 * xy._2).sum

  def scalarProductAlt(xs: Vector[Double], ys: Vector[Double]): Double =
    (xs zip ys).map{case (x,y) => x * y}.sum
  // generally, {case (x,y) => x * y} is equivalent to
  // x => x match { case ... }

  def isPrime(n: Int): Boolean = ( 2 until n)forall(d => n%d!=0)

  def main(args: Array[String]): Unit = {
    val s = "Hello World"
    val xs = Array(1,2,3,4,5,6)

    println(s filter (c=>c.isUpper))

    println(s exists (c=>c.isUpper))

    println(s forall (c=>c.isUpper))

    val pairs = xs zip s
    println(pairs.foreach(pair => println(pair)))
    val (left, right) = pairs.unzip
    left.foreach(i=>println(i))
    right.foreach(c=>println(c))

    println(s flatMap (c=>List('.',c)))




  }
}
