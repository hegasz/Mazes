package view

import scalafx.Includes._
import scalafx.stage.Stage
import scalafx.scene.Scene
import scalafx.scene.Parent
import javafx.event.ActionEvent
import scalafx.scene.control.Button
import javafx.event.EventHandler
import scala.collection.mutable.HashMap
import scalafx.scene.layout.Pane
import scalafx.application.JFXApp3


object SceneController{

    var scene: Scene = null
    var activeScene: String = ""

    private val hashMap: HashMap[String, Pane] = new HashMap()

    hashMap += ("menu" -> MenuPane.menuPane)
    hashMap += ("game" -> GamePane.gamePane)
    hashMap += ("won" -> WonPane.wonPane)
    
    def add(name: String, pane: Pane): Unit = {
        hashMap += (name->pane)
    }

    def remove(name: String): Unit = {
        hashMap -= name
    }

    def switchToMenu(): Unit = {setPane("menu"); activeScene = "menu"}
    def switchToGame(): Unit = {setPane("game"); activeScene = "game"}
    def switchToWon(): Unit = {setPane("won"); activeScene = "won"}

    def setPane(name: String): Unit = {
        require(hashMap.contains(name), name + " is not a pane. Make sure it is added to this controller.")
        scene.setRoot(hashMap(name))
    }
}
