package view

import controller.{MenuController, SceneController, GameController}
import scalafx.Includes._
import scalafx.scene.control.Button
import scalafx.scene.layout.Pane
import javafx.event.EventHandler
import javafx.event.ActionEvent
import javafx.scene.control.TextField
import scalafx.scene.layout.HBox
import scalafx.scene.layout.VBox
import scalafx.geometry.Pos
import scalafx.scene.control.ChoiceBox
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.TextFormatter
import javafx.util.converter.IntegerStringConverter
import scalafx.scene.control.Label
import scalafx.animation.FadeTransition
import javafx.util.Duration
import javafx.scene.layout.BorderPane
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.Slider
import scalafx.geometry.Orientation
import scalafx.scene.Group
import scalafx.scene.layout.GridPane
import scalafx.scene.text.TextAlignment


object MenuPane{
    val menuPane: Pane = new Pane()
    menuPane.setStyle("-fx-background-color: #f8ad9d;")

    val borderPane: BorderPane = new BorderPane()
    // keep borderPane centered
    borderPane.layoutXProperty().bind(menuPane.widthProperty().subtract(borderPane.widthProperty()).divide(2))
    borderPane.layoutYProperty().bind(menuPane.heightProperty().subtract(borderPane.heightProperty()).divide(2))

    
    // *start* pane for mini grid and sliders
    val canvasPane: Pane = new Pane()
    val canvasPaneSize: Double = MenuController.getBoxSize()*1.3 // more than 1.3*boxSize to avoid weird clipping
    val canvasPaneOffset: Double = (canvasPaneSize-MenuController.getBoxSize())/2
    canvasPane.setMaxWidth(canvasPaneSize)
    canvasPane.setMinWidth(canvasPaneSize)
    canvasPane.setMaxHeight(canvasPaneSize)
    canvasPane.setMinHeight(canvasPaneSize)
    var gridCanvas: Canvas = MenuController.getMiniGridCanvas()
    gridCanvas.relocate(canvasPaneOffset,canvasPaneOffset)
    // sliders
    val numColsSlider: Slider = new Slider(1,20,10)
    val numRowsSlider: Slider = new Slider(1,20,10)
    numColsSlider.setMajorTickUnit(1)
    numRowsSlider.setMajorTickUnit(1)
    numColsSlider.setMinorTickCount(0)
    numRowsSlider.setMinorTickCount(0)
    numColsSlider.setSnapToTicks(true)
    numRowsSlider.setSnapToTicks(true)
    numRowsSlider.setOrientation(Orientation.Vertical)
    val sliderLength: Double = MenuController.getBoxSize()
    numColsSlider.setMaxWidth(sliderLength)
    numRowsSlider.setMaxHeight(sliderLength)
    numColsSlider.setMinWidth(sliderLength)
    numRowsSlider.setMinHeight(sliderLength)
    numColsSlider.relocate(canvasPaneOffset,MenuController.getBoxSize()+canvasPaneOffset)
    numRowsSlider.relocate(MenuController.getBoxSize()+canvasPaneOffset,canvasPaneOffset)
    numColsSlider.setStyle("-fx-base: #000000; -fx-control-inner-background: #f08080;")
    numRowsSlider.setStyle("-fx-base: #000000; -fx-control-inner-background: #f08080;")
    numColsSlider.valueProperty().addListener((observable, oldValue, newValue) => {
        redrawMiniGrid(newValue.asInstanceOf[Double].toInt, numRowsSlider.getValue().asInstanceOf[Double].toInt)
    })
    numRowsSlider.valueProperty().addListener((observable, oldValue, newValue) => {
        redrawMiniGrid(numColsSlider.getValue().asInstanceOf[Double].toInt, newValue.asInstanceOf[Double].toInt)
    })
    canvasPane.getChildren().addAll(gridCanvas,numColsSlider,numRowsSlider)
    // *end* pane for mini grid and sliders


