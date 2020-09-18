## How to make a way to access objects in an extensible class hierarchy 
- Classification and access methods
    quadratic explosion of members (e.g. isSum, isProduct, ...)
- Type test and casts
    unsafe, low-level
- Object-oriented decomposition
    need to touch all classes in order to add a new method
    not good for non-local simplification
- Functional Decomposition with Pattern Matching
    - the reason we need to have test function like 'isSum' ? 
        - we want to know
            - which subclass
            - argument of the constructor
        - we can use 'case class'
    ```
    trait Expr
    case class Number(n :Int) extends Expr
    case class Sum(e1 :Expr, e2 :Expr) extends Expr 
    ```
  - patterns are constructed from
    - constructors
    - variables
        - should start with lower case
        - a variable name can appear only once e.g. Sum(x,x) is not legal
    - wildcard patterns _,
    - constants e.g. 1, true
        - name of constants begin with a capital letters
        - except reserved words such as null, true, false