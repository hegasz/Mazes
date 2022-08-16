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
import scalafx.animation.FadeTransition
import javafx.util.Duration
import scalafx.scene.text.TextAlignment
import scalafx.scene.text.Text


object GamePane{    

    val innerColour: String = "5F4B8BFF"
    //val outerColour: String = "E69A8DFF"

    //val innerColour: String = "F1C0E8"
    //val outerColour: String = "FFCFD2"

    //val innerColour: String = "5F4B8BFF"
    val outerColour: String = "f8ad9d"
        

    val gamePane: Pane = new Pane()
    val borderPane: BorderPane = new BorderPane()
    
    val topBox: HBox = new HBox()
    topBox.setStyle(s"-fx-background-color: #$outerColour;")

    //topBox.minWidthProperty().bind(gamePane.widthProperty())
    //topBox.maxWidthProperty().bind(gamePane.widthProperty())


    // *start* top left buttons box
    val buttonsBox: HBox = new HBox()
    val homeButton: Button = new Button("home")
    homeButton.setOnAction(new EventHandler[ActionEvent]{
        override def handle(event: ActionEvent): Unit = SceneController.switchToMenu()
    })
    val helpButton: Button = new Button("help")
    helpButton.setOnAction(new EventHandler[ActionEvent]{
        override def handle(event: ActionEvent): Unit = showHelp()
    })
    val mazeSmallerButton: Button = new Button("-")
    mazeSmallerButton.setOnAction(new EventHandler[ActionEvent]{
        override def handle(event: ActionEvent): Unit = GameController.decreaseBoxRatio()
    })
    val mazeLargerButton: Button = new Button("+")
    mazeLargerButton.setOnAction(new EventHandler[ActionEvent]{
        override def handle(event: ActionEvent): Unit = GameController.increaseBoxRatio()
    })
    buttonsBox.getChildren().addAll(homeButton, helpButton, mazeSmallerButton, mazeLargerButton)
    // *end* top left buttons box


    // *start* help labels
    val leftButtonsHelp: Label = new Label("                                      ^^^\n               Adjust how big the maze is")
    val movementToggleHelp: Label = new Label("                        ^^^\nChange movement style")
    val mazeHelp: Label = new Label("\nUse WASD keys to move the circle from the\nbottom left to the top right of the maze")
    /** leftButtonsHelp and movementToggleHelp are placed at the top in boxes later on below
      * mazeHelp is placed in bottomBox later on below */
    mazeHelp.setTextAlignment(TextAlignment.Center)
    leftButtonsHelp.setVisible(false)
    movementToggleHelp.setVisible(false)
    mazeHelp.setVisible(false)
    // *end* help labels

    // *start* top left buttons and help message box
    val buttonsAndHelpBox: VBox = new VBox()
    buttonsAndHelpBox.getChildren().addAll(buttonsBox,leftButtonsHelp)
    topBox.getChildren().add(buttonsAndHelpBox)
    // *end* top left buttons and help message box


    // *start* movement control toggle switch
    val switchPane: BorderPane = new BorderPane()
    val switchClass = new ToggleSwitch("F4978E","FFDAB9","f08080")
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
    switchPane.setLeft(leftLabel)
    switchPane.setCenter(switch)
    switchPane.setRight(rightLabel)
    switchPane.setTop(topLabel)
    switchPane.setMinSize(150,30)
    switchPane.setMaxSize(150,30)
    BorderPane.setAlignment(leftLabel,Pos.CenterRight)
    BorderPane.setAlignment(rightLabel,Pos.CenterLeft)
    BorderPane.setAlignment(topLabel,Pos.BottomCenter)
    // *end* movement control toggle switch

    // *start* movement control toggle and help message box
    val movementToggleAndHelpBox: VBox = new VBox()
    movementToggleAndHelpBox.getChildren().addAll(switchPane, movementToggleHelp)
    // this is added to the top later on below
    // *end* movement control toggle and help message box


    // *start* speed control slider
    val speedControlBox: VBox = new VBox()
    val speedLabel: Label = new Label("movement speed:")
    val speedSlider: Slider = new Slider(1,7,4)
    speedSlider.setMajorTickUnit(1)
    speedSlider.setMinorTickCount(0)
    speedSlider.setSnapToTicks(true)
    speedSlider.setMaxWidth(100)
    speedSlider.setMinWidth(100)
    speedSlider.setRotate(180)
    speedSlider.setStyle("-fx-base: #000000; -fx-control-inner-background: #F4978E;")    
    speedSlider.valueProperty().addListener((observable, oldValue, newValue) => {
        GameController.setPacmanSpeed(newValue.intValue())
    })
    speedControlBox.getChildren().addAll(speedLabel,speedSlider)
    speedControlBox.setAlignment(Pos.TopCenter)
    // *end* speed control slider
    

    // *start* place items at top of window with padding to align nicely
    val region1: Region = new Region()
    region1.hgrow = Priority.Always
    val region2: Region = new Region()
    region2.hgrow = Priority.Always
    // add movement box to top box with separating regions to center slider
    speedControlBox.setVisible(false)
    topBox.getChildren().addAll(region1,
                                speedControlBox,
                                region2,
                                movementToggleAndHelpBox)
    // *end* place items at top of window with padding to align nicely


    val bottomBox: HBox = new HBox()
    bottomBox.setStyle(s"-fx-background-color: #$outerColour;")
    bottomBox.getChildren().add(mazeHelp)
    bottomBox.setAlignment(Pos.TopCenter)
    val leftBox: VBox = new VBox()
    leftBox.setStyle(s"-fx-background-color: #$outerColour;")
    val rightBox: VBox = new VBox()
    rightBox.setStyle(s"-fx-background-color: #$outerColour;")

    val centerBox: Pane = new Pane()
    centerBox.setStyle(s"-fx-background-color: #$innerColour;")
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

    def updateMaze(): Unit = {
        centerBox.getChildren().clear()
        centerBox.getChildren().add(GameController.getGameStack())
    }

    def showHelp(): Unit = {
        leftButtonsHelp.setVisible(true)
        movementToggleHelp.setVisible(true)
        mazeHelp.setVisible(true)
        val fade: FadeTransition = new FadeTransition()
        fade.setDuration(Duration.millis(5000));  
        fade.setFromValue(10);  
        fade.setToValue(0)
        fade.setNode(leftButtonsHelp)
        fade.play()
        val fade2: FadeTransition = new FadeTransition()
        fade2.setDuration(Duration.millis(5000));  
        fade2.setFromValue(10);  
        fade2.setToValue(0)
        fade2.setNode(movementToggleHelp)
        fade2.play()
        val fade3: FadeTransition = new FadeTransition()
        fade3.setDuration(Duration.millis(5000));  
        fade3.setFromValue(10);  
        fade3.setToValue(0)
        fade3.setNode(mazeHelp)
        fade3.play()
    }
}
