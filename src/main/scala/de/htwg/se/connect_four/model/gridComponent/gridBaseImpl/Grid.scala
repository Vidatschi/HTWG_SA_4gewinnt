package de.htwg.se.connect_four.model.gridComponent.gridBaseImpl

import de.htwg.se.connect_four.model.gridComponent.GridInterface

import scala.collection.mutable.ArrayBuffer

case class Grid(cells: Matrix[Cell]) extends GridInterface {
  def this(row: Int, col: Int) = this(new Matrix[Cell](row, col, Cell(0)))
  def rows: Int = cells.row
  def cols: Int = cells.col
  def cell(row: Int, col: Int): Cell = cells.cell(row, col)
  def set(row: Int, col: Int, value: Int): Grid = copy(
    cells.replaceCell(row, col, Cell(value))
  )
  def row(row: Int): Field = Field(cells.rows(row))
  def col(col: Int): Field = Field(cells.rows.map(row => row(col)))

  def left_diagonal(row: Int, col: Int): Field = {
    val array = r1_left_diagonal(row, col)
    val mvec = ArrayBuffer[Cell]()
    r2_left_diagonal(array(0), array(1), mvec)
    Field(mvec.toVector)
  }

  def right_diagonal(row: Int, col: Int): Field = {
    val array = r1_right_diagonal(row, col)
    val mvec = ArrayBuffer[Cell]()
    r2_right_diagonal(array(0), array(1), mvec)
    Field(mvec.toVector)
  }

  def r1_left_diagonal(row: Int, col: Int): Array[Int] = {
    if (row < cells.row - 1 && col > 0) {
      r1_left_diagonal(row + 1, col - 1)
    } else {
      Array(row, col)
    }
  }

  def r2_left_diagonal(row: Int, col: Int, mvec: ArrayBuffer[Cell]): Unit = {
    if (row >= 0 && col < cells.col) {
      mvec.append(cells.cell(row, col))
      r2_left_diagonal(row - 1, col + 1, mvec)
    }
  }

  def r1_right_diagonal(row: Int, col: Int): Array[Int] = {
    if (row > 0 && col > 0) {
      r1_right_diagonal(row = row - 1, col = col - 1)
    } else {
      Array(row, col)
    }
  }

  def r2_right_diagonal(row: Int, col: Int, mvec: ArrayBuffer[Cell]): Unit = {
    if (row < cells.row && col < cells.col) {
      mvec.append(cells.cell(row, col))
      r2_right_diagonal(row = row + 1, col = col + 1, mvec)
    }
  }

  override def toString: String = cells.toString
}

object Grid {
  import play.api.libs.json._
  implicit val gridWrites: OWrites[Grid] = Json.writes[Grid]
  implicit val gridReads: Reads[Grid] = Json.reads[Grid]
}
