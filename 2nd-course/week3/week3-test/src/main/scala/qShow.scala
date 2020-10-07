
object qShow extends App{
  trait Show[A] {
    def apply(a: A): String
  }
  object Show {
    implicit val showInt: Show[Int] = new Show[Int] {
      def apply(n: Int): String = s"Int($n)"
    }
  }

  println(implicitly[Show[Int]]) // qShow$Show$$anon$1@7bb11784

  // Rewrite the last line to show explicitly the implicit argument
  // that has been inferred by the compiler. It should look like the following:
  // implicitly[Show[Int]](...)
  // def implicitly[A](implicit arg: A): A = arg

  println(implicitly[Show[Int]](Show.showInt)) //qShow$Show$$anon$1@7bb11784
}

