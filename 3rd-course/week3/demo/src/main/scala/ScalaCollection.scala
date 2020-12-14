import scala.collection.parallel.CollectionConverters._
import scala.collection.parallel.ParSet
import java.util.concurrent._

object ScalaCollection extends App {
  // Scala Collections Hierarchy

  // Traversable[T] - foreach method --> (Since version 2.13.0) Use Iterable instead of Traversable
  // Iterable[T] - iterator
  // Seq[T] - an ordered sequence of elements with type T
  // Set[T] - a set of elements with type T(no duplicates)
  // Map[K,V] - a map of keys with type K with values of type V (no duplicate keys)

  // Parallel Collection Hierarchy
  // ParIterable
  // ParSeq
  // ParSet
  // ParMap

  // GenIterable <- Iterable, ParIterable
  // GenSeq <- Seq, ParSeq
  // GenSet <- Set, ParSet
  // GenMap <- Map, ParMap
  // deprecated since 2.13


  // changes since 2.13
  // Gen* collection types have been removed
  // Parallel collections are now a separate module (https://github.com/scala/scala-parallel-collections)
  // Traversable and TraversableOnce are now only deprecated aliases for Iterable and IterableOnce.
  // Symbol aggregate is deprecated.(for non-parallel Seq)
  // `aggregate` is not relevant for sequential collections. Use `foldLeft(z)(seqop)` instead.
  // https://docs.scala-lang.org/overviews/core/collections-migration-213.html
  // https://www.scala-lang.org/api/2.13.0/scala/collection/index.html
  // https://dzone.com/articles/scala-213-has-scala-done-it-again
  // https://github.com/scala/scala-parallel-collections

  //  To depend on scala-parallel-collections in sbt, add this to your build.sbt:
  //    libraryDependencies +=
  //      "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.0-RC1"

  //  In your code, adding this import:
  //  import scala.collection.parallel.CollectionConverters._
  //  will enable use of the .par method as in earlier Scala versions.


  def largestPalindrome(xs:Seq[Int]):Int = {
    // def aggregate[S](z: => S)(seqop: (S, T) => S, combop: (S, S) => S): S
    xs.par.aggregate(Int.MinValue)(
      // seqop
      (largest, n) =>
        if(n>largest && n.toString == n.toString.reverse) n
        else largest,
      // combop
      math.max
    )
  }
  val array = (0 until 10000).toArray
  println(largestPalindrome(array))


  // Parallelizable Collections
  //  ParArray
  //  ParVector
  //  mutable.ParHashMap
  //  mutable.ParHashSet
  //  immutable.ParHashMap
  //  immutable.ParHashSet
  //  ParRange
  //  ParTrieMap (collection.concurrent.TrieMaps are new in 2.10)

  def intersection(a: Set[Int], b: Set[Int]): scala.collection.mutable.Set[Int] = {
    val result = scala.collection.mutable.Set[Int]()
    for(x<-a) if(b contains x) result.add(x)
    result
  }

  // this is bad approach
  // it modifies the same memory location in parallel
  // it will bring unexpected result
  def parIntersectionBad(a: ParSet[Int], b: ParSet[Int]): scala.collection.mutable.Set[Int] = {
    val result = scala.collection.mutable.Set[Int]()
    for(x<-a) if(b contains x) result.add(x)
    result
  }

  def parIntersectionGood(a: ParSet[Int], b: ParSet[Int]): ConcurrentSkipListSet[Int] = {
    val result = new ConcurrentSkipListSet[Int]()
    for(x<-a) if (b contains x) result.add(x)
    result
  }

  // by using combinator, we can avoid side effect
  def parIntersectionGoodAnother(a: ParSet[Int], b: ParSet[Int]): ParSet[Int] = {
    if(a.size < b.size) a.filter(b(_))
    else b.filter(a(_))
  }

  println(intersection((0 until 1000).toSet, (0 until 1000 by 4).toSet))
  println(parIntersectionBad((0 until 1000).par.toSet, (0 until 1000 by 4).par.toSet))


  // RULE
  // Never write to a collection that is concurrently traversed
  // Never read from a collection that is concurrently modified

  // TrieMap collection is an exception to the rules
  // The snapshot method can be used to grab the current state
  val graph = scala.collection.concurrent.TrieMap[Int, Int]() ++= (0 until 10000).map(i=>(i, i+1))
  graph(graph.size -1) = 0
  val previous = graph.snapshot()
  for((k,v) <- graph.par) graph(k) = previous(v)
  val violation = graph.find({case(i,v) => v != (i+2)%graph.size})
  println(s"violation: $violation")

}
