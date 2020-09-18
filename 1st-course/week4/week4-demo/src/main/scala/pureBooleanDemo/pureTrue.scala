package pureBooleanDemo

object pureTrue extends PureBoolean{
  // if true then t
  override def ifThenElse(t: => PureBoolean, e: => PureBoolean): PureBoolean = t

  override def toString:String = "PureTrue"
}
