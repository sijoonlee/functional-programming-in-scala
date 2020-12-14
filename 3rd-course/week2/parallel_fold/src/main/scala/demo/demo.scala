package demo

object demo {

  sealed abstract class Tree[A]
  case class Leaf[A](value:A) extends Tree[A]
  case class Node[A](left: Tree[A], right: Tree[A]) extends Tree[A]

  def reduce[A](t: Tree[A], f:(A,A) => A):A = t match {
    case Leaf(v) => v
    case Node(l, r) => f(reduce[A](l, f), reduce[A](r, f))
  }

//  def reducePar[A](t:Tree[A], f: (A,A) => A):A = t match {
//    case Leaf(v) => v
//    case Node(l, r) => {
//      val (lV, rV) = parallel(reduce[A](l, f), reduce[A](r, f))
//      f(lV, rV)
//    }
//  }

  def toList[A](t:Tree[A]): List[A] = t match {
    case Leaf(v) => List(v)
    case Node(l, r) => toList[A](l) ++ toList[A](r)
  }

  def map[A, B](t:Tree[A], f:A=>B): Tree[B] = t match {
    case Leaf(v) => Leaf(f(v))
    case Node(l, r) => Node(map[A,B](l, f), map[A,B](r, f))
  }

  // toList is same with reduce(map(t, List(_)), _++_)
  def mapReduceToList[A](t:Tree[A]): List[A] = reduce(map(t, List(_)), _++_)

  def main(args: Array[String]): Unit = {
    def tree = Node(Leaf(1), Node(Leaf(2), Leaf(3)))
    def fMinus = (x:Int, y:Int) => x - y
    def result  = reduce[Int](tree, fMinus)
    print(result) // 1-(2-3) = 2
  }
}
