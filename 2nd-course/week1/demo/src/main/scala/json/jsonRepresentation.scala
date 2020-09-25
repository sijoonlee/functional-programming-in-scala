package json

object jsonRepresentation {

  abstract class JSON
  case class JSeq (elems: List[JSON]) extends JSON
  case class JObj (bindings: Map[String, JSON]) extends JSON
  case class JNum (num: Double) extends JSON
  case class JStr (str: String) extends JSON
  case class JBool (b: Boolean) extends JSON
  case object JNull extends JSON

  def show(json:JSON): String = json match {
    case JSeq(elems) =>
      "[" + (elems map show mkString ", ") + "]"
    case JObj(bindings) =>
      val assocs = bindings map {
        case (key, value) => "\"" + key + "\": " + show(value)
      }
      "{" + (assocs mkString ", ") + "}"
    case JNum(num) => num.toString
    case JStr(str) => "\"" + str + "\""
    case JBool(b) => b.toString
    case JNull => "null"
  }


  def main(args: Array[String]): Unit = {
    val data = JObj(Map(
      "name" -> JStr("John"),
      "address" -> JObj(Map(
        "city" -> JStr("Kingston"),
        "state" -> JStr("Ontario")
      )),
      "phoneNumbers" -> JSeq(List(
        JObj(Map(
          "type" -> JStr("home"),
          "number" -> JStr("111-111-1111")
        )),
        JObj(Map(
          "type" -> JStr("work"),
          "number" -> JStr("211-111-1111")
        ))
      ))
    ))

    print(show(data))
  }


}
