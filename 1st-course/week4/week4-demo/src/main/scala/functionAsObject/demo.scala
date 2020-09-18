package functionAsObject

object demo{
  trait List[+T]{
    // [+T] is important!
    // otherwise, "val x: List[String] = Nil" will make an error
    // since Nil is List[Nothing], Nothing is a subtype of String
    def isEmpty :Boolean
    def head :T
    def tail :List[T]

    // covariant type T occurs in contravariant position in type T of value elem
    // think of the case where we have Empty and NonEmpty classes share a parent IntSet
    // and we have a list - ys: List[NonEmpty]
    // Running ys.prepend(Empty) has an error
    // since Cons class's head and tail should be the same class
    // def prepend(elem :T): List[T] = new Cons(elem, this)

    // we can use lower bound
    // this passes the variance check because:
    // covariant type parameters may appear in lower bounds
    // contravariant type parameters may appear in upper bounds
    // * when running ys.prepend(Empty) where ys is a instance of NonEmpty
    // type inference will choose List[IntSet]
    def prepend [U >: T](elem: U): List[U] = new Cons(elem, this)
  }

  class Cons[T](val head :T, val tail :List[T]) extends List[T] {
    def isEmpty = false
  }

  // this one works, but it would be wasteful
  // if we instantiate every time we need just Empty object
//  class Nil[T] extends List[T] {
//    def isEmpty :Boolean = true
//    def head :Nothing = throw new NoSuchElementException("Nil.head")
//    def tail :Nothing = throw new NoSuchElementException("Nil.tail")
//  }

//  object Nil extends List[T] { // we can't, we need typing
//    def isEmpty :Boolean = true
//    def head :Nothing = throw new NoSuchElementException("Nil.head")
//    def tail :Nothing = throw new NoSuchElementException("Nil.tail")
//  }

  // this one works, but we need to make List covariant
  object Nil extends List[Nothing] { // Nothing is a subtype to any Class
    def isEmpty :Boolean = true
    def head :Nothing = throw new NoSuchElementException("Nil.head")
    def tail :Nothing = throw new NoSuchElementException("Nil.tail")
  }
  object List{
    // List(1,2) = list.apply(1,2)
    def apply[T](x1 :T, x2 :T): List[T] = new Cons(x1, new Cons(x2, Nil))
    def apply[T]() = Nil
  }

  def main(args: Array[String]): Unit = {
    List.apply(1, 2)
    val x: List[String] = Nil
  }
}
