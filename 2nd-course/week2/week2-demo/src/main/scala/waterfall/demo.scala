package waterfall

object demo {
  def main(args: Array[String]): Unit = {
    val problem = new Pouring(Vector(4, 7))
    println(problem.moves) // Vector(Empty(0), Empty(1), Fill(0), Fill(1), Pour(0,1), Pour(1,0))

    println(problem.pathSets.take(3).toList)
    /*List(
    Set(--> Vector(0, 0)),
    HashSet(Pour(0,1)--> Vector(0, 0),
            Empty(0)--> Vector(0, 0),
            Fill(0)--> Vector(4, 0),
            Fill(1)--> Vector(0, 7),
            Pour(1,0)--> Vector(0, 0),
            Empty(1)--> Vector(0, 0)),
    HashSet(Empty(1) Fill(1)--> Vector(0, 7),
            Empty(1) Empty(0)--> Vector(0, 0),
            Pour(1,0) Pour(0,1)--> Vector(0, 0),
            Pour(1,0) Pour(1,0)--> Vector(0, 0),
            Fill(0) Fill(0)--> Vector(4, 0),
            Fill(1) Pour(1,0)--> Vector(4, 3),
            Empty(0) Empty(0)--> Vector(0, 0),
            Pour(0,1) Empty(1)--> Vector(0, 0),
            Empty(1) Pour(1,0)--> Vector(0, 0),
            Pour(0,1) Fill(1)--> Vector(0, 7),
            Empty(1) Empty(1)--> Vector(0, 0),
            Empty(1) Pour(0,1)--> Vector(0, 0),
            Pour(0,1) Pour(0,1)--> Vector(0, 0),
            Pour(0,1) Empty(0)--> Vector(0, 0),
            Fill(1) Fill(0)--> Vector(4, 7),
            Empty(1) Fill(0)--> Vector(4, 0),
            Empty(0) Fill(1)--> Vector(0, 7),
            Pour(0,1) Fill(0)--> Vector(4, 0),
            Pour(1,0) Fill(0)--> Vector(4, 0),
            Empty(0) Empty(1)--> Vector(0, 0),
            Fill(0) Pour(1,0)--> Vector(4, 0),
            Pour(1,0) Empty(1)--> Vector(0, 0),
            Fill(0) Pour(0,1)--> Vector(0, 4),
            Fill(1) Empty(0)--> Vector(0, 7),
            Fill(0) Empty(1)--> Vector(4, 0),
            Empty(0) Fill(0)--> Vector(4, 0),
            Empty(0) Pour(0,1)--> Vector(0, 0),
            Pour(0,1) Pour(1,0)--> Vector(0, 0),
            Fill(0) Fill(1)--> Vector(4, 7),
            Fill(1) Pour(0,1)--> Vector(0, 7),
            Pour(1,0) Empty(0)--> Vector(0, 0),
            Empty(0) Pour(1,0)--> Vector(0, 0),
            Pour(1,0) Fill(1)--> Vector(0, 7),
            Fill(1) Empty(1)--> Vector(0, 0),
            Fill(1) Fill(1)--> Vector(0, 7),
            Fill(0) Empty(0)--> Vector(0, 0)))
    */

    println(problem.solution(6).toList)
    /*
    List(Fill(1) Pour(1,0) Empty(0) Pour(1,0) Fill(1) Pour(1,0)--> Vector(4, 6),
        Fill(1) Pour(1,0) Empty(0) Pour(1,0) Fill(1) Pour(1,0) Empty(0)--> Vector(0, 6))
     */

  }


}
