## Problem

If tail is called several times, the corresponding stream will be recomputed each time
This problem can be avoided by storing the result of the first evaluation of tail
and re-using the stored result instead of recomputing tail

We call it 'lazy evaluation' 
- by-name evaluation: everything is recomputed
- strict evaluation : normal parameters and val definitions

## Lazy Evaluation in Scala

Haskell is a functional programming language that uses lazy evaluation by default

Scala uses strict evaluation by default, but allows lazy evaluation of value definitions
> lazy val x = expr

## Exercise

def expr = {
    val x = { println("x"); 1 }         // (1)
    lazy val y = { println("y"); 2 }    // (2)
    def z = { println("z"); 3 }         // (3)
    z + y + x + z + y + x               // (4)
}

// evaluation
x - x is evaluated in (1)
z - z is evaluated when it is called at (4) (first z)
y - y is evaluated when it is called at (4) (first y) - second y will use the stored
z - z is re-evaluated when it is called at (4) (second z)


## Lazy Vals and Streams

def cons[T](hd:T, tl: => Stream[T]) = new Stream[T] {
    def head = hd
    lazy val tail = tl
    ...
}

## Streams in Action
(streamRange(1000, 10000) filter isPrime) apply 1

-->
(if (1000 >= 10000) empty else cons(1000, streamRange(1000+1, 10000)))
.filter(isPrime).apply(1)

-->
cons(1000, streamRange(1000+1, 10000))
.filter(isPrime).apply(1)

--> by abbreviating cons(1000, streamRange(1000+1, 10000)) as C1
C1.filter(isPrime).apply(1)

-->
(if(C1.isEmpty) C1
else if (isPrime(C1.head)) cons(C1.head, C1.tail.filter(isPrime)))
else if C1.tail.filter(isPrime))
.apply(1)

-->
(if (isPrime(C1.head)) cons(C1.head, C1.tail.filter(isPrime)))
else if C1.tail.filter(isPrime))
.apply(1)

-->
(if (isPrime(1000)) cons(C1.head, C1.tail.filter(isPrime)))
else if C1.tail.filter(isPrime))
.apply(1)

-->
(if (false) cons(C1.head, C1.tail.filter(isPrime)))
else if C1.tail.filter(isPrime))
.apply(1)

-->
C1.tail.filter(isPrime).apply(1)

-->
streamRange(1001, 10000)
.filter(isPrime).apply(1)

--> --> --> same thing happens continously

streamRange(1009, 10000)
.filter(isPrime).apply(1)

-->
cons(1009, streamRange(1009+1, 10000))  // abbreviated to C2
.filter(isPrime).apply(1)

--> 
C2.filter(isPrime).apply(1)


Assuming apply is defined like this in Stream[T]
def apply(n: Int): T = 
    if(n==0) head
    else tail.apply(n-1)
-->
if(1==0) cons(1009, C2.tail.filter(isPrime)).head
else cons(1009, C2.tail.filter(isPrime)).tail.apply(0)

-->
cons(1009, C2.tail.filter(isPrime)).tail.apply(0)

--> by evaluation .tail
C2.tail.filter(isPrime).apply(0)

--> streamRange(1010, 10000).filter(isPrime).apply(0)

--> --> -->

--> 
streamRange(1013, 1000).filter(isPrime).apply(0)

-->
1013