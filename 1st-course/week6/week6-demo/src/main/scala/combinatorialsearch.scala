object combinatorialsearch {
  def isPrime(n: Int): Boolean = ( 2 until n)forall(d => n%d!=0)
  // generate the sequence of all pairs of integers (i,j)
  // such that 1 <= j < i < n
  // filter i + j is prime

  // (1 until n) map (i =>
  //    (1 until i) map (j => (i, j)))

  def pair(n:Int): Seq[Seq[(Int,Int)]] =
   (1 until n) map (i =>
      (1 until i) map (j => (i, j))) // note that i is exclusive

  // xs flatMap f  = (xs map f).flatten
  def flattened(n:Int): Seq[(Int, Int)] =
    (1 until n) flatMap (i =>
      (1 until i) map (j => (i, j))) // note that i is exclusive

  def usingFor(n: Int): Seq[(Int, Int)] =
    for {
      i <- 1 until n
      j <- 1 until i
      if isPrime(i+j)
    } yield(i, j)

  def scalaProduct(xs: List[Double], ys: List[Double]): Double =
    (for ( (x, y) <- xs zip ys) yield x * y).sum


  case class Person(name:String, age:Int)

  def main(args: Array[String]): Unit = {
    println(pair(5)) // Vector(Vector(), Vector((2,1)), Vector((3,1), (3,2)), Vector((4,1), (4,2), (4,3)))
    println(pair(5).foldRight(Seq[(Int,Int)]())(_++_)) // Vector((2,1), (3,1), (3,2), (4,1), (4,2), (4,3))
    println(pair(5).flatten)                           // Vector((2,1), (3,1), (3,2), (4,1), (4,2), (4,3))
    println(flattened(5)) // Vector((2,1), (3,1), (3,2), (4,1), (4,2), (4,3))
    println(flattened(5) filter (pair => isPrime(pair._1 + pair._2) ))


    // For expression

    // for ( s ) yield e

    // where s is a sequence of generators and filters
    // and e is an expression whose value is returned by an iteration

    // A generator is of the form p <- e, where p is a pattern and e an expression whose value is a collection

    // A filter is of the form if f where f is a boolean expression
    // The sequence must start with a generator

    // if there are several generators in the sequence,
    // the last generators vary before the first does

    // brace { s } can be used instead of ( s )
    // then the sequence of generators and filters can be written on multiple lines without requiring semicolons

    var people = List(Person("james", 30),Person("jone", 20))
    println( for (p<-people if p.age>20) yield p.name) // List(james)
    // this is equivalent to
    // people filter (p => p.age > 20) map (p => p.name)

    println(usingFor(5))



    /* Sets */

    var fruit = Set("apple", "banana")
    var s = (1 to 5).toSet

    println(s map (_+2)) // HashSet(5, 6, 7, 3, 4)
    println(s.nonEmpty) // true
    println(fruit filter (_.startsWith("ap")))

    /* Difference between Sets and Sequences
    *
    * Sets are unordered
    *
    * don't have duplicate elements
    *
    * The fundamental operation is 'contains'
    * s contains 5
    *
    * */






  }
}
