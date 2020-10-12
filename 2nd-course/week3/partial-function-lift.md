https://stackoverflow.com/questions/17965059/what-is-lifting-in-scala
by oxbow_lakes

## Partial Function

Remember a PartialFunction[A, B] is a function defined for some subset of the domain A (as specified by the isDefinedAt method). You can "lift" a PartialFunction[A, B] into a Function[A, Option[B]]. That is, a function defined over the whole of A but whose values are of type Option[B]

This is done by the explicit invocation of the method lift on PartialFunction.

```
scala> val pf: PartialFunction[Int, Boolean] = { case i if i > 0 => i % 2 == 0}
pf: PartialFunction[Int,Boolean] = <function1>

scala> pf.lift
res1: Int => Option[Boolean] = <function1>

scala> res1(-1)
res2: Option[Boolean] = None

scala> res1(1)
res3: Option[Boolean] = Some(false)
```

## Methods

You can "lift" a method invocation into a function. This is called eta-expansion (thanks to Ben James for this). So for example:
```
scala> def times2(i: Int) = i * 2
times2: (i: Int)Int
```
We lift a method into a function by applying the underscore
```
scala> val f = times2 _
f: Int => Int = <function1>

scala> f(4)
res0: Int = 8
```
Note the fundamental difference between methods and functions. res0 is an instance (i.e. it is a value) of the (function) type (Int => Int)

## Functors
A functor (as defined by scalaz) is some "container" (I use the term extremely loosely), F such that, if we have an F[A] and a function A => B, then we can get our hands on an F[B] (think, for example, F = List and the map method)

We can encode this property as follows:
```
trait Functor[F[_]] { 
  def map[A, B](fa: F[A])(f: A => B): F[B]
}
```
This is isomorphic to being able to "lift" the function A => B into the domain of the functor. That is:
```
def lift[F[_]: Functor, A, B](f: A => B): F[A] => F[B]
```
That is, if F is a functor, and we have a function A => B, we have a function F[A] => F[B]. You might try and implement the lift method - it's pretty trivial.

### Monad Transformers
As hcoopz says below (and I've just realized that this would have saved me from writing a ton of unnecessary code), the term "lift" also has a meaning within Monad Transformers. Recall that a monad transformers are a way of "stacking" monads on top of each other (monads do not compose).

So for example, suppose you have a function which returns an IO[Stream[A]]. This can be converted to the monad transformer StreamT[IO, A]. Now you may wish to "lift" some other value an IO[B] perhaps to that it is also a StreamT. You could either write this:
```
StreamT.fromStream(iob map (b => Stream(b)))
```
Or this:
```
iob.liftM[StreamT]
```
this begs the question: why do I want to convert an IO[B] into a StreamT[IO, B]?. The answer would be "to take advantage of composition possibilities". Let's say you have a function f: (A, B) => C
```
lazy val f: (A, B) => C = ???
val cs = 
  for {
    a <- as                //as is a StreamT[IO, A]
    b <- bs.liftM[StreamT] //bs was just an IO[B]
  }
  yield f(a, b)

cs.toStream //is a Stream[IO[C]], cs was a StreamT[IO, C]
```