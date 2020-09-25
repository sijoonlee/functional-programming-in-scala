package randomGen

object elegantRandomGen {

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

  val integers = new Generator[Int] {
    val rand = new java.util.Random
    def generate = rand.nextInt()
  }

  def single[T](x: T): Generator[T] = new Generator[T] {
    def generate = x
  }

  def choose(lo:Int, hi:Int): Generator[Int] =
    for (x <- integers) yield lo + x % (hi-lo)


  // ex) oneOf(x,y,z)
  def oneOf[T](xs: T*): Generator[T] =
    for (idx <- choose(0, xs.length)) yield xs(idx)

  def lists: Generator[List[Int]] = for {
    isEmpty <- booleans
    list <- if(isEmpty) emptyLists else nonEmptyLists
  } yield list

  def emptyLists = single(Nil)

  def nonEmptyLists = for {
    head <- integers
    tail <- lists
  } yield head::tail

  /*
  val booleans = for (x <- integers) yield x > 0
  val booleans = integers map { x => x > 0 }
  */

  val booleans = new Generator[Boolean] {
    def generate = integers.generate > 0
  }
  /*
  def pairs[T, U](t: Generator[T], u: Generator[U]) = t flatMap{
    x => u map { y=> (x, y)}
  }

  def pairs[T, U](t: Generator[T], u: Generator[U]) = t flatMap{
    x => new Generator[(T,U)] {
      def generate = (x, u.generate)
    }
  }

  def pairs[T, U](t: Generator[T], u: Generator[U]) =
    new Generator[(T, U)] {
      def generate = (new Generator[(T,U)] {
        def generate = (t.generate, u.generate)
      }).generate
  }
  */
  def pairs[T, U](t: Generator[T], u: Generator[U]) =
    new Generator[(T,U)] {
      def generate = (t.generate, u.generate)
    }

}
