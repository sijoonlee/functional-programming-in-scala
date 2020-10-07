
object midTest {


  def main(args: Array[String]): Unit = {
    implicit val n: Int = 42
    def f(implicit x: Int) = x
    println(f) // 42
    println(f(0)) // 0

    implicit val world: String = "World"
    def greet(implicit name: String) = s"Hello, $name!"
    println(greet) // Hello, World!

    }
}
