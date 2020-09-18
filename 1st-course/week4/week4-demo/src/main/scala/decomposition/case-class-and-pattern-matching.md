## Case class and pattern matching

```
abstract class Expr
case class Var(name: String) extends Expr
case class Number(num: Double) extends Expr
case class UnOp(operator: String, arg: Expr) extends Expr
case class BinOp(operator: String, left: Expr, right: Expr) extends Expr
```
* cf) Class definition with empty body can be written without {}
* ex) class C {} is same as class C

- classes with such a modifier, 'case' are called 'case classes'
- it adds a factory method with the name of the class
    - you can use val v = Var("x") instead of val v = new Var("x")
- all arguments in the parameter list of a case class implicitly get a val prefix
    - e.g.) v.name --> String, op.left --> Number
- the compiler adds 'natural' implementations of methods toString, hashCode, equals to the class
- the compiler adds a copy method to the class for making modified copies with different attributes
- case class supports pattern matching

## Pattern matching

- Constructor patterns
    - A constructor pattern matches all the values of type C
    - with the same parameters
    
UnOp("-", UnOp("-", e)) => e // double negation ex) --a
BinOp("+", e , Number(0)) => e // Adding zero ex) +0

def simplifyTop(expr: Expr): Expr = expr match {
    case UnOp("-", UnOp("-", e)) => e // Double negation
    case BinOp("+", e, Number(0)) => e // Adding zero
    case BinOp(" * ", e, Number(1)) => e // Multiplying by one
    case _ => expr  // wildcard patern, matches any object
}

- Constant pattern
def describe(x: Any) = x match {
    case 5 => "five"
    case true => "truth"
    case "hello" => "hi!"
    case Nil => "the empty list"
    case _ => "something else"
}

- Variable pattern
// somethingElse is a variable that matches anything other than 0
expr match {
    case 0 => "zero"
    case somethingElse => "not zero: " + somethingElse
}

-  a simple name starting with a lowercase letter is taken to be a pattern variable
val pi = math.Pi
pi: Double = 3.141592653589793

E match {
    case pi => "strange math? Pi = " + pi
    case _ => "OK"  // unreachable code since pi matches all
}

- this one is considered as constant pattern
import math.{E, Pi}
E match {
    case Pi => "strange math? Pi = " + Pi
    case _ => "OK"
}
res11: String = OK

- Sequence patterns
expr match {
    case List(0, _, _) => println("found it")
    case _ =>
}
---> three-element list with starting zero

expr match {
case List(0, _* ) => println("found it")
case _ =>
}
---> any list with starting zero
---> _* means zero or more number of elements

- Tuple patterns

def tupleDemo(expr: Any) =
    expr match {
        case (a, b, c) => println("matched " + a + b + c)
        case _ =>
    }

tupleDemo(("a ", 3, "-tuple"))  // matched a 3-tuple

- Typed patterns

def generalSize(x: Any) = x match {
    case s: String => s.length
    case m: Map[_, _] => m.size
    case _ => -1
}
generalSize("abc") // 3
generalSize(Map(1 -> 'a', 2 -> 'b')) // 2
generalSize(math.Pi) // -1

- Type erasure
def isIntIntMap(x: Any) = x match {
    case m: Map[Int, Int] => true
    case _ => false
}
// this will make warning: the underlying of Map[Int,Int]) is unchecked
//     since it is eliminated by erasure
// Scala uses the erasure model of generics, just like Java does. 
// This means that no information about type arguments is maintained at runtime.

isIntIntMap(Map(1 -> 1)) // true
isIntIntMap(Map("abc" -> "abc")) // true

// The only exception to the erasure rule is arrays, because they are handled
// specially in Java as well as in Scala. The element type of an array is stored
// with the array value

scala> def isStringArray(x: Any) = x match {
    case a: Array[String] => "yes"
    case _ => "no"
}

val as = Array("abc")
isStringArray(as) // yes

val ai = Array(1, 2, 3)
isStringArray(ai) // no

- Variable binding
// we can add a variable to any other pattern

// A pattern with a variable binding via the @ sign
expr match {
    case UnOp("abs", e @ UnOp("abs", _)) => e
    case _ =>
}

- Pattern guarding
def simplifyAdd(e: Expr) = e match {
    case BinOp("+", x, x) => BinOp(" * ", x, Number(2))
        // error: x is already defined as value x 
    case _ => e
}

// This fails because Scala restricts patterns to be linear: a pattern variable may
only appear once in a pattern

// Below will be fine
// pattern guard is an if + arbitrary boolean expression
def simplifyAdd(e: Expr) = e match {
    case BinOp("+", x, y) if x == y =>
    BinOp(" * ", x, Number(2))
    case _ => e
}

- Pattern overlaps
 it is important that the catch-all cases come after the
more specific simplification rules.

def simplifyBad(expr: Expr): Expr = expr match {
    case UnOp(op, e) => UnOp(op, simplifyBad(e))
    case UnOp("-", UnOp("-", e)) => e
}
// warning: unreachable code case UnOp("-", UnOp("-", e)) => e

- Sealed classes
    - Whenever you write a pattern match, you need to make sure you have cov-
    ered all of the possible cases.
    - A sealed class cannot have any new subclasses added except the ones in the
    same file. 
    - This is very useful for pattern matching because it means you only
    need to worry about the subclasses you already know about
    - If you match against case classes that inherit from a sealed class, the compiler will flag missing combinations
    of patterns with a warning message.

sealed abstract class Expr
case class Var(name: String) extends Expr
case class Number(num: Double) extends Expr
case class UnOp(operator: String, arg: Expr) extends Expr
case class BinOp(operator: String, left: Expr, right: Expr) extends Expr

def describe(e: Expr): String = e match {
    case Number(_) => "a number"
    case Var(_) => "a variable"
}
// You will get a compiler warning like the following:
warning: match is not exhaustive!
missing combination UnOp
missing combination BinOp

// if you want to check just partial set of them and to avoid error/warning messages you can add an @unchecked annotation to the selector expression of the match. 
// This is done as follows:
def describe(e: Expr): String = (e: @unchecked) match {
    case Number(_) => "a number"
    case Var(_) => "a variable"
}

- Option type
val capitals = Map("France" -> "Paris", "Japan" -> "Tokyo")

capitals get "France" // Option[String] = Some(Paris)
capitals get "North Pole" // Option[String] = None

for ((country, city) <- capitals)
    println("The capital of " + country + " is " + city)
// The capital of France is Paris
// The capital of Japan is Tokyo

val results = List(Some("apple"), None, Some("orange"))
for (Some(fruit) <- results) println(fruit)
// apple
// orange

def show(x: Option[String]) = x match {
    case Some(s) => s
    case None => "?"
}
show(capitals get "Japan") // String = Tokyo
show(capitals get "France") // String = Paris
show(capitals get "North Pole") // String = ?

// Partial function
val withDefault: Option[Int] => Int = {
    case Some(x) => x
    case None => 0
}

withDefault(Some(10)) // 10
withDefault(None) // 0


val second: PartialFunction[List[Int],Int] = {
case x :: y :: _ => y
}
second.isDefinedAt(List(5,6,7)) // Boolean = true
second.isDefinedAt(List()) // Boolean = false