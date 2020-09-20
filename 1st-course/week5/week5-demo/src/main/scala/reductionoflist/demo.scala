package reductionoflist

object demo {

  // ReduceLeft
  // List(x1, ..., xn) reduceLeft op = (((...( x1 op x2 ) op x3 ) op x4 ) ... )))) op xn

  // def sum(xs: List[Int]) = (0::xs) reduceLeft ((x, y) => x+y)
  def sum(xs: List[Int]) = (0::xs) reduceLeft ( _ + _ ) // first _ means x, second _ means y
  // def product(xs: List[Int]) = (1::xs) reduceLeft ((x, y) => x*y)
  def product(xs: List[Int]) = (1::xs) reduceLeft ( _ * _ )


  // FoldLeft
  // foldLeft is like reduceLeft but takes an accumulator z
  // which is returned when foldLeft is called on an empty list
  // (List(x1, ..., xn) foldLeft z)(op) = (..(z op x1) op ... ) op xn

  def sumFoldLeft(xs: List[Int]): Int = xs.foldLeft(0)(_+_)
  def prodFoldLeft(xs: List[Int]): Int = (xs foldLeft 1)(_*_)

  // and there're ReduceRight and FoldRight
  def concat[T](xs: List[T], ys:List[T]):List[T] =
    (xs foldRight ys)(_::_)
  // foldLeft can't be here

// (xs foldLeft ys)(_::_) --> it starts with ys :: x1 where ys is List, x1 is T (invalid)
//***********************************************************
//  override def foldLeft[B](z: B)(op: (B, A) => B): B = {
//    var acc = z
//    var these: LinearSeq[A] = coll
//    while (!these.isEmpty) {
//      acc = op(acc, these.head)
//      these = these.tail
//    }
//    acc
//  }

// (xs foldRight ys)(_::_) --> it starts with xn :: ys where xn is T, ys is List (legit)
// ***********************************************************
//  final override def foldRight[B](z: B)(op: (A, B) => B): B = {
//    var acc = z
//    var these: List[A] = reverse
//    while (!these.isEmpty) {
//      acc = op(these.head, acc)
//      these = these.tail
//    }
//    acc
//  }

  def mapFun[T, U](xs: List[T], f: T => U): List[U] =
    (xs foldRight List[U]())( (x, acc) => f(x) :: acc )

  def lengthFun[T](xs: List[T]): Int =
    (xs foldRight 0)( (x,acc) => acc+1  )

  def main(args: Array[String]): Unit = {
    println(mapFun(List(1,2,3),((x:Int) => 2*x)))
    println(lengthFun(List(1,2,3)))
  }

}
