package quickcheck

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  lazy val genHeap: Gen[H] = for {
    v <- arbitrary[A]
    m <- oneOf(const(empty), genHeap, genHeap)
  } yield insert(v, m)

  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

  // adding the minimal element, and then finding it
  property("gen1") = forAll { (h: H) =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h)) == m
  }

  // adding the minimal element, and then finding it
  property("minOfOne") = forAll { a: Int =>
    val h = insert(a, empty)
    findMin(h) == a
  }

  //  If you insert any two elements into an empty heap,
  //  finding the minimum of the resulting heap should get the smallest of the two elements back.
  property("minOfTwo") = forAll { (a: Int, b:Int) =>
    val h1 = insert(a, empty)
    val h2 = insert(b, h1)
    findMin(h2) == ( if( a>=b ) b else a )
  }

  //  If you insert an element into an empty heap,
  //  then delete the minimum, the resulting heap should be empty.
  property("deleteOneEmptyCheck") = forAll { a: Int =>
    val h1 = insert(a, empty)
    val h2 = deleteMin(h1)
    isEmpty(h2)
  }

  //  Given any heap, you should get a sorted sequence of elements
  //  when continually finding and deleting minima. (Hint: recursion and helper functions are your friends.)
  property("sortCheck") = forAll { h:H =>
    def isSorted(h:H, min:Int) : Boolean = {
      isEmpty(h) || { val newMin = findMin(h); min <= newMin && isSorted(deleteMin(h), newMin) }
    }
    isEmpty(h) || isSorted(deleteMin(h), findMin(h))
  }


  //  Finding a minimum of the melding of any two heaps should return a minimum of one or the other.
  property("minMelding") = forAll { (h1:H, h2:H) =>
    val min1 = findMin(h1)
    val min2 = findMin(h2)// if(isEmpty(h2)) 0 else findMin(h2)
    val minFromMelded = findMin(meld(h1, h2))
    minFromMelded == (if(min1 <= min2) min1 else min2)
  }

  property("Two heaps should be equal if recursivly removing min elements result in same elements until empty") = forAll { (h1: H, h2: H) =>
    def heapEqual(h1: H, h2: H): Boolean =
      if (isEmpty(h1) && isEmpty(h2)) true
      else {
        val m1 = findMin(h1)
        val m2 = findMin(h2)
        m1 == m2 && heapEqual(deleteMin(h1), deleteMin(h2))
      }
    heapEqual(meld(h1, h2),
      meld(deleteMin(h1), insert(findMin(h1), h2)))
  }

}
