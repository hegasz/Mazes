package view

import controller.SceneController
import scalafx.Includes._
import scalafx.scene.control.Button
import scalafx.scene.layout.BorderPane
import scalafx.scene.layout.Pane
import javafx.event.EventHandler
import javafx.event.ActionEvent
import scalafx.scene.text.Text
import scalafx.scene.paint.LinearGradient
import scalafx.scene.paint.Color
import scalafx.scene.layout.StackPane
import scalafx.scene.paint.Stops

object WonPane{
    val wonPane: Pane = new Pane()
    wonPane.setStyle("-fx-background-color: #f8ad9d;")
    val borderPane: BorderPane = new BorderPane()
    // keep everything centered
    borderPane.layoutXProperty().bind(wonPane.widthProperty().subtract(borderPane.widthProperty()).divide(2));
    borderPane.layoutYProperty().bind(wonPane.heightProperty().subtract(borderPane.heightProperty()).divide(2));
    val buttonPane: StackPane = new StackPane()
    val homeButton: Button = new Button("home")
    homeButton.setOnAction(new EventHandler[ActionEvent]{
        override def handle(event: ActionEvent): Unit = SceneController.switchToMenu()
    })
    val mazeSolvedText: Text = new Text {
              text = "Maze Solved!"
              style = "-fx-font: bold 20pt monospaced"
              fill = new LinearGradient(
                startX = 0,
                startY = 0,
                endX = 1,
                endY = 0,
                stops = Stops(Color.valueOf("0x00FFFF"), Color.valueOf("0xFF00FF"))
              )
            }
    buttonPane.getChildren().add(homeButton)
    borderPane.setCenter(mazeSolvedText)
    borderPane.setBottom(buttonPane)
    wonPane.getChildren().add(borderPane)
}

