package pureBooleanDemo

abstract class PureBoolean {
  // if cond then t else e
  def ifThenElse(t: => PureBoolean, e: => PureBoolean): PureBoolean

  def && (x: => PureBoolean) :PureBoolean = ifThenElse(x, pureFalse)
  def || (x: => PureBoolean) :PureBoolean = ifThenElse(pureTrue, x)
  def unary_! :PureBoolean = ifThenElse(pureFalse, pureTrue)
  def == (x: PureBoolean) :PureBoolean = ifThenElse(x, x.unary_!)
  def != (x: PureBoolean) :PureBoolean = ifThenElse(x.unary_!, x)
}
