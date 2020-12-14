// https://docs.scala-lang.org/tour/traits.html

object SplittersCombiners extends App{
  /* Simple Iterator trait */
  trait Iterator[A] {
    def next(): A
    def hasNext: Boolean
  }
  // def iterator: Iterator[A]
  // foldLeft using iterator
  class example extends Iterator[Int]{
    override def hasNext: Boolean = ???
    override def next(): Int = ???

    def foldLeft(z:Int)(f:(Int, Int)=>Int):Int = {
      var s = z
      while(hasNext) s = f(s, next())
      s
    }
  }


  /* Simple Splitter trait */
  trait Splitter[A] extends Iterator[A] {
    def split: Seq[Splitter[A]]
    def remaining: Int // how many left
  }
  // after calling split, the original splitter is left in an undefined state
  // the resulting splitters traverse disjoint subsets of the original splitter
  // remaining is an estimate on the number of remaining elements
  // split is an efficient method - O(log n) or better

  class example2 extends Splitter[Int] {
    override def split: Seq[Splitter[Int]] = ???
    override def remaining: Int = ???
    override def next(): Int = ???
    override def hasNext: Boolean = ???
    val threshold = 10
    def foldLeft(z:Int)(f:(Int, Int)=>Int):Int = {
      var s = z
      while(hasNext) s = f(s, next())
      s
    }
    def fold(z:Int)(f:(Int, Int)=> Int):Int= {
      if(remaining<threshold) foldLeft(z)(f)
      else {
        val children = for(child<-split) yield task {child.fold(z)(f)}
        children.map(_.join()).foldLeft(z)(f)
      }
    }

    /* Builder */
    trait Builder[A, Repr] {
      def += (elem:A): Builder[A, Repr]
      def result:Repr
    }
    // calling result returns a collection of type Repr
    // calling result leaves the Builder in an undefined state

    class example3 extends Builder[Int,List[Int]] {
      def foreach(value: Any) = ???

      override def +=(elem: Int): Builder[Int, List[Int]] = ???
      override def result: List[Int] = ???
      def newBuilder: Builder[Int, List[Int]] = ???
      def filter(p: Int=> Boolean): List[Int] = {
        val b = newBuilder
        for(x<-this) if(p(x)) b+=x
        b.result
      }
    }


    /* Combiner */
    trait Combiner[A, Repr] extends Builder[A, Repr] {
      def combine(that: Combiner[A, Repr]): Combiner[A, Repr]
    }
    // calling combine returns a new combiner that contains elements of input combiners
    // calling combine leaves both original Combiners in an undefined state
    // combine is an efficient method - O(log n) or better

    class example4 extends Combiner[Int, List[Int]] {
      override def combine(that: Combiner[Int, List[Int]]): Combiner[Int, List[Int]] = ???
      override def +=(elem: Int): Builder[Int, List[Int]] = ???
      override def result: List[Int] = ???

      def newCombiner(): Combiner[Int, List[Int]] = ???



    }



  }

}
