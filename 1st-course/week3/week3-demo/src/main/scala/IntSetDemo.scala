object IntSetDemo{

  abstract class IntSet {
    def incl(x: Int): IntSet
    def contains(x: Int): Boolean
    def union(other: IntSet): IntSet
  }

  // this is singleton object
  object Empty extends IntSet{
    def contains(x: Int): Boolean = false
    def incl(x: Int): IntSet = new NonEmpty(x, Empty, Empty)
    def union(other: IntSet): IntSet = other
    override def toString() = "."

  }

  class NonEmpty(elem: Int, left: IntSet, right: IntSet) extends IntSet {
    def contains(x: Int): Boolean =
      if(x < elem) left contains x
      else if(x>elem) right contains x
      else true
    def incl(x: Int): IntSet =
      // note that we keep old data by creating new NonEmpty instance
      // this is called 'persistent data structure' since we don't throw away the old one
      if (x<elem) new NonEmpty(elem, left incl x, right)
      else if (x>elem) new NonEmpty(elem, left, right incl x)
      else this
    def union(other: IntSet): IntSet =
      ((left union right) union other ) incl elem
    override def toString = "{" + left + elem + right + "}"
  }

  def main(args:Array[String]):Unit = {
    val t1 = new NonEmpty(3, Empty, Empty)
    val t2 = t1 incl 4
    println(t2) // {.3{.4.}}
  }

}
