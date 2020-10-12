object conversions2 extends App {
  import scala.language.implicitConversions

  implicit class HasIsEven(n: Int) {
    def isEven: Boolean = n % 2 == 0
  }

  println(42.isEven)
  println(new HasIsEven(42).isEven)

}
