package pureBooleanDemo

object pureFalse extends PureBoolean{
  // if false then e
  override def ifThenElse(t: => PureBoolean, e: => PureBoolean): PureBoolean = e

  override def toString:String = "PureFalse"
}
