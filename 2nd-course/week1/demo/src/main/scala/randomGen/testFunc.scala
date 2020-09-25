package randomGen

object testFunc {
  trait Generator[+T] {
    self => // alias for this
    def generate: T
    def map[S](f:T => S): Generator[S] = new Generator[S] {
      // if 'generate', it will refer this.generate inside of map method, which we want to avoid
      // or we can use 'Generator.this.generate'
      def generate = f(self.generate)

    }
    def flatMap[S](f: T=> Generator[S]): Generator[S] = new Generator[S] {
      def generate = f(self.generate).generate
    }
  }
  def pairs[T, U](t: Generator[T], u: Generator[U]) =
    new Generator[(T,U)] {
      def generate = (t.generate, u.generate)
    }

  def lists: Generator[List[Int]] = for {
    isEmpty <- booleans
    list <- if(isEmpty) emptyLists else nonEmptyLists
  } yield list

  def emptyLists = single(Nil)

  def nonEmptyLists = for {
    head <- integers
    tail <- lists
  } yield head::tail

  val integers = new Generator[Int] {
    val rand = new java.util.Random
    def generate = rand.nextInt()
  }
  val booleans = integers.map( _ > 0)

  def single[T](x: T): Generator[T] = new Generator[T] {
    def generate = x
  }

  def test[T](g: Generator[T], numTimes: Int = 100)
             (test: T => Boolean): Unit = {
    for (i <- 0 until numTimes) {
      val value = g.generate
      assert(test(value), "test failed for " + value)
    }
    println("passed " + numTimes + " tests")
  }

  def main(args: Array[String]): Unit = {
    test(pairs(lists, lists)) {
      case (xs, ys) => (xs ++ ys).length > xs.length
    }

    /* QuickCheck, ScalaCheck
    * https://github.com/typelevel/scalacheck/blob/master/doc/UserGuide.md
    * Instead of writing tests, write properties that are assumed to hold
    * This idea is implemented in the ScalaCheck tool
    *
    * import org.scalacheck.Prop.forAll
    * forAll { (l1: List[Int], l2: List[Int]) =>
    *   l1.size + l2.size == (l1 ++ l2).size
    * }
    *
    * */


  }

}
