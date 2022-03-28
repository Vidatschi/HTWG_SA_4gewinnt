package de.htwg.se.connect_four.model

import de.htwg.se.connect_four.model.playerComponent.Player
//import org.scalatest.{Matchers, WordSpec}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PlayerSpec extends AnyWordSpec with Matchers {
  "A Player" when {
    "new" should {
      val name ="Your Name"
      val player = Player(name)
      "have a name" in {
        player.name should be("Your Name")
      }
      "have a nice String representation" in {
        player.toString should be("Your Name")
      }
    }
  }
}
