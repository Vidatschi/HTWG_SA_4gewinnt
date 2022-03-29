package de.htwg.se.connect_four

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import net.codingwell.scalaguice.ScalaModule
import de.htwg.se.connect_four.controller.controllerComponent._
import de.htwg.se.connect_four.model.fileIOComponent.FileIOInterface
import de.htwg.se.connect_four.model.gridComponent.GridInterface
import de.htwg.se.connect_four.model.gridComponent.gridAdvancedImpl.Grid
import de.htwg.se.connect_four.model.fileIOComponent.fileIoXmlImpl.FileIO

class ConnectFourModule extends AbstractModule {

  val defaultRows: Int = 6
  val defaultCols: Int = 7

  override def configure(): Unit = {
    bindConstant().annotatedWith(Names.named("DefaultRows")).to(defaultRows)
    bindConstant().annotatedWith(Names.named("DefaultCols")).to(defaultCols)
    bind(classOf[GridInterface]).to(classOf[Grid])
    bind(classOf[ControllerInterface]).to(
      classOf[controllerBaseImpl.Controller]
    )
    bind(classOf[GridInterface])
      .annotatedWith(Names.named("Grid Small"))
      .toInstance(new Grid(6, 7))
    bind(classOf[GridInterface])
      .annotatedWith(Names.named("Grid Middle"))
      .toInstance(new Grid(10, 11))
    bind(classOf[GridInterface])
      .annotatedWith(Names.named("Grid Large"))
      .toInstance(new Grid(16, 17))

    bind(classOf[FileIOInterface]).to(classOf[FileIO])
  }
}
