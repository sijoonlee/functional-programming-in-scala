package mergesortdemo

import scala.math.Ordering

/* Rule for Implicit Parameters
The compiler will search an implicit definition that
- marked 'implicit'
- has a type compatible with T
- visible at the point of the function call
- or defined in a comparison object associated with T

==> If there is a single(most specific) definition, it will be taken as argument
==> otherwise, error
 */


object refactoredWithImplictParam {
  def msort[T](xs: List[T])(implicit ord: Ordering[T]): List[T] = {
    val n = xs.length/2
    if (n == 0) xs // length is 0 or 1, no need to sort
    else {
      def merge(xs: List[T], ys: List[T]): List[T] = {
        println(xs.toString + " + " + ys.toString)
        (xs, ys) match {
          case (Nil, ys) => ys
          case (xs, Nil) => xs
          case (x::xs1, y::ys1) =>
            if(ord.lt(x, y)) x :: merge(xs1, ys) // x is less than y
            else y :: merge(xs, ys1)
        }
      }
      val (fst, snd) = xs splitAt n // pattern matching with pair ( , )
      println("Splited : " + fst.toString + " | " + snd.toString)
      merge(msort(fst), msort(snd))
    }
  }

  def main(args: Array[String]): Unit = {
    println(msort(List("apple","bpppppppple","appple","bple","appple")))
  }
}
