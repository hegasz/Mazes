package view

import scalafx.scene.layout.BorderPane
import scalafx.scene.layout.HBox
import scalafx.scene.control.Button
import scalafx.scene.layout.VBox
import scalafx.scene.layout.Pane
import javafx.event.EventHandler
import javafx.event.ActionEvent
import javafx.scene.input.KeyEvent
import javafx.scene.input.KeyCode



object GamePane{

    val gamePane: Pane = new Pane()
    val borderPane: BorderPane = new BorderPane()
    val topBox: HBox = new HBox()
    topBox.setStyle("-fx-background-color: #755139FF;")
    val homeButton: Button = new Button("home")
    homeButton.setOnAction(new EventHandler[ActionEvent]{
        override def handle(event: ActionEvent): Unit = SceneController.switchToMenu()
    })
    val mazeSmallerButton: Button = new Button("-")
    mazeSmallerButton.setOnAction(new EventHandler[ActionEvent]{
        override def handle(event: ActionEvent): Unit = GameController.decreaseBoxRatio(0.1)
    })
    val mazeLargerButton: Button = new Button("+")
    mazeLargerButton.setOnAction(new EventHandler[ActionEvent]{
        override def handle(event: ActionEvent): Unit = GameController.increaseBoxRatio(0.1)
    })
    
    topBox.getChildren().addAll(homeButton, mazeSmallerButton, mazeLargerButton)
    val bottomBox: HBox = new HBox()
    bottomBox.setStyle("-fx-background-color: #755139FF;")
    val leftBox: VBox = new VBox()
    leftBox.setStyle("-fx-background-color: #755139FF;")
    val rightBox: VBox = new VBox()
    rightBox.setStyle("-fx-background-color: #755139FF;")
    val centerBox: Pane = new Pane()
    centerBox.setStyle("-fx-background-color: #F2EDD7FF;")
    centerBox.getChildren().add(GameController.getGridCanvas())

    borderPane.setTop(topBox)
    borderPane.setBottom(bottomBox)
    borderPane.setLeft(leftBox)
    borderPane.setRight(rightBox)
    borderPane.setCenter(centerBox)

    gamePane.getChildren().add(borderPane)

    gamePane.setOnKeyPressed(new EventHandler[KeyEvent]() {
            override def handle(event: KeyEvent) = {
                if (event.getCode() == KeyCode.A) {
                    GameController.moveLeft()
                }
                if (event.getCode() == KeyCode.D) {
                    GameController.moveRight()
                }
                if (event.getCode() == KeyCode.W) {
                    GameController.moveUp()
                }
                if (event.getCode() == KeyCode.S) {
                    GameController.moveDown()
                }
            }
        })

    // Resize grid and borders
    def reSize(width: Double, height: Double, boxSize: Double): Unit = {

        var borderHeight: Double = 0; var borderWidth: Double = 0
        
        centerBox.getChildren().clear()
        centerBox.getChildren().add(GameController.getGridCanvas())

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
