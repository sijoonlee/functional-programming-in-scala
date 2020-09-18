package subtyping

object demo {

  class Parent { def name:String = "Jone"}
  class ChildA extends Parent { override def name:String = "Jone Junior A" }
  class ChildB extends Parent { override def name:String = "Jone Junior B" }

  /* BOUND
  * S <: T means S is a subtype of T (Upper bound)
  * S >: T means S is a supertype of T (Lower bound)
  * S >: A <: B means S is a supertype of A / a subtype of B (Mixed bound)
  */

  def getNameSub[S <: Parent](obj :S): String =
    obj.name

  def getNameSuper[S >: ChildA](obj :S): String =
    obj.toString
    // obj.name
    //    Error: value name is not a member of type parameter S
    //    Since supertype could be Parent, AnyRef and Any

  def getNameMixed[S >: ChildA <: Parent](obj :S): String =
    obj.name

  def main(args: Array[String]): Unit = {
    println(getNameSub(new Parent))
    println(getNameSub(new ChildA))
    println(getNameSub(new ChildB))
    println(getNameSuper(new Parent))
    println(getNameSuper(new ChildA))
    println(getNameSuper(new ChildB))
  }
}
