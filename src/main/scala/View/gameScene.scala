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
import scalafx.scene.control.Label
import scalafx.scene.layout.StackPane
import scalafx.geometry.Pos
import scalafx.scene.control.Slider
import scalafx.scene.layout.Region
import scalafx.scene.layout.Priority
import javafx.beans.value.ChangeListener


object GamePane{

    val gamePane: Pane = new Pane()
    val borderPane: BorderPane = new BorderPane()
    
    val topBox: HBox = new HBox()
    topBox.setStyle("-fx-background-color: #755139FF;")

    // buttons
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

    // Box for movement controls
    val movementBox: HBox = new HBox()
    val switchPane: BorderPane = new BorderPane()
    val switchClass = new ToggleSwitch()
    val switch = switchClass.switch
    switchClass.state.onChange{(_, _, newValue) =>
            if(newValue){
                GameController.setPacmanControls()
                showSpeedSlider()
            }
            else{
                GameController.setDiscreteControls()
                hideSpeedSlider()
            }
           }
    val leftLabel: Label = new Label(" discrete")
    val rightLabel: Label = new Label("pacman ")
    val topLabel: Label = new Label("movement style:")
    val speedControlBox: VBox = new VBox()
    val speedLabel: Label = new Label("movement speed:")
    val speedSlider: Slider = new Slider(1,7,4)
    speedSlider.setMajorTickUnit(1)
    speedSlider.setMinorTickCount(0)
    speedSlider.setSnapToTicks(true)
    speedSlider.setMaxWidth(100)
    speedSlider.setMinWidth(100)
    speedSlider.setRotate(180)
    speedSlider.setStyle("-fx-base: #000000; -fx-control-inner-background: #f08080;")    
    speedSlider.valueProperty().addListener((observable, oldValue, newValue) => {
        GameController.setPacmanSpeed(newValue.intValue())
    })
    speedControlBox.getChildren().addAll(speedLabel,speedSlider)
    speedControlBox.setAlignment(Pos.TopCenter)
    switchPane.setLeft(leftLabel)
    switchPane.setCenter(switch)
    switchPane.setRight(rightLabel)
    switchPane.setTop(topLabel)
    switchPane.setMinSize(150,30)
    switchPane.setMaxSize(150,30)
    BorderPane.setAlignment(leftLabel,Pos.CenterRight)
    BorderPane.setAlignment(rightLabel,Pos.CenterLeft)
    BorderPane.setAlignment(topLabel,Pos.BottomCenter)
    val region1: Region = new Region()
    region1.hgrow = Priority.Always
    val region2: Region = new Region()
    region2.hgrow = Priority.Always
    val region3: Region = new Region()
    region3.hgrow = Priority.Always
    val region4: Region = new Region()
    region4.hgrow = Priority.Always
    val region5: Region = new Region()
    region5.hgrow = Priority.Always
    val region6: Region = new Region()
    region6.hgrow = Priority.Always
    val region7: Region = new Region()
    region7.hgrow = Priority.Always
    val region8: Region = new Region()
    region8.hgrow = Priority.Always
    val region9: Region = new Region()
    region9.hgrow = Priority.Always
    // add movement box to top box with separating regions to center slider
    speedControlBox.setVisible(false)
    topBox.getChildren().addAll(region1, region3, region4, region6, region8,
                                speedControlBox,
                                region2, region5, region7, region9,
                                switchPane)

    val bottomBox: HBox = new HBox()
    bottomBox.setStyle("-fx-background-color: #755139FF;")
    val leftBox: VBox = new VBox()
    leftBox.setStyle("-fx-background-color: #755139FF;")
    val rightBox: VBox = new VBox()
    rightBox.setStyle("-fx-background-color: #755139FF;")

    val centerBox: Pane = new Pane()
    centerBox.setStyle("-fx-background-color: #F2EDD7FF;")
    centerBox.getChildren().add(GameController.getGameStack())

    borderPane.setTop(topBox)
    borderPane.setBottom(bottomBox)
    borderPane.setLeft(leftBox)
    borderPane.setRight(rightBox)
    borderPane.setCenter(centerBox)

    gamePane.getChildren().add(borderPane)

    // respond to movement keys
    gamePane.setOnKeyPressed(new EventHandler[KeyEvent]() {
            override def handle(event: KeyEvent) = {
                if (event.getCode() == KeyCode.A) {
                    GameController.handleLeft()
                }
                if (event.getCode() == KeyCode.D) {
                    GameController.handleRight()
                }
                if (event.getCode() == KeyCode.W) {
                    GameController.handleUp()
                }
                if (event.getCode() == KeyCode.S) {
                    GameController.handleDown()
                }
            }
        })

    // Resize grid and borders
    def reSize(width: Double, height: Double, boxSize: Double): Unit = {

        var borderHeight: Double = 0; var borderWidth: Double = 0
        
        centerBox.getChildren().clear()
        centerBox.getChildren().add(GameController.getGameStack())

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

    def showSpeedSlider(): Unit = speedControlBox.setVisible(true)
    def hideSpeedSlider(): Unit = speedControlBox.setVisible(false)
}
