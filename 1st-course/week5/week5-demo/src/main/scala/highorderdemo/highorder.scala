package highorderdemo

object highorder {
  /*
  def map[U](f:T=>U):List[U] = this match {
    case Nil => this
    case x :: xs => f(x) :: xs.map(f)
  }

  def scaleList(xs: List[Double], factor: Double): List(Double) = xs match {
    case Nil => xs
    case y :: ys => y * factor :: scaleList(ys, factor)
  }

  def scaleList(xs: List[Double], factor: Double): List(Double) =
    xs map ( x => x*factor )
  */

  // write a function 'pack' that packs consecutive duplicates of an element into sublists
  // e.g) List('a','a','a','b','b') ===> List(List('a','a','a'), List('b','b'))

  def pack[T](l:List[T]):List[List[T]] = l match {
    case Nil => Nil
    case x :: xs =>
      val (first, rest) = l span (y => y == x)
      println(first, rest)
      first :: pack(rest)
  }

  /* cf) span method
    @inline final override def span(p: A => Boolean): (List[A], List[A]) = {
    val b = new ListBuffer[A]
    var these = this
    while (!these.isEmpty && p(these.head)) {
      b += these.head
      these = these.tail
    }
    (b.toList, these)
  }
   */


  // run-length
  // e.g. List('a', 'a', 'a', 'b', 'c', 'c', 'a')
  // encode(List('a', 'a', 'a', 'b', 'c', 'c', 'a')) ==> List( ('a',3), ('b',1), ('c', 2), ('a',1) )
  def encode[T](xs: List[T]): List[(T, Int)] =
    pack(xs) map (ys => (ys.head, ys.length))

  def main(args: Array[String]): Unit = {
    print(pack(List(1,1,1,2,2,3)))
    print(encode(List(1,1,1,2,2,3)))
  }
}
