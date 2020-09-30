### To prove a property P(t) for all trees t of a certain type,

- show that P(1) holds for all leaves l of a tree
- for each type of internal node t with subtrees s1,...,sn
  show that P(s1) ^ ... ^ P(sn) implies P(t)


### Recall our definition of IntSet with the operations 'contains' and 'incl'

abstract class IntSet {
    def incl(x: Int): IntSet
    def contains(x: Int): Boolean
}

abstract class Empty extends IntSet {
    def contains(x: Int): Boolean = false
    def incl(x: Int): IntSet = NonEmpty(x, Empty, Empty)
}

case class NonEmpty(elem:Int, left:IntSet, right:IntSet) extends IntSet {
    def contains(x: Int):Boolean = 
        if(x < elem) left contains x
        else if (x > elem) right contains x
        else true
    def incl(x: Int): IntSet = 
        if(x < elem) NonEmpty(elem, left incl x, right)
        else if(x > elem) NonEmpty(elem, left, right incl x)
        else this
}

### The Laws of IntSet

We have the following three laws:

- Empty contains x        = false
- (s incl x) contains x   = true
- (s incl x) contains y   = s contains y      if x!= y

#### Proposition 1 - Empty contains x = false
- according to the definition of contains in Empty

#### Proposition 2 - (s incl x) contains x = true
- Proof by structural induction on s

Base case: Empty
(Empty incl x) contains x
= NonEmpty(x, Empty, Empty) contains x  // by definition of Empty.incl
= true                                  // by definition of NonEmpty.contains

Induction Step: NonEmpty(x, l, r)
(NonEmpty(x, l, r) incl x) contains x   // by definition of NonEmpty.incl
= NonEmpty(x, l, r) contains x          // by definition of NonEmpty.contains
= true

Induction Step: NonEmpty(y, l, r) where y < x
(NonEmpty(y, l, r) incl x) contains x
= NonEmpty(y, l, r incl x) contains x   // by definition of NonEmpty.incl
= (r incl x) contains x                 // by definition of NonEmpty.contains
= true                                  // by the induction hypothesis

Induction Step: NonEmpty(y, l, r) where y > x is analogous

#### Proposition 3 - (s incl x) contains y = s contains y if x!= y
- Proof by structural induction on s. Assume that y < x (the dual case x < y is analogous)

Base case: Empty
(Empty incl y) contains x
= NonEmpty(y, Empty, Empty) contains x  // by definition of Empty.incl
= Empty contains x                      // by definition of NonEmpty.contains
= false                                 // by Proposition 1

For the inductive step, we need to consider a tree NonEmpty(z, l, r)
- We distinguish five cases
1. z = x
2. z = y
3. z < y < x
4. y < z < x
5. y < x < z

Induction Step: 1) z = x, 2) z = y, NonEmpty(x, l, r)
(NonEmpty(x, l, r) incl y) contains x
= NonEmpty(x, l incl y, r) contains x   // by definition of NonEmpty.incl
= true                                  // by definition of NonEmpty.contains
= NonEmpty(x, l, r) contains x          // by definition of NonEmpty.contains

Induction Step: NonEmpty(y, l, r)
(NonEmpty(y, l, r) incl y) contains x
= NonEmpty(y, l, r) contains x          // by definition of NonEmpty.incl

Induction Step: NonEmpty(z, l, r) where z < y < x
(NonEmpty(z, l, r) incl y) contain x
= NonEmpty(z, l, r incl y) contains x   // by definition of NonEmpty.incl
= (r incl y) contains x                 // by definition of NonEmpty.contains
= r contains x                          // by the induction hypothesis
= NonEmpty(z, l, r) contains x          // by definition on NonEmpty.contains

Induction Step: NonEmpty(z, l, r) where y < z < x
(NonEmpty(z, l, r) incl y) contains x   
= NonEmpty(z, l incl y, r) contains x   // by definition of NonEmpty.incl
= r contains x                          // by definition of NonEmpty.contains
= NonEmpty(z, l, r) contains x          // by definition of NonEmpty.contains

Induction Step: NonEmpty(z, l, r) where y < x < z
(NonEmpty(z, l, r) incl y) contains x
= NonEmpty(z, l incl y, r) contains x   // by definition of NonEmpty.incl
= (l incl y) contains x                 // by definition of NonEmpty.contains
= l contains x                          // by the induction hypothesis
= NonEmpty(z, l, r) contains x          // by definition of NonEmpty.contains


## Proposition 4
Suppose we add a function 'union' to IntSet

abstract class IntSet {
    def union(other: IntSet): IntSet
}
object Empty extends IntSet{
    def union(other: IntSet): IntSet = other
}
class NonEmpty(x: Int, l: IntSet, r: IntSet) extends IntSet {
    def union(other: IntSet): IntSet = (l union (r union (other))) incl x
}

* Exercise
- Show (xs union ys) contains x = xs contains x || ys contains x