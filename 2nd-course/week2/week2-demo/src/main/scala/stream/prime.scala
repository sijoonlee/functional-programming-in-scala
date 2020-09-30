package stream

object prime {

  // Stream is deprecated, use LazyList
  object primes {
    def from(n:Int): LazyList[Int] = n #:: from(n+1)
    val nats = from(0)
    val m4s = nats map (_ * 4)
    println((m4s take 20).toList.toString())

    /* The Sieve of Eratosthenes
     it is an ancient technique to calculate prime numbers
     - start with all integers from 2, the first prime number
     - eliminate all multiples of 2
     - the first element of the resulting list is 3, a prime number
     - eliminate all multiples of 3
     - iterate forever
     - at each step, the first number in the list is a prime number
    */
    def sieve(s:LazyList[Int]): LazyList[Int] =
      s.head #:: sieve(s.tail filter (_ % s.head != 0))

    val primes = sieve(from(2))
    println(primes.take(20).toList)
  }

  def main(args: Array[String]): Unit = {
    primes
  }

}


