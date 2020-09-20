package mergesortdemo

object refactoredWithT {
  def msort[T](xs: List[T])(lt: (T,T) => Boolean): List[T] = {
    val n = xs.length/2
    if (n == 0) xs // length is 0 or 1, no need to sort
    else {
      def merge(xs: List[T], ys: List[T]): List[T] = {
        println(xs.toString + " + " + ys.toString)
        (xs, ys) match {
          case (Nil, ys) => ys
          case (xs, Nil) => xs
          case (x::xs1, y::ys1) =>
            if(lt(x, y)) x :: merge(xs1, ys) // x is less than y
            else y :: merge(xs, ys1)
        }
      }
      val (fst, snd) = xs splitAt n // pattern matching with pair ( , )
      println("Splited : " + fst.toString + " | " + snd.toString)
      merge(msort(fst)(lt), msort(snd)(lt))
    }
  }

  def main(args: Array[String]): Unit = {
    println(msort(List("apple","bpppppppple","appple","bple","appple"))( (x:String, y:String) => x.length < y.length) )
    println(msort(List("apple","bpppppppple","appple","bple","appple"))( (x:String, y:String) => x.compareTo(y) < 0) )
  }
}
