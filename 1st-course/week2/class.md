## class

class Rational(x:Int, y:Int) {
    require( y > 0, "denominator should be positive")
    def numer = x
    def denom = y

    def this(x:Int) = this(x, 1) // secondary constructor

    def add(other: Rational) = 
        assert(other.denom > 0)
        new Rational(numer*other.denom + other.numer*denom, denom*other.denom)

    def < (other: Rational) =
        number * that.denom < that.numer * denom 
    // you can use new Rational(1, 2) > new Rational(2,3)

    def unary_- : Rational = new Rational(-numer, denom)
    // val that = new Rational(1,2)
    // you can use -that

    override def toString = numer + "/" + denom
}

This definition introduces two entities:
    - a type, Rational
    - a constructor, Rational

Scala keeps them in different namespaces, no confilict

## Constructor
- Primary: takes all arguments
- Secondary

## object
- The elements of a class type
- created by prefixing an application of the constructor of the class with operator new
    - new Rational(1,2)
