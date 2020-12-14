import scala.collection.parallel.CollectionConverters._

object Accessor extends App  {

  // Accessor: returns a sigle value
  // ex) sum, max, fold, count, aggregate ...

  // Transformer: returns new collection as result instead of single value
  // ex: map, filter, flatMap, groupBy, ...

  // foldLeft is non-parallelizable
  // because of the signature of the function (B,A) => B
  // def foldLeft[B](z:B)( f: (B,A) => B ): B

  // we need fold
  def fold[A](z:A)(f:(A,A) => A):A = ???

  def sum(xs:Array[Int]):Int = {
    xs.par.fold(0)(_+_)
  }

  def max(xs:Array[Int]):Int = {
    xs.par.fold(Int.MinValue)(math.max)
  }

  def play(a:String, b:String): String = List(a,b).sorted match {
    case List("paper","scissors") => "scissors"
    case List("paper", "rock") => "paper"
    case List("rock", "scissors") => "rock"
    case List(a,b) if a == b => a
    case List("",b) => b
  }
  Array("paper", "rock", "paper", "scissors").par.fold("")(play)



  // more powerful - aggregate

  def aggregate[A, B](z:B)(f:(B,A)=>B, g:(B,B)=>B):B = ???
  // f is preprocessor
  // g is working in parallel since it is monoid with z:B

  Array('E','P','F','L').par.aggregate(0)(
    (count, c) => if(isVowel(c)) count + 1 else count, // f
    _ + _
  )

  def isVowel(c:Char):Boolean = {
    if (c == 'A' | c == 'E' | c == 'I' | c == 'O' | c== 'U')
      true
    else
      false
  }

}
