package mapdemo

object mapdemo1 {

  def showCapital(capitalOfCountry:Map[String, String])(country:String):String =
    capitalOfCountry.get(country) match {
      case Some(capital) => capital
      case None => "missing data"
    }

  def main(args: Array[String]): Unit = {
    val capitalOfCountry = Map("US" -> "Washington", "S.Korea" -> "Seoul")
    println(showCapital(capitalOfCountry)("US"))
    println(showCapital(capitalOfCountry)("UK"))

    val capWithDefault = capitalOfCountry withDefaultValue "<unknown>"
    println(capWithDefault("UK"))


    // sort
    val fruit = List("apple", "orange", "pear")
    println(fruit sortWith(_.length < _.length)) // List(pear, apple, orange)
    println(fruit.sorted) //List(apple, orange, pear)

    // groupBy
    println(fruit groupBy(_.head)) // HashMap(a -> List(apple), p -> List(pear), o -> List(orange))


  }
}
