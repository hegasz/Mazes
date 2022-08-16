package controller

import view.{MenuPane,GamePane,WonPane}
import scalafx.Includes._
import scalafx.scene.Scene
import scala.collection.mutable.HashMap
import scalafx.scene.layout.Pane


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
