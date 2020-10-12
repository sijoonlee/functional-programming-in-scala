object conversions extends App{
  import scala.language.implicitConversions

  case class Rational(numerator: Int, denominator: Int)

  object Rational {
    implicit def fromInt(n: Int) = Rational(n, 1)
  }

  val r: Rational = 42
  println(r) // Rational(42,1)

}
