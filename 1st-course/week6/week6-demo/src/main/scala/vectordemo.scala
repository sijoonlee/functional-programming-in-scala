object vectordemo {

  def main(args: Array[String]): Unit = {
    val oneTwoThree = Vector(1,2,3)
    val fourFive = Vector(4,5)

    println(oneTwoThree ++ fourFive)

    // instead of x :: xs
    // use x +: xs ===> create a new vector with leading x, followed by Vector xs
    // or xs :+ x ====> create a new vector with trailing x, preceded by Vector xs
    println(fourFive :+ 6)


  }


}
