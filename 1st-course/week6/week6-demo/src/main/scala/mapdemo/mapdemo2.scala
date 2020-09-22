package mapdemo

object mapdemo2 {


  // x^2 + 3x + 1 can be represented by Map( 0 -> 1, 1 -> 3, 2 -> 1)
  class Polynomial(val termsParam: Map[Int, Double]){
    def this(bindings: (Int, Double)*) = this(bindings.toMap)// * represents repeated params
    val terms = termsParam withDefaultValue 0.0
    // this one is assuming there's no default value for non-existing key
    def adjustWithNoDefault(term:(Int, Double)):(Int, Double) = {
      val (exp, coeff) = term
      terms get exp match {
        case Some(coeff1) => exp -> (coeff + coeff1)
        case None => exp -> coeff
      }
    }
    // we have default value, this function can be simplified
    def adjust(term:(Int, Double)):(Int, Double) = {
      val (exp, coeff) = term
      exp -> (coeff + terms(exp))
    }

    // This accompanies the process creating intermediary Map and concatenating with other
//    def + (other: Polynomial):Polynomial =
//      new Polynomial( terms ++ (other.terms map adjust) )

    def addTerm(terms:Map[Int, Double], term:(Int, Double)):Map[Int, Double] = {
      val (exp, coeff) = term
      terms + (exp -> (coeff + terms(exp)))
    }

    // This one doesn't create intermediary Map
//    def + (other: Polynomial):Polynomial =
//      new Polynomial((other.terms foldLeft terms)(addTerm))

    // Alternative form using pattern match
    def + (other: Polynomial):Polynomial =
      new Polynomial((other.terms foldLeft terms) { case (acc: Map[Int, Double], (k, v)) => acc + (k -> (v + acc(k))) })

    override def toString =
      (for((exp, coeff) <- terms.toList.sorted.reverse ) yield coeff + "x^" + exp) mkString " + "
  }

  def main(args: Array[String]): Unit = {
    val x = new Polynomial(0->1, 1->3, 2->1)//new Polynomial(Map(0->1, 1->3, 2->1))
    val y = new Polynomial(0->5, 2->2)//new Polynomial(Map(0->5, 2->2))
    println(x)
    println(y)
    println(x + y)
  }


}
