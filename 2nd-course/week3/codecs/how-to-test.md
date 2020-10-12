sbt:progfun2-codecs> console

scala> import codecs._; import codecs._

scala> Util.parseJson(""" { "name": "Bob", "age": 10 } """)
val res0: Option[codecs.Json] = Some(Obj(Map(name -> Str(Bob), age -> Num(10))))

scala> res0.flatMap(_.decodeAs[Person]) // This will crash until you implement it in this assignment
val res1: Option[codecs.Person] = Some(Person(Bob,10))

scala> implicitly[Encoder[Int]]
val res2: codecs.Encoder[Int] = codecs.Encoder$$anon$1@74d8fde0

scala> res2.encode(42)
val res3: codecs.Json = Num(42)

scala> :quit 