import java.util.NoSuchElementException

object ListDemo {
  trait List[T] {
    def isEmpty: Boolean
    def head: T
    def tail: List[T]
  }

  class Cons[T](val head: T, val tail: List[T]) extends List[T] {
    def isEmpty = false
    // def head, def tail is already implied from default constructor
  }

  class Nil[T] extends List[T] {
    def isEmpty = true
    // Nothing is subtype of any other types
    def head: Nothing = throw new NoSuchElementException("Nil.head")
    def tail: Nothing = throw new NoSuchElementException("Nil.tail")
  }

  def singleton[T](elem: T) = new Cons[T](elem, new Nil[T])

  def main(args:Array[String]):Unit = {
    singleton[Int](1)
    singleton[Boolean](false)
    singleton(1) // compiler infers the type is Int
    singleton(false) // compiler infers the type is Boolean
  }
}
