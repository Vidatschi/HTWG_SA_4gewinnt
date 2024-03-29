package de.htwg.se.connect_four.controller.controllerComponent.controllerBaseImpl

import com.google.inject.name.Names
import com.google.inject.{Guice, Inject, Key}
import de.htwg.se.connect_four.ConnectFourModule
import de.htwg.se.connect_four.controller.controllerComponent.GameStatus.GameStatus
import de.htwg.se.connect_four.model.gridComponent.GridInterface
import de.htwg.se.connect_four.model.gridComponent.gridBaseImpl.Cell
import de.htwg.se.connect_four.util.UndoManager
import de.htwg.se.connect_four.controller.controllerComponent.{
  CellChanged,
  ControllerInterface,
  GameStatus,
  GridSizeChanged,
  WinEvent
}
import de.htwg.se.connect_four.model.fileIOComponent.FileIOInterface
import com.google.inject.Injector

class Controller @Inject() (var grid: GridInterface)
    extends ControllerInterface {

  var playerList: Array[Boolean] = Array(true, false)
  var gameStatus: Gamestate = Gamestate(StatelikeIDLE(GameStatus.IDLE))
  private val undoManager = new UndoManager
  val injector: Injector = Guice.createInjector(new ConnectFourModule)

  val fileIo: FileIOInterface = injector.getInstance(classOf[FileIOInterface])

  def save: Unit = {
    fileIo.save(grid, playerList)
    publish(new CellChanged)
  }

  def load: Unit = {
    val data = fileIo.load
    grid = data._1
    playerList = data._2
    publish(new CellChanged)
  }

  def createEmptyGrid(s: String): Unit = {
    s match {
      case "Grid Small" => {
        grid = injector.getInstance(
          Key.get(classOf[GridInterface], Names.named("Grid Small"))
        )
      }
      case "Grid Middle" => {
        grid = injector.getInstance(
          Key.get(classOf[GridInterface], Names.named("Grid Middle"))
        )
      }
      case "Grid Huge" => {
        grid = injector.getInstance(
          Key.get(classOf[GridInterface], Names.named("Grid Large"))
        )
      }
    }
    resetPlayerList()
    gameStatus = Gamestate(StatelikeIDLE(GameStatus.IDLE))
    publish(new GridSizeChanged(s))
  }

  def setValueToBottom(column: Int): Unit = {
    val value = if (playerList(0)) {
      1
    } else {
      2
    }
    for (i <- grid.cells.row - 1 to 0 by -1) {
      if (grid.col(column).cell(i).equals(Cell(0))) {
        undoManager.doStep(new SetCommand(i, column, value, this))
        if (this.checkWinner(i, column)) {
          gameStatus.changeState()
          publish(new WinEvent)
          return
        } else {
          this.changeTurn()
          publish(new CellChanged)
          return
        }
      }
    }
  }

  def checkWinner(row: Int, col: Int): Boolean = {
    if (check4number(grid.col(col).cells)) {
      true
    } else if (check4number(grid.row(row).cells)) {
      true
    } else if (check4number(grid.left_diagonal(row, col).cells)) {
      true
    } else if (check4number(grid.right_diagonal(row, col).cells)) {
      true
    } else {
      false
    }
  }

  private def check4number(vector: Vector[Cell]): Boolean = {
    var counter = 0
    for (cell <- vector) {
      if (cell.equals(Cell(currentPlayer()))) {
        counter = counter + 1
        if (counter == 4) {
          return true
        }
      } else {
        counter = 0
      }
    }
    false
  }

  def getTurn(playerNumber: Int): Boolean = {
    playerList(playerNumber)
  }

  def changeTurn(): Unit = {
    playerList(0) = !playerList(0)
    playerList(1) = !playerList(1)
  }

  def currentPlayer(): Int = {
    if (playerList(0)) {
      return 1
    }
    2
  }

  def resetPlayerList(): Unit = {
    playerList = Array(true, false)
  }

  def gridToString: String = grid.toString

  def undo: Unit = {
    undoManager.undoStep
    publish(new CellChanged)
  }

  def redo: Unit = {
    undoManager.redoStep
    publish(new CellChanged)
  }

  override def getGameStatus(): GameStatus = gameStatus.mystate.gameStatus

  override def getGridRow: Int = grid.rows

  override def getGridCol: Int = grid.cols
}
