package testing

import forcomp._

object forTesting {
  type Word = String
  type Sentence = List[Word]
  type Occurrences = List[(Char, Int)]

  def wordOccurrences1(w: Word): Occurrences =
    w.groupMapReduce(identity)(_ => 1)(_ + _).toList

  def wordOccurrences2(w: Word): Occurrences =
    w.toSeq.groupBy(key => key).view.mapValues(_.map(_ => 1).reduce(_ + _)).toList

  // def groupMapReduce[K, B](key: A => K)(f: A => B)(reduce: (B, B) => B): Map[K, B]
  // is equivalent to
  // groupBy(key).mapValues(_.map(f).reduce(reduce))

  def wordOccurrences(w: Word): Occurrences =
    w.toSeq.groupBy(item => item).map(item => (item._1, item._2.length)).toList

  def sentenceOccurrences(s: Sentence): Occurrences = {
    (for (w <- s) yield wordOccurrences(w))
      .flatten
      .groupBy(item => item._1)
      .map(item => (item._1, item._2.map(x => x._2).sum))
      .toList
  }

/* get sub occurrences
* param: occurrence e.g. ('a',2)
* return: sub-occurrences including self e.g. ('a',2), ('a',1), ('a',0)
* */
  def getSubOcc(occ: (Char, Int)): Occurrences = {
    val (c, n) = occ
    (0 to n).foldLeft(List(): Occurrences)((acc, i) => (c, i) :: acc)
  }

  def combinations(occurrences: Occurrences): List[Occurrences] = occurrences match {
    case Nil => List()
    case List(x) => getSubOcc(x).map(item => List(item))
    case head :: tail => {
      (for {
        sub <- getSubOcc(head)
        rest: Occurrences <- combinations(tail): List[Occurrences]
      } yield sub :: rest) map ( listOcc => listOcc.filter(_._2 != 0))
    }
  }


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

    val test = List("a", "a", "b", "a", "c")
    println(test.groupBy(item => item)) // HashMap(a -> List(a, a, a))
    println(test.groupBy(item => item).map(item => (item._1, item._2.length))) // HashMap(a -> 3, b -> 1, c -> 1)

    val sentence = List("word1", "word2", "word3")
    println(sentenceOccurrences(sentence))

    val occ = List(('a', 2), ('b', 1))

    // println(getSubOcc(('a',2)))
    println(combinations(occ))
    println(combinations(occ).length)

    val occ2 = List(('a', 1), ('b', 1))

    println(Anagrams.subtract(occ, occ2))

    println(Anagrams.sentenceAnagrams(List("I","Love","you")))
  }
}