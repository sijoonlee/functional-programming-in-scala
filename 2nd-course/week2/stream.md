## Performance Problem

We can use Stream to avoid computing the tail of a sequence until it is needed for the evaluation result
(which might be never)

This idea is implemented in a new class, the Stream

Stream are similar to lists, but their tail is evaluated only on demand

## Defining Streams
Streams are defined from a constant Stream.empty and a constructor Stream.cons

For instance,
> val xs = Stream.cons(1, Stream.cons(2, Stream.empty))

They can also be defined like the other collections by using the object Stream as a factory
> Stream(1,2,3)

The toStream method on a collection will turn the collection into a stream
> (1 to 1000).toStream  // Stream[Int] = Stream(1,?)

## Stream Ranges
```
def streamRange(lo:Int, hi:Int): Stream[Int] = 
    if(lo >= hi) Stream.empty
    else Stream.cons(lo, streamRange(lo+1, hi))
```
- compare to List
```
def listRange(lo:Int, hi:Int): List[Int] =
    if(lo >= hi) Nil
    else lo :: listRange(lo + 1, hi)
```

## Methods on Streams
Stream supports almost all methods of List

For instance, to find the second prime number between 1000 and 10000

((1000 to 10000).toStream filter isPrime)(1)

an alternative operator #:: which produces a stream

x #:: xs == Stream.cons(x, xs)

#:: can be used in expressions as well as patterns

## Implementations of Streams

trait Stream[+A] extends Seq[A] {
    def isEmpty: Boolean
    def head: A
    def tail: Stream[A]
    ...
}

object Stream {
    def cons[T](hd: T, tl: => Stream[T]) = new Stream[T] {
        def isEmpty = false
        def head = hd
        def tail = tl
    }
    val empty = new Stream[Nothing] {
        def isEmpty = true
        def head = throw new NoSuchElementException("empty.head")
        def tail = throw new NoSuchElementException("empty.tail")
    }
    def filter(p: T => Boolean): Stream[T] =
        if(isEmpty) this
        else if( p(head) ) cons(head, tail.filter(p))
        else tail.filter(p)
}

## Exercise
Consider this modification of streamRange

def streamRange(lo: Int, hi: Int): Stream[Int] = {
    print(lo+" ")
    if(lo >= hi) Stream.empty
    else Stream.cons(lo, streamRange(lo + 1, hi))
}

streamRange(1,10).take(3).toList
// 1 2 3, the rest part remains unevaluated