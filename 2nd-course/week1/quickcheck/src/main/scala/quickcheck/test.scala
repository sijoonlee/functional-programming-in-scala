package quickcheck
import scala.math.Ordering

object test {

  class BinomialHeapClass extends BinomialHeap {
    override type A = Int
    override def ord = scala.math.Ordering.Int
  }

  def main(args: Array[String]): Unit = {
    val myHeapGen = new BinomialHeapClass()
    val h1 = myHeapGen.insert(1, myHeapGen.empty)
    val h2 = myHeapGen.insert(2, h1)
    val h3 = myHeapGen.insert(3, h2)
    val h4 = myHeapGen.insert(4, h3)
    val h5 = myHeapGen.insert(5, h4)
    val h6 = myHeapGen.insert(6, h5)
    val h7 = myHeapGen.insert(7, h6)
    println(h1)
    println(h2)
    println(h3)
    println(h4)
    println(h5)
    println(h6)
    println(h7)

//    List([1:List()])
//    List([1:List([2:List()])])
//    List([3:List()], [1:List([2:List()])])
//    List([1:List([3:List([4:List()])], [2:List()])])
//    List([5:List()], [1:List([3:List([4:List()])], [2:List()])])
//    List([5:List([6:List()])], [1:List([3:List([4:List()])], [2:List()])])
//    List([7:List()], [5:List([6:List()])], [1:List([3:List([4:List()])], [2:List()])])
//

    // adding 4 to [ [3], [1,[2]] ]
    // -> ins(link([4],[3]),  [1,[2]] )
    // -> ins([3-[4]], [1-[2]])
    // -> ins( link([3-[4]], [1-[2]]), Nil )
    // -> ins( [1 - [3[4]],[2]], Nil )
    // -> [1 - [3[4]],[2]]
  }

}
