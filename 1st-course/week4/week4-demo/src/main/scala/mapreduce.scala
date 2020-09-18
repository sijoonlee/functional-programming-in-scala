import scala.collection.mutable.ListBuffer

object mapreduce {




//  def mapReduce(list:List[Char], f :Char => Int, state:List[(Char, Int)]):List[(Char, Int)] = {
//
//    def count(ch:Char):Int = {
//      def addNewChar:List[(Char,Int)] =
//        state concat List[(ch, 1)]
//
//      state.find(_._1 == ch) match {
//        case Some((c, i)) =>
//        case None => state concat List[(ch, 0)]
//      }
//    }
//
//    def loop(list:List[Char], f:Char => Int, state:List[(Char, Int)]):List[(Char, Int)] = list match {
//      case head::tail => {}
//    }
//
//  }

  def main(args: Array[String]): Unit = {

    var strings = List('a', 'b', 'c', 'a', 'a', 'c')

    var aaa = strings.groupBy(item => item).map(item => (item._1, item._2.length)).toList
    println(aaa)

    var bbb = aaa.sortBy(item => item._2).map(item => item._1)
    println(bbb)

    println('x' :: strings)
    println(strings ::: List('x'))
    println(strings.tail)

    var ccc = List(List('a','b','c'), List('e','f'))
    println(ccc.flatMap(l => l))
  }

}
