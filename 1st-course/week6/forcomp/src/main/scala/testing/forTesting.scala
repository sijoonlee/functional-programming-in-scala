package testing

object forTesting {
  type Word = String
  type Sentence = List[Word]
  type Occurrences = List[(Char, Int)]
  def wordOccurrences1(w: Word): Occurrences =
    w.groupMapReduce(identity)(_=>1)(_ + _).toList
  def wordOccurrences2(w: Word): Occurrences =
    w.toSeq.groupBy(key => key).view.mapValues(_.map( _ => 1).reduce(_+_)).toList
  // def groupMapReduce[K, B](key: A => K)(f: A => B)(reduce: (B, B) => B): Map[K, B]
  // is equivalent to
  // groupBy(key).mapValues(_.map(f).reduce(reduce))

//  def reduceLeft[B >: A](op: (B, A) => B): B = {
//    val it = iterator
//    if (it.isEmpty)
//      throw new UnsupportedOperationException("empty.reduceLeft")
//
//    var first = true
//    var acc: B = 0.asInstanceOf[B]
//
//    while (it.hasNext) {
//      val x = it.next()
//      if (first) {
//        acc = x
//        first = false
//      }
//      else acc = op(acc, x)
//    }
//    acc
//  }


  def main(args: Array[String]): Unit = {
    println(wordOccurrences("aabba"))

    val test = List("a","a","b","a","c")
    println(test.groupBy(item => item)) // HashMap(a -> List(a, a, a))
    println(test.groupBy(item => item).map( item => (item._1, item._2.length))) // HashMap(a -> 3, b -> 1, c -> 1)

  }
}
