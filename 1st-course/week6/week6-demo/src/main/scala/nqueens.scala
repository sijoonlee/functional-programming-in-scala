object nqueens {

  // there're eight queens to place on a chessboard(8X8) in a way no queen threaten the others

  // solution
  // place a queen on each row

  // @param n: n X n chess board
  def queens(n:Int): Set[List[Int]] = {
    def isSafe(col:Int, queens:List[Int]): Boolean ={
      for (row <- 0 until queens.length) {
        if(col == queens(row) - (row + 1)  || col == queens(row) + (row + 1) || col == queens(row)) return false
      }
      true
    }
    def isSafeAlt(col:Int, queens:List[Int]): Boolean ={
      val row = queens.length
      val queensWithRow = (row - 1 to 0 by -1) zip queens
      queensWithRow forall {
        case (r, c) => col != c && math.abs(col-c) != row - r
      }
    }
    def placeQueens(k: Int): Set[List[Int]] =
      if(k==0) Set(List())
      else
        for {
          queens <- placeQueens(k-1)
          col <- 0 until n
          if isSafeAlt(col, queens)
        } yield col :: queens
    placeQueens(n)
  }
  def show(queens:List[Int]) = {
    val lines =
      for (col <- queens.reverse)
        yield Vector.fill(queens.length)("* ").updated(col, "X ").mkString
    "\n" + (lines mkString "\n")
  }
  def main(args: Array[String]): Unit = {
    println(queens(4)) // HashSet(List(2, 0, 3, 1), List(1, 3, 0, 2))
    println(queens(4) take 1 map show)
  }

}
