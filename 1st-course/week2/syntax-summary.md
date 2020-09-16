## Extended Backus-Naur form (EBNF)
    | denotes an alternative
    [...] an (zero or one) option
    {...} a repetition of (zero or more)

## Types
- Type = SimpleType | FunctionType
- FunctionType = SimpleType '=>' Type
- SimpleType = Ident
- Types = Type {',' Type}

- A type can be
    - Int, Double
    - Byte, Short, Char, Long, Float
    - Boolean 
    - String
    - and a function type, such Int => Int, (Int, Int) => Int

## Expressions
- Expr = InfixExpr | FunctionExpr | If '(' Expr ')' Expr else Expr
- InfixExpr = PrefixExpr | InfixExpr Operator InfixExpr
- Operator = ident
- PrefixExpr = ['+' | '-' | '!' | '~' ] SimpleExpr
- SimpleExpr = ident | literal | SimpleExpr '.' ident | Block
- FunctionExpr = Bindings '=>' Expr
- Bindings = ident [ ':' SimpleType] | '(' [Binding {',' Binding|}] ')'
- Binding = ident[':' Type]
- Block = '{' {Def ';'} Expr '}'

- an Expression can be
    - An identifier such as x, isGoodEnough
    - A literal, like 0, 1.0, "abc"
    - A function application, like sqrt(x)
    - An operator application, like -x, x+y
    - A selection, like math.abs
    - A conditional expression, like if( x < 0 ) -x else x
    - A block, like {val x = math.abs(y); x*2}
    - An anonymous function, like x => x + 1

## Definitions
- Def = FunDef | ValDef
- FunDef = def ident {'(' [ Parameters ]')'} [':' Type] '=' Expr
- ValDef = val ident [':' Type] '=' Expr
- Parameter = ident ':' [ '=>' ] Type
- Parameters = Parameter { ',' Parameter }

- a Definition can be
    - A function definition, such def square(x:Int) = x*x
    - A value definition, such val y = square(2)

- a Parameter can be
    - A call-by-value parameter, like (x:Int)
    - A call-by-name parameter, like (y: => Double)