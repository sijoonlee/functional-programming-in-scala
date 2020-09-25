
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



### Resource
