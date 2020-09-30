## the stream of all natural numbers

def from(n: Int): Streams[Int] = n #:: from(n+1)

val nats = from(0)

nats map (_ * 4) // a stream of all multiples of 4