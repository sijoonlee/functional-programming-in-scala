package forReview

object translationdemo {

  // map, flatMap, filter can be expressed using for

  def mapFun[T, U](xs: List[T], f:T=>U): List[U]=
    for( x <- xs ) yield f(x)

  def flatMapFun[T, U](xs: List[T], f:T=>Iterable[U]): List[U] =
    for( x <- xs; y <- f(x) ) yield y

  def filterFun[T](xs:List[T], p:T=>Boolean):List[T] =
    for( x <- xs if p(x)) yield x

  // In reality, Scala compiler translates For-expression to map/flatMap/a lazy variant of filter
  def ex1For(e :List[Int]):List[Int] =
    for (x <- e) yield x * 2

  def ex1Map(e: List[Int]):List[Int] =
    e.map( x => 2*x )

  def ex2For(e: List[String]):List[Char] =
    for (x <- e if x.toLowerCase > "d"; c <- x) yield c

  def ex2Filter(e: List[String]):List[Char] =
    for (x <- e.withFilter(x=>x.toLowerCase > "d"); c <- x) yield c
  // withFilter filters arguments for the following map or flatMap function
  // withFilter doesn't produce intermediate list

  // nested for
  def ex3For(e1: List[String], e2: List[String]):List[String] =
    for (x <- e1; y <- e2; cx <- x; cy <- y ) yield cx + "," + cy

  def ex3FlatMap(e1: List[String], e2: List[String]):List[String] =
    e1.flatMap( x => for (y <- e2 ; cx <- x; cy <- y ) yield cx + "," + cy)

}
