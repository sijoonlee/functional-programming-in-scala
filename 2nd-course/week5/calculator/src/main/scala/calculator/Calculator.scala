package calculator

/*
* This function takes as input a map from variable name to expressions of their values.
* Since the expression is derived from the text entered by the user,
* it is a Signal. The Expr abstract data type is defined as follows:
*/
sealed abstract class Expr
final case class Literal(v: Double) extends Expr
final case class Ref(name: String) extends Expr
/*The Ref(name) case class represents a reference to another variable in the map namedExpressions.*/
final case class Plus(a: Expr, b: Expr) extends Expr
final case class Minus(a: Expr, b: Expr) extends Expr
final case class Times(a: Expr, b: Expr) extends Expr
final case class Divide(a: Expr, b: Expr) extends Expr

object Calculator extends CalculatorInterface {
  /*
   * Spreadsheet-like calculator
   * Now that you are warmed up manipulating Signals,
   * it is time to proceed with the original goal of this assignment: the spreadsheet-like calculator.
   *
   * To simplify things a bit, we use a list of named variables,
   * rather than a 2-dimensional table of cells.
   * In the Web UI, there is a fixed list of 10 variables,
   * but your code should be able to handle an arbitrary number of variables.
   *
   * Implement the function computeValues, and its helper eval, while keeping the following things in mind:
   * Refs to other variables could cause cyclic dependencies
   * (e.g., a = b + 1 and b = 2 * a. Such cyclic dependencies are considered as errors
   * (failing to detect this will cause infinite loops).
   *
   * Referencing a variable that is not in the map is an error.
   * Such errors should be handled by returning Double.NaN, the Not-a-Number value.
   * It is not asked that, when given an expression for a = b + 1,
   * you compute the resulting value signal for a in terms of the value signal for b.
   * It is OK to compute it from the expression signal for b.
   *
   */
  def computeValues(namedExpressions: Map[String, Signal[Expr]]): Map[String, Signal[Double]] = {
    for ((name, expr) <- namedExpressions) yield name -> Signal(eval(expr(), namedExpressions))
    // or
    //    namedExpressions.map({
    //      case (varName, signalExpr) => {
    //        varName -> Signal(eval(signalExpr(), namedExpressions))
    //      }
    //    })
  }

  def eval(expr: Expr, references: Map[String, Signal[Expr]]): Double = {
    expr match {
      case Literal(v) => v
      case Ref(name) => eval(getReferenceExpr(name, references), references.removed(name))
      case Plus(a, b)   => eval(a, references) + eval(b, references)
      case Minus(a, b)  => eval(a, references) - eval(b, references)
      case Times(a, b)  => eval(a, references) * eval(b, references)
      case Divide(a, b) => eval(a, references) / eval(b, references)
      case _ => Double.NaN
    }
  }

  /** Get the Expr for a referenced variables.
   *  If the variable is not known, returns a literal NaN.
   */
  private def getReferenceExpr(name: String,
      references: Map[String, Signal[Expr]]) = {
    references.get(name).fold[Expr] {
      Literal(Double.NaN)
    } { exprSignal =>
      exprSignal()
    }
  }
}
