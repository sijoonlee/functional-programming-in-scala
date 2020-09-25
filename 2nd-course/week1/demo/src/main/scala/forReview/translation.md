https://www.scala-lang.org/files/archive/spec/2.12/06-expressions.html#for-comprehensions-and-for-loops


## For Expression
- simplify this
```
(1 until n) flatMap (i => 
    (1 until i) filter (j => isPrime(i+j)) map
        (j => (i,j)))
```
- to 
```
for {
    i <- 1 until n
    j <- 1 until i
    if isPrime(i+j)
} yield (i, j)
```
- actually for expression will be translated below
``` 
(1 until n)
  .flatMap {
     case i => (1 until i)
       .withFilter { j => isPrime(i+j) }
       .map { case j => (i, j) } }
```
## Translation of For

### ex1
- Simple for expression
```
for(x <- e1) yield e2
```
- is translated to
```
e1.map(x=>e2)
```

### ex2
```
for(x <- e1 if f; s) yield e2

for(x <- e1.withFilter(x=>f); s) yield e2

...
```

### ex3
```
for (x <- e1; y <- e2; s) yield e3

e1.flatMap(x => for(y<-e2; s) yield e3)
```

## For expression and Pattern Matching
- The left-hand side of a generator may also be a pattern
```
val data: List[JSON] =
for {
    JObj(bindings) <- data
    JSeq(phones) <- bindings("phoneNumbers")
    JObj(phone) <- phones
    JStr(digits) = phone("number")
    if digits startWith "212
} yield (bindings("firstName"), bindings("lastName"))
```
- Translation of Pattern Matching in for
if pat is a pattern with a single variable x, we translate
```
pat <- expr
```
to
```
x <- expr withFilter {
        case pat => true
        case _ => false
    } map {
        case pat => x
    }
```
- Example
```
for {
    x <- 2 to N
    y <- 2 to x
    if (x % y == 0)
} yield (x,y)

(2 to N) flatMap ( x =>
    (2 to x) withFilter ( y =>
        x % y == 0 ) map ( y => (x,y) ))

```

- Translation of For is not limited to list/seq/collection
    - if you define map, flatMap, withFilter for your custom type
    - you can use for with your own type