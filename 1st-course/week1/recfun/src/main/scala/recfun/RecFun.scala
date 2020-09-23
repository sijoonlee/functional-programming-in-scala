package recfun
import scala.annotation.tailrec

object RecFun extends RecFunInterface {

  def main(args: Array[String]): Unit = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(s"${pascal(col, row)} ")
      println()
    }
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int =
    if (c == 0 || c == r) 1
    else pascal(c-1, r-1) + pascal(c, r-1)

  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = {
    @tailrec
    def loop(acc: Int, chars: List[Char]): Int = {
      if (chars.isEmpty) acc
      else if (chars.head == '(') loop(acc+1, chars.tail)
      else if (chars.head == ')' && acc > 0) loop(acc-1, chars.tail)
      else if (chars.head == ')' && acc <= 0) -1
      else loop(acc, chars.tail)
    }
    if (loop(0, chars) == 0) true else false
  }
  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = money match {
    case 0 => 1
    case i if (i > 0) => {
      if (!coins.isEmpty) countChange(money - coins.head, coins) + countChange(money, coins.tail)
      else 0
    }
    case _ => 0
  }
//
//  if(money == 0) 1
//    else if(money > 0 && !coins.isEmpty) {
//      countChange(money - coins.head, coins) + countChange(money, coins.tail)
//    }
//    else 0
//  }
}
