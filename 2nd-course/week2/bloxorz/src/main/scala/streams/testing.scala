package streams

object testing extends StringParserTerrain {


  private lazy val vector: Vector[Vector[Char]] =
    Vector(level.split("\r?\n").map(str => Vector(str: _*)).toIndexedSeq: _*)

  def main(args: Array[String]): Unit = {

    println(findChar('T', vector))
    println(terrain(Pos(0,0)))
    println(terrainFunction(vector)(Pos(2,0)))
  }

  /**
   * A ASCII representation of the terrain. This field should remain
   * abstract here.
   */
  override val level: String = """------
                                 |--ST--
                                 |--oo--
                                 |--oo--
                                 |------""".stripMargin
}