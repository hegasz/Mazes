package view

import scalafx.scene.Scene
import scalafx.scene.paint.Color._
import scalafx.scene.layout.StackPane
import scalafx.scene.control.Button
import scalafx.scene.layout.Pane
import javafx.event.EventHandler
import javafx.event.ActionEvent

object Menu2Pane{
    val menu2Pane: Pane = new Pane()
    menu2Pane.setStyle("-fx-background-color: green;")
    val button = new Button("back to menu 1?")
    menu2Pane.getChildren().add(button)
    button.setOnAction(new EventHandler[ActionEvent]{
        override def handle(event: ActionEvent): Unit = SceneController.switchToMenu()
    })
}