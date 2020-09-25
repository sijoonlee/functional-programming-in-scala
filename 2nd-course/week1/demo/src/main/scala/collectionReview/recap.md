## Iterable
- Iterable
    - Seq
        - Indexed Seq
            - Vector
            - (Array)
            - (String)
        - Linear Seq
            - List
    - Set
    - Map
    
## Methods 
- Shared methods by all collections
    - map
    - flatMap
    - filter
    - foldLeft
    - foldRight

## Implementations    
*Actual implementation is different*

### Map
```
abstract class List[+T] {
    def map[U](f:T => U):List[U] = this match {
        case x::xs => f(x) :: xs.map(f) // prepend
        case Nil => Nil
    } 
}
```

### flatMap
```
abstract class List[+T] {
    def flatMap[U](f:T => List(U)): List[U] = this match {
        case x :: xs => f(x) ++ xs.flatMap(f) // concat
        case Nil => Nil
    }
}
```

### filter
```
abstract class List[+T] {
    def filter(p:T => Boolean): List[T] = this match {
        case x :: xs => 
            if( p(x) ) x :: xs.filter(p) else xs.filter(p)
        case Nil => Nil
    }
}
```
