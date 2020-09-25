package function1

object function1demo {

  /*
  trait Function1[-A, +R] {
    def apply(x:A):R
  }

  new Function1[JBinding, String] {
    def apply(x:JBinding) = x match {
      case (key, value) => key + ": " + show(value)
    }
  }


  We can subclass the function type
  trait Map[Key, Value] extends (Key=>Value)
  trait Seq[Elem] extends (Int=>Elem)

  We can write elem(i) ~ it's sequence indexing
  */


  def main(args: Array[String]): Unit = {
    /*
    The partial function trait is defined as follows:
      trait PartialFunction[-A, +R] extends Function1[-A,+R] {
        def apply(x:A):R
        def isDefinedAt(x:A):Boolean
      }
     */

    val f1: PartialFunction[String, String] = { case "ping" => "pong"}
    println(f1.isDefinedAt("ping")) // true
    println(f1.isDefinedAt("abcd")) // false
    /* isDefinedAt looks like
    def isDefinedAt(x:String) = x match {
      case "ping" => true
      case _ => false
    }
    */

    val f2: PartialFunction[List[Int], String] = {
      case Nil => "zero"
      case x :: rest => {
        rest match {
          case Nil => "one"
        }
      }
    }

    println(f2.isDefinedAt(List(1,2,3))) // true
    f2(List(1,2,3)) // MatchError
    // even though there is no match eventually,
    // isDefinedAt consider List(1,2,3) as true
    // Since it matches at the first time from case x::rest



  }


}
