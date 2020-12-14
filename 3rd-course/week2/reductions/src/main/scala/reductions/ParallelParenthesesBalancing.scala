package reductions

import scala.annotation._
import org.scalameter._

object ParallelParenthesesBalancingRunner {

  @volatile var seqResult = false

  @volatile var parResult = false

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 40,
    Key.exec.maxWarmupRuns -> 80,
    Key.exec.benchRuns -> 120,
    Key.verbose -> true
  ) withWarmer(new Warmer.Default)

  def main(args: Array[String]): Unit = {
    val length = 100000000
    val chars = new Array[Char](length)
    val threshold = 10000
    val seqtime = standardConfig measure {
      seqResult = ParallelParenthesesBalancing.balance(chars)
    }
    println(s"sequential result = $seqResult")
    println(s"sequential balancing time: $seqtime")

    val fjtime = standardConfig measure {
      parResult = ParallelParenthesesBalancing.parBalance(chars, threshold)
    }
    println(s"parallel result = $parResult")
    println(s"parallel balancing time: $fjtime")
    println(s"speedup: ${seqtime.value / fjtime.value}")
  }
}

object ParallelParenthesesBalancing extends ParallelParenthesesBalancingInterface {

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
   */
  def count(t:Char):Int = {
    if(t == '(') 1
    else if(t == ')') -1
    else 0
  }


  def balance(chars: Array[Char]): Boolean = {
    @tailrec
    def recurCount(subChars:Array[Char], total:Int):Int = {
      subChars match {
        case Array() => 0
        case Array(t) => count(t) + total
        case headAndTail => {
          val subtotal = total + count(headAndTail.head)
          if (subtotal < 0) -1 // halt the recursive process if something like this happens Array( ')', '(' )
          else recurCount(headAndTail.tail, subtotal)
        }
        //        case Array(head,tail@_*) => { // here, tail is Seq[Char], not List[Char]
        //          if(head == '(') recurCheck(tail, counter + 1)
        //          else if(head == ')') recurCheck(tail, counter -1)
        //          else recurCheck(tail, counter)
        //        }
      }
    }
    recurCount(chars, 0) == 0
  }

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
   */
  def parBalance(chars: Array[Char], threshold: Int): Boolean = {
    @tailrec
    def recurCount(subChars:Array[Char], total:Int):Int = {
      subChars match {
        case Array() => 0
        case Array(t) => count(t) + total
        case headAndTail => {
          recurCount(headAndTail.tail, total + count(headAndTail.head))
        }
      }
    }
    def traverse(from: Int, until: Int): Int = {
      val subChar = chars.slice(from, until)
      recurCount(subChar, 0)
    }

    def reduce(from: Int, until: Int):Int = {
      if(until - from <= threshold) traverse(from, until)
      else {
        val mid = from + (until - from) / 2
        val (left, right) = parallel(reduce(from, mid), reduce(mid, until))
        if (left < 0) -1 // halt the recursive process if something like this happens Array( ')', '(' )
        else left + right
      }
    }
    reduce(0, chars.length) == 0
  }

  // For those who want more:
  // Prove that your reduction operator is associative!


  def main(args: Array[String]): Unit = {
    val testChars = Array('(', ')')
    println(balance(testChars))
    println(parBalance(testChars, 1))
    val testChars1 = Array(')', ')')
    println(balance(testChars1))
    println(parBalance(testChars1, 1))
    val testChars2 = Array(')', '(')
    println(balance(testChars2))
    println(parBalance(testChars2, 1))
    val testChars3 = Array(')', '(', '(')
    println(balance(testChars3))
    println(parBalance(testChars3, 1))

  }
}
