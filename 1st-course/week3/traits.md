## Traits

- Both in Java and Scala, a class can only have one superclass
- What if a class wants to conform several supertypes
- Trait comes into play here
- Classes, objects and traits can inherit from at most one class but arbitrary many traits
- Trait is declared like an abstract class
- Traits resemble interfaces in Java, but are more powerful becuase they can contains fields and concrete methods
- On the other hand, traits can not have (value) parameters, only classes can

trait Planar {
    def height: Int
    def width: Int
    def surface = height * width
}

class Square extends Shape with Planar with Movable ...