    // *start* more sizes box
    val moreSizesBox: VBox = new VBox()
    moreSizesBox.setMaxWidth(canvasPaneSize)
    moreSizesBox.setMinWidth(canvasPaneSize)
    moreSizesBox.setMaxHeight(canvasPaneSize)
    moreSizesBox.setMinHeight(canvasPaneSize)
    val sizesPane: GridPane = new GridPane()
    val widthField: TextField = new TextField()
    val widthLabel: Label = new Label("width: ")
    val heightField: TextField = new TextField()
    val heightLabel: Label = new Label("height: ")
    widthField.setTextFormatter(new TextFormatter(new IntegerStringConverter()))
    heightField.setTextFormatter(new TextFormatter(new IntegerStringConverter()))
    widthField.setText("10")
    heightField.setText("10")
    sizesPane.add(widthLabel, 0, 0)
    sizesPane.add(widthField, 1, 0)
    sizesPane.add(heightLabel, 0, 1)
    sizesPane.add(heightField, 1, 1)
    val dimensionsTooLargeLabel: Label = new Label("values less than 500 work best")
    dimensionsTooLargeLabel.setVisible(false)
    moreSizesBox.getChildren().addAll(sizesPane, dimensionsTooLargeLabel)
    moreSizesBox.setAlignment(Pos.Center)
     // *end* more sizes box


    // *start* box for algorithm choice and go button
    val box: HBox = new HBox()
    box.setAlignment(Pos.Center)
    // algorithms choice box
    val algorithms = ObservableBuffer(
        "Randomised Recursive Backtracking (longer corridors)",
        "Randomised Kruskal's MST (shorter corridors)",
        "Random Binary Tree (strong diagonal bias)"
    )
    val mazeAlgorithmChoice = new ChoiceBox(algorithms)
    mazeAlgorithmChoice.setValue("Randomised Recursive Backtracking (longer corridors)")
    // go button
    val goButton = new Button("GO")
    goButton.setOnAction(new EventHandler[ActionEvent]{
        override def handle(event: ActionEvent): Unit = go()
    })
    box.getChildren().addAll(mazeAlgorithmChoice, goButton)
    // *end* box for algorithm choice and go button

    // *start* help button
    val helpButton: Button = new Button("help")
    helpButton.setOnAction(new EventHandler[ActionEvent]{
        override def handle(event: ActionEvent): Unit = showHelp()
    })
    helpButton.layoutXProperty().bind(menuPane.widthProperty().subtract(helpButton.widthProperty()))
    menuPane.getChildren().add(helpButton)
    // *end* help button

    // *start* more sizes button
    val moreSizesButton = new Button("bigger mazes?")
    var miniGridShowing: Boolean = true
    moreSizesButton.setOnAction(new EventHandler[ActionEvent]{
        override def handle(event: ActionEvent): Unit = {
            if(miniGridShowing){
                moreSizesView()
                moreSizesButton.setText("maybe not")
                miniGridShowing = false
            }
            else{
                miniGridView()
                moreSizesButton.setText("bigger mazes?")
                miniGridShowing = true
            }
        }
    })
    moreSizesButton.layoutXProperty().bind(menuPane.widthProperty().subtract(moreSizesButton.widthProperty()+helpButton.widthProperty()))
    menuPane.getChildren().add(moreSizesButton)
    // *end* more sizes button

    // *start* help labels
    val gridSizeHelp: Label = new Label("Select the maze dimensions you want\nusing the sliders and preview grid")
    val textFieldsHelp: Label = new Label("Enter the maze width (number of columns) and\nheight (number of rows) you want below")
    val menuAndGoHelp: Label = new Label("\nThen select the maze generation algorithm\nyou would like to use and click go!")
    val moreSizesHelp: Label = new Label("Click here to manually set the maze size --> ")
    val backToGridHelp: Label = new Label("Click here to go back to the preview grid --> ")
    gridSizeHelp.setVisible(false)
    textFieldsHelp.setVisible(false)
    menuAndGoHelp.setVisible(false)
    moreSizesHelp.setVisible(false)
    backToGridHelp.setVisible(false)
    gridSizeHelp.setTextAlignment(TextAlignment.Center)
    textFieldsHelp.setTextAlignment(TextAlignment.Center)
    menuAndGoHelp.setTextAlignment(TextAlignment.Center)
    moreSizesHelp.setTextAlignment(TextAlignment.Center)
    backToGridHelp.setTextAlignment(TextAlignment.Center)
    borderPane.setTop(gridSizeHelp)
    BorderPane.setAlignment(gridSizeHelp,Pos.Center)
    BorderPane.setAlignment(textFieldsHelp,Pos.Center)
    moreSizesHelp.layoutXProperty().bind(menuPane.widthProperty().subtract(moreSizesButton.widthProperty()+helpButton.widthProperty()+moreSizesHelp.widthProperty()))
    backToGridHelp.layoutXProperty().bind(menuPane.widthProperty().subtract(moreSizesButton.widthProperty()+helpButton.widthProperty()+backToGridHelp.widthProperty()))
    moreSizesHelp.setLayoutY(5)
    backToGridHelp.setLayoutY(5)
    menuPane.getChildren().addAll(moreSizesHelp,backToGridHelp)
    // *end* help labels

