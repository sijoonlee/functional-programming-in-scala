package scalashop
import HorizontalBoxBlur._

object testingIdea {
  def test(height:Int, numTasks: Int): Unit = {
    val sizePerTask:Int = getTaskSize(numTasks, height)
    val startPoints = Range(0, height) by sizePerTask
    val chunks:IndexedSeq[(Int,Int)] = startPoints.map(s => (s,chunkEndIndex(s, sizePerTask, height)))
    val tasks = chunks.map{
      case (start, end) => println(start, end)
    }
    val tasks2 = startPoints.map( t => print(t, t + sizePerTask))
  }
  def main(args: Array[String]): Unit = {

    test(32, 32)
    println("----------")



  }

}
