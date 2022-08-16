package view

import scalafx.Includes._
import scalafx.scene.control.Button
import scalafx.scene.layout.BorderPane
import scalafx.scene.layout.Pane
import javafx.event.EventHandler
import javafx.event.ActionEvent
import scalafx.scene.layout.VBox
import scalafx.scene.control.Label
import scalafx.scene.text.Text
import scalafx.scene.paint.LinearGradient
import scalafx.scene.effect.DropShadow
import scalafx.scene.paint.Color._
import scalafx.scene.paint.Stops
import scalafx.scene.layout.StackPane

object WonPane{
    val wonPane: Pane = new Pane()
    wonPane.setStyle("-fx-background-color: #f8ad9d;")
    val borderPane: BorderPane = new BorderPane()
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
                stops = Stops(web("0x00FFFF"), web("0xFF00FF"))
              )
              //effect = new DropShadow {
              //  color = DarkGray
              //  radius = 15
              //  spread = 0.25
              //}
            }
    buttonPane.getChildren().add(homeButton)
    borderPane.setCenter(mazeSolvedText)
    borderPane.setBottom(buttonPane)
    wonPane.getChildren().add(borderPane)
}

