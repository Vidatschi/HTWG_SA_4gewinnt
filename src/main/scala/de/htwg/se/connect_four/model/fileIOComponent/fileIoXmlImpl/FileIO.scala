package de.htwg.se.connect_four.model.fileIOComponent.fileIoXmlImpl

import com.google.inject.{Guice, Key}
import com.google.inject.name.Names
import de.htwg.se.connect_four.ConnectFourModule
import de.htwg.se.connect_four.model.fileIOComponent.FileIOInterface
import de.htwg.se.connect_four.model.gridComponent.GridInterface
import java.io._
import scala.xml.PrettyPrinter
import scala.xml.Elem
import scala.language.postfixOps

class FileIO extends FileIOInterface {
  override def load: (GridInterface, Array[Boolean]) = {
    val file = scala.xml.XML.loadFile("grid.xml")
    val sizeAttr = (file \\ "grid" \ "@size")
    val player1 = (file \\ "grid" \ "@player1").text.toBoolean
    val player2 = (file \\ "grid" \ "@player2").text.toBoolean
    val size = sizeAttr.text.toInt
    val injector = Guice.createInjector(new ConnectFourModule)
    val mappedCells = file \\ "cell" map { col =>
      ((col \ "@col").text + (col \ "@row").text -> col.text.trim)
    } toMap
    val clist = mappedCells.toList
    val cellNodes = (file \\ "cell")
    val grid: GridInterface = size match {
      case 42 =>
        set_grid(clist, injector.getInstance(Key.get(classOf[GridInterface], Names.named("Grid Small"))), 0)
      case 110 =>
        set_grid(clist, injector.getInstance(Key.get(classOf[GridInterface], Names.named("Grid Middle"))), 0)
      case 272 =>
        set_grid(clist, injector.getInstance(Key.get(classOf[GridInterface], Names.named("Grid Huge"))), 0)
    }

    (grid, Array(player1, player2))
  }

  override def save(grid: GridInterface, players: Array[Boolean]): Unit = {
    saveString(grid, players)
  }

  def saveString(interface: GridInterface, players: Array[Boolean]): Unit = {
    val pw = new PrintWriter(new File("grid.xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(toXml(interface, players))
    pw.write(xml)
    pw.close()
  }

  def toXml(interface: GridInterface, players: Array[Boolean]): Elem = {
    <grid  size={(interface.rows * interface.cols).toString} player1={
      players.apply(0).toString
    } player2={players.apply(1).toString}>
      {
      for {
        row <- 0 until interface.rows
        col <- 0 until interface.cols
      } yield cellToXml(interface, row, col)
    }
    </grid>
  }

  def cellToXml(grid: GridInterface, row: Int, col: Int): Elem = {
    <cell row={row.toString} col={col.toString}>
      {grid.cell(row, col).value}
    </cell>
  }

  def set_grid(clist: List[(String, String)], grid: GridInterface, index: Int): GridInterface = {
    if (index < clist.size) {
      val col: Int = clist.apply(index)._1.substring(0, 1).toInt
      val row: Int = clist.apply(index)._1.substring(1, 2).toInt
      val value: Int = clist.apply(index)._2.toInt
      set_grid(clist, grid.set(row, col, value), index + 1)
    } else {
      grid
    }
  }
}
