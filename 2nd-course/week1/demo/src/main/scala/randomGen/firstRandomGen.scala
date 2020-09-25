package randomGen

object firstRandomGen {

  trait Generator[+T] {
    def generate: T
  }

  val integers = new Generator[Int] {
    val rand = new java.util.Random
    def generate = rand.nextInt()
  }

  val booleans = new Generator[Boolean] {
    def generate = integers.generate > 0
  }

  def main(args: Array[String]): Unit = {
  }

}
