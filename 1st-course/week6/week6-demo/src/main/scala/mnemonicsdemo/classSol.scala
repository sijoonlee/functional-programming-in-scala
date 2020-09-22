package mnemonicsdemo

object classSol {
  val words = List("a", "bc", "abc")
  val mnemonics = Map(
    '1' -> "ABC", '2' -> "DEF", '3'-> "GHI", '4' -> "JKL",
    '5' -> "MNO", '6' -> "PQR", '7' -> "ST", '8' -> "UV",
    '9' -> "WX", '0' -> "YZ"
  )

  val charCode: Map[Char, Char] =
    for ((digit, str) <- mnemonics; ltr <- str) yield ltr -> digit

  def wordCode(word:String): String =
    word.toUpperCase map charCode

  def wordsForNum: Map[String, Seq[String]] =
    words groupBy wordCode

  def encode(number:String): Set[List[String]] =
    if(number.isEmpty) Set(List())
    else {
      for{
        split <- 1 to number.length
        word <- wordsForNum(number take split)
        rest <- encode(number drop split)
      } yield word :: rest
    }.toSet

  def main(args: Array[String]): Unit = {
    println(wordCode("Java"))
    println(wordsForNum) // HashMap(11 -> List(bc), 1 -> List(a), 111 -> List(abc))
    println(encode("11")) // Set(List(a, a), List(bc))
    println(encode("111")) // Set(List(a, a, a), List(a, bc), List(bc, a), List(abc))

  }

}
