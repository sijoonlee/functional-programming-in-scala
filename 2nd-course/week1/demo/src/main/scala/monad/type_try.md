### Try

Try resembles Option
Instead of Some/None, Success/Failure

abstract class Try[+T]
case class Success[T](x: T) extends Try[T]
case class Failure(ex: Exception) extends Try[Nothing]

Try is used to pass results of computations 
that can fail with an exception between threads and computers

### Creating a Try
- Try(expr)
- gives Success(someValue) or Failure(someException)
- implementation
```
object Try {
    def apply[T](expr: => T): Try[T] = 
        try Succss(expr)
        catch {
            case NonFatal(ex) => Failure(ex)
        }
}
```

### Composing Try
- Like Option, Try-valued computations can be composed in for-expressions
```
for {
    x <- computeX
    y <- computeY
} yield f(x, y)
``` 
- if computeX and computeY succeed with results Success(x) and Success(y)  
this will return Success(f(x,y))
- if either computation fails with an exception ex,  
this will return Failure(ex)  

### Definition of flatMap and map on Try
```
abstract class Try[T] {
    def flatMap[U](f: T=> Try[U]): Try[U] = this match {
        case Success(x) => try f(x) catch { case NonFatal(ex) => Failure(ex) }
        case fail: Failure => fail
    }
    def map[U](f: T=> U): Try[U] = this match {
        case Success(x) => Try(f(x))
    }
}
```
- for a Try value t,
```
t map f == t flatMap (x => Try(f(x)))
        == t flatMap (f andThen Try)
```

### Try is not a Monad
- Left Unit Rule fails  
Try(expr) flatMap f != f(expr)
    - the left-hand side will never raise a non-fatal exception
    - the right-hand side will raise an exception by expr or f
- Bullet-proof principle  
An expression composed from 'Try', 'map', 'flatMap' will never throw a non-fatal exception

 