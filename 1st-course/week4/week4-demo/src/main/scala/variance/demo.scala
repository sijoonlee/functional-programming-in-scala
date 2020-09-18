package variance

object demo {
  /* Covariance
  * if Parent :> ChildA, then List[Parent] :> List[ChildA] ??
  * -> Answer is yes
  * List is 'covariant'
  * since their subtyping relation varies with the type parameter
  * Array is covariant in Java, but caused problem
  * in Scala, Array is not covariant
  * val a: Array[ChildA] = Array(new ChildA)
  * val b: Array[Parent] = a ------------------> makes an error here
  * b(0) = new ChildB
  * val s: ChildA = a(0)
  * */

  /* Liskov Substitution Principle
  * if A <: B, everything one can do with a value of type B
  * should also be able to be done with a value of type A
  */

  /* Definition of Variance
  * C[T] is a parameterized type and A, B are types such that A <: B
  * In general, there are three possible relationships between C[A] and C[B]
  * C is covariant - C[A] <: C[B] -------- class C[+A] {}
  * C is contravariant - C[A] >: C[B] ---- class C[-A] {}
  * C is non-variant - neither of them --- class C[] {}
  */

  /* Typing Rules for Functions
  * Generally,
  * if A1 >: A2 and B1 <: B2 then,
  *     F1( A1 => B1 ) <: F2( A2 => B2 )
  * since F1 accepts more, still produce a (sub)type of B2
  *
  * Therefore,
  *     their argument type - contravariant
  *     their result type - covariant
  * trait Function1[-T, +U]{
  *     def apply(x :T) :U
  * }
  *
  * Problematic combination is
  * class Test[+T] {
  *     def update(x: T) ...
  * }
  * - the covariant type parameter T
  * - which appears in parameter position of the method update
  *
  * Scala compiler will check if there are no problematic combinations
  * - covariant type parameters can only appear in method results
  * - contravariant type parameters can only appear in method parameters
  * - non-variant type parameters can appear anywhere
  */

}
