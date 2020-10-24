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
    if(end > totalLength) totalLength
    else end
  }
  def parBlur(src: Img, dst: Img, numTasks: Int, radius: Int): Unit = {
    // TODO implement using the `task` construct and the `blur` method
    val sizePerTask:Int = getTaskSize(numTasks, src.height)
    val startPoints = Range(0, src.width) by sizePerTask
    val chunks:IndexedSeq[(Int,Int)] = startPoints.map(s => (s,chunkEndIndex(s, sizePerTask, src.width)))
    val tasks = chunks.map{
      case (start, end) => task { blur(src, dst, start, end, radius) }
    }
    tasks.map(t => t.join())
    //    [Test Description] VerticalBoxBlur.parBlur with 32 tasks should execute 32 parallel tasks for a 32x64 image,
    //    each blurring one strip (3pts)(scalashop.BlurSuite)
    //    [Observed Error] assertion failed: (one of the tasks modifies pixels from different strips:
    //    (0,0), (0,1), (0,2), (0,3), (0,4), (0,5), (0,6), (0,7), (0,8), (0,9), (0,10), (0,11), (0,12), (0,13), (0,14),
    //    (0,15), (0,16), (0,17), (0,18), (0,19), (0,20), (0,21), (0,22), (0,23), (0,24), (0,25), (0,26), (0,27), (0,28),
    //    (0,29), (0,30), (0,31), (0,32), (0,33), (0,34), (0,35), (0,36), (0,37), (0,38), (0,39), (0,40), (0,41), (0,42),
    //    (0,43), (0,44), (0,45), (0,46), (0,47), (0,48), (0,49), (0,50), (0,51), (0,52), (0,53), (0,54), (0,55), (0,56),
    //    (0,57), (0,58), (0,59), (0,60), (0,61), (0,62), (0,63), (1,0), (1,1), (1,2), (1,3), (1,4), (1,5), (1,6), (1,7),
    //    (1,8), (1,9), (1,10), (1,11), (1,12), (1,13), (1,14), (1,15), (1,16), (1,17), (1,18), (1,19), (1,20), (1,21),
    //    (1,22), (1,23), (1,24), (1,25), (1,26), (1,27), (1,28), (1,29), (1,30), (1,31), (1,32), (1,33), (1,34), (1,35),
    //    (1,36), (1,37), (1,38), (1,39), (1,40), (1,41), (1,42), (1,43), (1,44), (1,45), (1,46), (1,47), (1,48), (1,49),
    //    (1,50), (1,51), (1,52), (1,53), (1,54), (1,55), (1,56), (1,57), (1,58), (1,59), (1,60), (1,61), (1,62), (1,63))


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
