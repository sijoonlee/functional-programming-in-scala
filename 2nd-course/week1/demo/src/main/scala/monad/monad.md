
## What is Monad
- https://medium.com/free-code-camp/demystifying-the-monad-in-scala-cc716bb6f534
```
You can think of monads as wrappers.
We wrap objects with monads because monads provide us with the following two operations:
- identity (return in Haskell, unit in Scala)
- bind (>>= in Haskell, flatMap in Scala)
```
```
trait M[T] {
    def flatMap[U](f: T=> M[U]): M[U]
}
   
def unit[T](x: T): M[T]
```
- unit  
*Note that unit is outside of trait*
```
We want it as a standalone static method (e.g. unit(“myBook”)). 
By passing our book to unit(), we get it back from wrapped in a monad.
```
- flatMap  

in case M is  List, U is Int
```
def flatMap[Int](f: T=> List[Int]): List[Int]  

val f = (i: Int) => List(i - 1, i, i + 1)

val list = List(5, 6, 7)

println(list.flatMap(f)) // List(4, 5, 6, 5, 6, 7, 6, 7, 8)
```
You can interpret as like below
```
Initial    ---> List(5,6,7) 
Map with f ---> ( List(4,5,6), List(5,6,7), List(6,7,8)) 
Flatten    ---> List(4, 5, 6, 5, 6, 7, 6, 7, 8)
```
Generalize
```
Initial              ---> M[A]
Map with F:A => M[B] ---> M[M[B]]
Flatten              ---> M[B]
```                           

We can express Map using FlatMap
```
m map f = m flatMap (x => unit(f(x)))
m map f = m flatMap (f andThen unit)
```

### Examples of Monad
- List is a monad with unit(x) = List(x)
- Set is a monad with unit(x) = Set(x)
- Option is a monad with unit(x) = Some(x)
- Generator is a monad with unit(x) = single(x)

*flatMap is an operation on each of these types  
whereas unit is in Scala is different for each method*
  
### Monad Three Laws
- Associativity  
    (m flatMap f) flatMap g == m flatMap ( x => f(x) flatMap g)
- Left unit  
    unit(x) flatMap f == f(x)
- Right unit  
    m flatMap unit == m

### Checking the Left Unit Law
- Some(x) flatMap f == f(x)
```
Some(x) flatMap f
== Some(x) match {
    case Some(x) => f(x)
    case None => None
}
== f(x)
```
* cf) case Some(x) => Some(f(x)) if Map

### Checking the Right Unit Law
- opt flatMap Some == opt
```
opt flatMap Some
== opt match {
    case Some(x) => Some(x)
    case None => None
}
= opt
```

### Checking the Associative Law
- (opt flatMap f) flatMap g == opt flatMap (x => f(x) flatMap g)
```
opt flatMap f flatMap g
== opt match { case Some(x) => f(x) case None => None }
        match { case Some(y) => g(y) case None => None }
== opt match {
        case Some(x) => f(x) match { case Some(y) => g(y) case None => None }
        case None(x) => None match { case Some(y) => g(y) case None => None } }
== opt match {
        case Some(x) => f(x) match { case Some(y) => g(y) case None => None }
        case None(x) => None } }
== opt match {
        case Some(x) => f(x) flatMap g
        case None(x) => None } }
== opt flatMap (x => f(x) flatMap g)
```

### Significance of the Laws for For-expressions
1. Associativity says one can 'inline' nested for expressions
```
for { y <- for ( x <- m; y <- f(x) ) yield y
      z <- g(y) } yield z
== for { x <- m
         y <- f(x)
         z <- g(y) } yield z
```

2. Right unit says
```
for ( x <- m ) yield x
== m
```

3. Left unit does not have an analogue for for-expression 
