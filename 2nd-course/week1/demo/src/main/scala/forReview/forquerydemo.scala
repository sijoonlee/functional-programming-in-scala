package forReview

object forquerydemo {

  case class Book(title:String, authors:List[String])
  val books: List[Book] = List(
    Book(title = "Computer Programs",
      authors = List("Abelson", "Harald", "Bird Anakin")),
    Book(title = "Introduction to Programming",
      authors = List("Bird Richard", "Harald", "Abelson")),
    Book(title = "Happiness",
      authors = List("Abelson", "Jane"))
  )

  def main(args: Array[String]): Unit = {
    // find the titles of books whose author's name is 'Bird'
    println(
      for(b<-books;a<-b.authors if a startsWith "Bird,")
        yield b.title
    )

    // flatMap version
    println(
      books
        .flatMap( b => for(a <- b.authors if a startsWith "Bird") yield b.title )
    )

    // withFilter
    println(
      books
        .flatMap {
          case b => b.authors
            .withFilter { a => a startsWith "Bird" }
            .map { case _ => b.title }
          case _ => Nil
        }
    )



    // find all the books which have "Program" in the title
    println(
      for(b<-books if (b.title indexOf "Program") >= 0)
        yield b.title
    )

    // find the names of all authors who have written at least two books present in the database
    println(
      {
        for {
          b1 <- books
          b2 <- books
          if b1.title < b2.title
          // if b1 != b2, the author's name shows up twice for Harold
          // if b1.title < b2.title, it still shows Abelson three times (since 3 combination of book pairs)
          a1 <- b1.authors
          a2 <- b2.authors
          if a1 == a2
        } yield a1
      }.distinct // we can add .distinct
      // or we can change books as Set
      // val books: Set[Book]
    )
  }

}
