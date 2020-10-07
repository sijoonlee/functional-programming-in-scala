trait LowPriorityImplicits {
  implicit val intOrdering: Ordering[Int] = Ordering.Int
}
object Main extends LowPriorityImplicits {

  implicit val intReverseOrdering: Ordering[Int] = Ordering.Int.reverse
  def main(args: Array[String]): Unit = {
    println(List(1, 2, 3).min) //3
  }
}