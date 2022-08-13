package view

import scalafx.scene.layout.BorderPane
import scalafx.scene.layout.HBox
import scalafx.scene.control.Button
import scalafx.scene.layout.VBox
import scalafx.scene.layout.Pane
import javafx.event.EventHandler
import javafx.event.ActionEvent



object GamePane{

    val gamePane: Pane = new Pane()
    val borderPane: BorderPane = new BorderPane()
    val topBox: HBox = new HBox()
    topBox.setStyle("-fx-background-color: red;")
    val homeButton: Button = new Button("home")
    homeButton.setOnAction(new EventHandler[ActionEvent]{
        override def handle(event: ActionEvent): Unit = SceneController.switchToMenu()
    })
    
    topBox.getChildren().add(homeButton)
    val bottomBox: HBox = new HBox()
    bottomBox.setStyle("-fx-background-color: green;")
    val leftBox: VBox = new VBox()
    leftBox.setStyle("-fx-background-color: blue;")
    val rightBox: VBox = new VBox()
    rightBox.setStyle("-fx-background-color: yellow;")
    val centerBox: Pane = new Pane()
    centerBox.setStyle("-fx-background-color: purple;")
    centerBox.getChildren().add(GameController.getGridState().value.canvas)

    borderPane.setTop(topBox)
    borderPane.setBottom(bottomBox)
    borderPane.setLeft(leftBox)
    borderPane.setRight(rightBox)
    borderPane.setCenter(centerBox)

    gamePane.getChildren().add(borderPane)

    // Resize grid and borders
    def reSize(width: Double, height: Double, boxSize: Double): Unit = {

        var borderHeight: Double = 0; var borderWidth: Double = 0
        
        centerBox.getChildren().clear()
        centerBox.getChildren().add(GameController.getGridState().value.canvas)

        centerBox.setMinWidth(boxSize)
        centerBox.setMinHeight(boxSize)
        centerBox.setMaxWidth(boxSize)
        centerBox.setMaxHeight(boxSize)

        borderHeight = (height-boxSize)/2
        borderWidth = (width-boxSize)/2

        topBox.setMinHeight(borderHeight)
        bottomBox.setMinHeight(borderHeight)
        leftBox.setMinWidth(borderWidth)
        rightBox.setMinWidth(borderWidth)
        topBox.setMaxHeight(borderHeight)
        bottomBox.setMaxHeight(borderHeight)
        leftBox.setMaxWidth(borderWidth)
        rightBox.setMaxWidth(borderWidth)

        borderPane.setCenter(centerBox)
        gamePane.getChildren().clear()
        gamePane.getChildren().add(borderPane)
    }
}
