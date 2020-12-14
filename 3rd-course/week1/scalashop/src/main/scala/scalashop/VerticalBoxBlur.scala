package scalashop

import org.scalameter._

object VerticalBoxBlurRunner {

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 5,
    Key.exec.maxWarmupRuns -> 10,
    Key.exec.benchRuns -> 10,
    Key.verbose -> true
  ) withWarmer(new Warmer.Default)

  def main(args: Array[String]): Unit = {
    val radius = 3
    val width = 1920
    val height = 1080
    val src = new Img(width, height)
    val dst = new Img(width, height)
    val seqtime = standardConfig measure {
      VerticalBoxBlur.blur(src, dst, 0, width, radius)
    }
    println(s"sequential blur time: $seqtime")

    val numTasks = 32
    val partime = standardConfig measure {
      VerticalBoxBlur.parBlur(src, dst, numTasks, radius)
    }
    println(s"fork/join blur time: $partime")
    println(s"speedup: ${seqtime.value / partime.value}")
  }

}

/** A simple, trivially parallelizable computation. */
object VerticalBoxBlur extends VerticalBoxBlurInterface {

  /** Blurs the columns of the source image `src` into the destination image
   *  `dst`, starting with `from` and ending with `end` (non-inclusive).
   *
   *  Within each column, `blur` traverses the pixels by going from top to
   *  bottom.
   */
  def blur(src: Img, dst: Img, from: Int, end: Int, radius: Int): Unit = {
    // TODO implement this method using the `boxBlurKernel` method
    for(
      x <- from until end;
      y <- 0 until src.height;
      if x >= 0 && x < src.width
    ) yield dst.update(x, y,  boxBlurKernel(src, x, y, radius))
  }

  /** Blurs the columns of the source image in parallel using `numTasks` tasks.
   *
   *  Parallelization is done by stripping the source image `src` into
   *  `numTasks` separate strips, where each strip is composed of some number of
   *  columns.
   */
  def getTaskSize(numTasks:Int, total:Int) = {
    // integer ceil
    (numTasks + total -1)/numTasks
    // ex) numTasks:3, total:10
    // --> 3, 3, 3 (X) - one missing, can't recover
    // --> 4, 4, 4 (O) - two more, but will be cut off
  }
  def chunkEndIndex(startIndex:Int, stepSize:Int, totalLength:Int):Int = {
    val end = startIndex + stepSize
    if(end >= totalLength) totalLength
    else end
  }
  def parBlur(src: Img, dst: Img, numTasks: Int, radius: Int): Unit = {
    // TODO implement using the `task` construct and the `blur` method
    val sizePerTask:Int = getTaskSize(numTasks, src.width)
    val startPoints = Range(0, src.width) by sizePerTask
    val chunks:IndexedSeq[(Int,Int)] = startPoints.map(s => (s,chunkEndIndex(s, sizePerTask, src.width)))
    val tasks = chunks.map{
      case (start, end) => task { blur(src, dst, start, end, radius) }
    }
    tasks.map(t => t.join())

    //    val colsPerTaks:Int = Math.max(src.width / numTasks,1)
//    val startPoints = Range(0, src.width) by colsPerTaks
//
//    val tasks = startPoints.map(t => {
//      task {
//        blur(src, dst, t, t + colsPerTaks, radius)
//      }
//    })
//    tasks.map(t => t.join())
  }
}
