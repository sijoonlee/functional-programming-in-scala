object solid extends App {
  type Solid;
  type Liquid;
  type Gaz;
  trait Physics {
    implicit def air: Gaz
    implicit def condense(implicit gaz: Gaz): Liquid
    implicit def freeze(implicit liquid: Liquid): Solid

    implicitly[Solid]
    // you rewrite the last line with the inferred arguments explicitly written
    implicitly[Solid](freeze(condense(air)))

  }

}
