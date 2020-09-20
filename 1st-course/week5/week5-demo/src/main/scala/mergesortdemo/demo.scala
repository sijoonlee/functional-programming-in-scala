package mergesortdemo

object demo {

  def msort(xs: List[Int]): List[Int] = {
    val n = xs.length/2
    if (n == 0) xs // length is 0 or 1, no need to sort
    else {
      def merge(xs: List[Int], ys: List[Int]): List[Int] = {
        println(xs.toString + " + " + ys.toString)
        xs match {
          case Nil => ys
          case x :: xs1 => ys match {
            case Nil => xs
//            case List(y) => // this will be checked by case Nil in the next recursion
//              if (x<y) List(x, y)
//              else List(y, x)
            case y :: ys1 =>
              if (x<y) x :: merge(xs1, ys)
              else y :: merge(xs, ys1)
          }
        }
      }
      val (fst, snd) = xs splitAt n // pattern matching with pair ( , )
      println("Splited : " + fst.toString + " | " + snd.toString)
      merge(msort(fst), msort(snd))
    }
  }

  def main(args: Array[String]): Unit = {
    println(msort(List(4,3,1,2,5)))
  }



}
