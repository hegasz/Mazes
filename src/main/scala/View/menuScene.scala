package view

import scalafx.scene.Scene
import scalafx.scene.paint.Color._
import scalafx.scene.layout.StackPane
import scalafx.scene.control.Button
import scalafx.scene.layout.Pane
import javafx.event.EventHandler
import javafx.event.ActionEvent

object Menu{
    val menuPane: Pane = new Pane()
    menuPane.setStyle("-fx-background-color: red;")
    val button = new Button("click me pls")
    menuPane.getChildren().add(button)
    button.setOnAction(new EventHandler[ActionEvent]{
        override def handle(event: ActionEvent): Unit = SceneController.switchToMenu2()
    })
}
