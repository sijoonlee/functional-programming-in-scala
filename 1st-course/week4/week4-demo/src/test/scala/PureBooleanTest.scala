import pureBooleanDemo.PureBoolean
import pureBooleanDemo.pureTrue
import pureBooleanDemo.pureFalse

object PureBooleanTest extends App{
  println( pureTrue != pureFalse )
  println( pureTrue == pureFalse )
  println( !pureTrue )
  println( pureTrue && pureFalse)
  println( pureTrue || pureFalse)
}

