package de.htwg.se.connect_four.model
import de.htwg.se.connect_four.model.gridComponent.gridBaseImpl.{Cell, Field}
//import org.scalatest.{Matchers, WordSpec}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FieldSpec extends AnyWordSpec with Matchers {
  "a field is a row or a column or a diagonal of a grid" when {
    val field = Field(Vector(Cell(1), Cell(2),Cell(3),Cell(5)))
    "cell function is to test" in {
      field.cell(0) should be(Cell(1))
      field.cell(3) should be(Cell(5))
    }
    "getCells function is to test" in {
      field.cells should be(Vector(Cell(1), Cell(2),Cell(3),Cell(5)))
    }
  }
}
