package controller

import view.{MenuPane,GamePane,WonPane}
import scalafx.Includes._
import scalafx.scene.Scene
import scala.collection.mutable.HashMap
import scalafx.scene.layout.Pane


object SceneController{

    private var scene: Scene = null
    private var activeScene: String = ""
    private val hashMap: HashMap[String, Pane] = new HashMap()

    hashMap += ("menu" -> MenuPane.menuPane)
    hashMap += ("game" -> GamePane.gamePane)
    hashMap += ("won" -> WonPane.wonPane)
    
    // set the screen this controller will operate on
    // MUST BE DONE BEFORE THIS CONTROLLER CAN BE USED
    def setScene(newScene: Scene): Unit = scene = newScene

    // return string code of what is currently showing on the scene 
    def getActiveScene: String = activeScene


    def switchToMenu(): Unit = {setPane("menu"); activeScene = "menu"}
    def switchToGame(): Unit = {setPane("game"); activeScene = "game"}
    def switchToWon(): Unit = {setPane("won"); activeScene = "won"}

    // set one of the panes as the one currently showing on the scene
    def setPane(name: String): Unit = {
        require(hashMap.contains(name), name + " is not a pane. Make sure it is added to this controller.")
        scene.setRoot(hashMap(name))
    }
}