    // *start* put choice menu and go button box in VBox with help label
    val wrapperBox: VBox = new VBox()
    wrapperBox.setAlignment(Pos.Center)
    wrapperBox.getChildren().addAll(box,menuAndGoHelp)
    // *end* put choice menu and go button box in VBox with help label
    

    // set children
    var canvasPaneWrapper: Group = new Group()
    canvasPaneWrapper.getChildren().add(canvasPane)
    borderPane.setCenter(canvasPaneWrapper)
    borderPane.setBottom(wrapperBox)
    menuPane.getChildren().add(borderPane)



    def displayDimensionsTooLarge(): Unit = {
        dimensionsTooLargeLabel.setVisible(true)
        val fade: FadeTransition = new FadeTransition()
        fade.setDuration(Duration.millis(5000));  
        fade.setFromValue(10);  
        fade.setToValue(0)
        fade.setNode(dimensionsTooLargeLabel)
        fade.play()
    }

    def showHelp(): Unit = {
        if(miniGridShowing){
            borderPane.setTop(gridSizeHelp)
            gridSizeHelp.setVisible(true)
            menuAndGoHelp.setVisible(true)
            moreSizesHelp.setVisible(true)
            val fade: FadeTransition = new FadeTransition()
            fade.setDuration(Duration.millis(5000));  
            fade.setFromValue(10);  
            fade.setToValue(0)
            fade.setNode(gridSizeHelp)
            fade.play()
            val fade2: FadeTransition = new FadeTransition()
            fade2.setDuration(Duration.millis(5000));  
            fade2.setFromValue(10);  
            fade2.setToValue(0)
            fade2.setNode(menuAndGoHelp)
            fade2.play()
            val fade3: FadeTransition = new FadeTransition()
            fade3.setDuration(Duration.millis(5000));  
            fade3.setFromValue(10);  
            fade3.setToValue(0)
            fade3.setNode(moreSizesHelp)
            fade3.play()
        }
        else{
            borderPane.setTop(textFieldsHelp)
            textFieldsHelp.setVisible(true)
            menuAndGoHelp.setVisible(true)
            backToGridHelp.setVisible(true)
            val fade: FadeTransition = new FadeTransition()
            fade.setDuration(Duration.millis(5000));  
            fade.setFromValue(10);  
            fade.setToValue(0)
            fade.setNode(textFieldsHelp)
            fade.play()
            val fade2: FadeTransition = new FadeTransition()
            fade2.setDuration(Duration.millis(5000));  
            fade2.setFromValue(10);  
            fade2.setToValue(0)
            fade2.setNode(menuAndGoHelp)
            fade2.play()
            val fade3: FadeTransition = new FadeTransition()
            fade3.setDuration(Duration.millis(5000));  
            fade3.setFromValue(10);  
            fade3.setToValue(0)
            fade3.setNode(backToGridHelp)
            fade3.play()
        }
    }

    def redrawMiniGrid(numCols: Int, numRows: Int): Unit = {
        MenuController.setMiniGridSize(numCols, numRows)
        canvasPane.getChildren().remove(gridCanvas)
        gridCanvas = MenuController.getMiniGridCanvas()
        gridCanvas.relocate(canvasPaneOffset,canvasPaneOffset)
        canvasPane.getChildren().add(gridCanvas)
    }

    // switch to the minigrid view
    def miniGridView(): Unit = {
        canvasPaneWrapper.getChildren().clear()
        canvasPaneWrapper.getChildren().add(canvasPane)
    }
    // switch to the text field more sizes view
    def moreSizesView(): Unit = {
        canvasPaneWrapper.getChildren().clear()
        canvasPaneWrapper.getChildren().add(moreSizesBox)
    }

    // actions for when go button is pressed
    def go(): Unit = {
        var numCols: Int = 0
        var numRows: Int = 0
        if(miniGridShowing){
            numCols = numColsSlider.getValue().toInt
            numRows = numRowsSlider.getValue().toInt
        }
        else{
            numCols = widthField.getText().toInt
            numRows = heightField.getText().toInt
            if(numCols>=500 || numRows>=500){
                displayDimensionsTooLarge()
                return
            }
        }
        SceneController.switchToGame()
        GameController.startMaze(numCols, numRows, mazeAlgorithmChoice.getValue())
        GameController.resetDefaults()
    }
 

}
