
## Concatenation is associative
- (xs ++ ys) ++ zs = xs ++ (ys ++ zs)
- xs ++ Nil = xs
- Nil ++ xs = xs


### Natural Induction
- To show a property P(n) for all the integers n >= b
    - show that we have P(b) (base case),
    - for all integers n >= b, show the induction step:
        if one has P(n), then one also has P(n+1)
- example
    def factorial(n: Int): Int = 
        if(n==0) 1
        else n*factorial(n-1)
    
    - show that, factorial(n) >= power(2,n) for all n>=4
        - base case: factorial(4) = 24, which is >= power(2,4), 16
        - induction step
            ```
            factorial(n+1)
                >= ( n + 1 ) * factorial(n)
                > 2 * factorial(n)
                >= 2 * power(2,n) // by induction hypothesis
                = power(2, n+1)   // by definition of power
            ```
        
### Referential Transparency
Note that a proof can freely apply reduction steps 
as equalities to some part of a term

Pure functional programs don't have side effects
Therefore a term is equivalent to the term to which it reduces

This principle is called 'referential transparency'

### Structual Induction
To prove a property P(xs) for all lists xs,
- Show that P(Nil) holds base case
- Show the induction step for a list xs and some element x
    if P(xs) holds, then P(x::xs) also holds
    
- Example
```
(xs ++ ys) ++ zs = xs ++ (ys ++ zs)

concatenation of xs and ys
if xs is Nil ==> ys
if x::xs1 ==> x :: concat(xs1, ys)

1st Clause: Nil++ys = ys
2nd Clause: (x::xs1)++ys = x::(xs1++ys)

Base case: Nil
Left Side - (Nil ++ ys) ++ zs = ys ++ zs  (by 1st Clause)
Right Side - Nill ++ (ys ++ zs) = ys ++ zs (by 1st Clause)

Induction step: x :: xs
Left Side - ((x::xs) ++ ys) ++ zs
            = (x::(xs++ys)) ++ zs (by 2nd Clause)
            = x::((xs++ys)++zs) (by 2nd Clause)
Right Side - (x::xs) ++ (ys++zs)
            = x::(xs++(ys++zs)) (by 2nd Clause)
            = x::((xs++ys)++zs) (by induction hypothesis)

```