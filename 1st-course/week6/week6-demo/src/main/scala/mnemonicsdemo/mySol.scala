package mnemonicsdemo

object mySol {

  val mnemonics = Map(
    '1' -> "ABC", '2' -> "DEF", '3'-> "GHI", '4' -> "JKL",
    '5' -> "MNO", '6' -> "PQR", '7' -> "ST", '8' -> "UV",
    '9' -> "WX", '0' -> "YZ"
  )

  def translate(phoneNumber:String):Set[String] =  {

    (for (c <- phoneNumber ) yield c )
      .map (c => mnemonics(c))
      .foldLeft(List(List()):List[List[String]])(mix)
      .map( (l:List[String]) => l.reverse mkString "" )
      .toSet
  }

  def mix(acc:List[List[String]], str:String):List[List[String]] = {
    for {
      l <- acc
      c <- str
    } yield c.toString :: l
  }

  def main(args: Array[String]): Unit = {
    print(translate("123"))
  }
}
