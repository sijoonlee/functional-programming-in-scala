package week1

import scala.annotation.tailrec

object tailRecursion {

  // Euclid's greatest common divisor
  @tailrec // this is the annotation for tail-recursive, if not tail-recursive, error is issued
  def gcd(a:Int, b:Int): Int =
    if (b==0) a else gcd(b, a%b)

  // this one is not tail-recursive
  def factorial(n:Int): Int =
    if(n == 0) 1 else n * factorial(n-1)

  def factorialTailRec(n:Int): Int = {
    @tailrec
    def loop(acc:Int, n:Int):Int =
      if(n==0) acc
      else loop(acc*n, n-1)
    loop(1,n)
  }


  def main(args:Array[String]):Unit = {
    println(gcd(10,15))
    println(factorial(3))
    println(factorialTailRec(3))

  }
}

/*
If a function call itself as its last action,
the function's stack frame can be reused.
This is called 'Tail recursion'
=> Tail recursive functions are iterative processes

In general, if the last action of a function consists of a calling a function
one stack frame would be sufficient for both functions.

Such calls are called tail-calls
 */
