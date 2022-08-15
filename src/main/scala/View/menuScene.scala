package view

import scalafx.Includes._
import scalafx.scene.Scene
import scalafx.scene.paint.Color._
import scalafx.scene.layout.StackPane
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
import algorithms.buildingBlocks._
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.Slider
import scalafx.geometry.Orientation
import scalafx.beans.binding.Bindings
import scalafx.scene.Group

object MenuPane{
    val menuPane: Pane = new Pane()
    menuPane.setStyle("-fx-background-color: #f8ad9d;")
    val borderPane: BorderPane = new BorderPane()
    borderPane.layoutXProperty().bind(menuPane.widthProperty().subtract(borderPane.widthProperty()).divide(2));
    borderPane.layoutYProperty().bind(menuPane.heightProperty().subtract(borderPane.heightProperty()).divide(2));

    var canvasPane: Pane = new Pane()
    val canvasPaneSize: Double = MenuController.boxSize*1.3 // more than 1.3*boxSize to avoid weird clipping
    val canvasPaneOffset: Double = (canvasPaneSize-MenuController.boxSize)/2
    canvasPane.setMaxWidth(canvasPaneSize)
    canvasPane.setMinWidth(canvasPaneSize)
    canvasPane.setMaxHeight(canvasPaneSize)
    canvasPane.setMinHeight(canvasPaneSize)
    
    var gridCanvas: Canvas = MenuController.getMiniGridCanvas()
    gridCanvas.relocate(canvasPaneOffset,canvasPaneOffset)

    val box: HBox = new HBox()
    box.setAlignment(Pos.Center)

    val numColsSlider: Slider = new Slider(1,20,10)
    val numRowsSlider: Slider = new Slider(1,20,10)
    numColsSlider.setMajorTickUnit(1)
    numRowsSlider.setMajorTickUnit(1)
    numColsSlider.setMinorTickCount(0)
    numRowsSlider.setMinorTickCount(0)
    numColsSlider.setSnapToTicks(true)
    numRowsSlider.setSnapToTicks(true)
    numRowsSlider.setOrientation(Orientation.Vertical)
    val sliderLength: Double = MenuController.boxSize
    numColsSlider.setMaxWidth(sliderLength)
    numRowsSlider.setMaxHeight(sliderLength)
    numColsSlider.setMinWidth(sliderLength)
    numRowsSlider.setMinHeight(sliderLength)
    numColsSlider.relocate(canvasPaneOffset,MenuController.boxSize+canvasPaneOffset)
    numRowsSlider.relocate(MenuController.boxSize+canvasPaneOffset,canvasPaneOffset)


    numColsSlider.setStyle("-fx-base: #000000; -fx-control-inner-background: #f08080;")
    numRowsSlider.setStyle("-fx-base: #000000; -fx-control-inner-background: #f08080;")
    
    //val numColsField: TextField = new TextField()
    //val numRowsField: TextField = new TextField()
    //numColsField.setTextFormatter(new TextFormatter(new IntegerStringConverter()))
    //numRowsField.setTextFormatter(new TextFormatter(new IntegerStringConverter()))
    //numColsField.setText("10")
    //numRowsField.setText("10")
    //numColsField.textProperty().addListener((observable, oldValue, newValue) => {
    //    println("textfield changed from " + oldValue + " to " + newValue);
    //})
    //numRowsField.textProperty().addListener((observable, oldValue, newValue) => {
    //    println("textfield changed from " + oldValue + " to " + newValue);
    //})

    numColsSlider.valueProperty().addListener((observable, oldValue, newValue) => {
        redrawMiniGrid(newValue.asInstanceOf[Double].toInt, numRowsSlider.getValue().asInstanceOf[Double].toInt)
    })
    numRowsSlider.valueProperty().addListener((observable, oldValue, newValue) => {
        redrawMiniGrid(numColsSlider.getValue().asInstanceOf[Double].toInt, newValue.asInstanceOf[Double].toInt)
    })


    //val dimensionsTooLargeLabel: Label = new Label("Dimensions less than 2500 please")
    //dimensionsTooLargeLabel.setVisible(false)

    val algorithms = ObservableBuffer(
        "Randomised Recursive Backtracking (longer corridors)",
        "Randomised Kruskal's MST (shorter corridors)"
    )
    val mazeAlgorithmChoice = new ChoiceBox(algorithms)
    mazeAlgorithmChoice.setValue("Randomised Recursive Backtracking (longer corridors)")

    val goButton = new Button("GO")
    goButton.setOnAction(new EventHandler[ActionEvent]{
        override def handle(event: ActionEvent): Unit = {
            //val numCols: Int = numColsField.getText().toInt
            //val numRows: Int = numRowsField.getText().toInt
            //if(numCols>2500 || numRows>2500) displayDimensionsTooLarge()
            //else{
            //    GameController.changeMazeDimensions(numCols,numRows)
            //    SceneController.switchToGame()
            //}
            val numCols: Int = numColsSlider.getValue().toInt
            val numRows: Int = numRowsSlider.getValue().toInt

            GameController.changeMazeDimensions(numCols,numRows)
            GameController.resetDefaults()
            SceneController.switchToGame()

        }
    })

    box.getChildren().addAll(mazeAlgorithmChoice, goButton)
    canvasPane.getChildren().addAll(gridCanvas,numColsSlider,numRowsSlider)
    var canvasPaneWrapper: Group = new Group()
    canvasPaneWrapper.getChildren().add(canvasPane)
    borderPane.setCenter(canvasPaneWrapper)
    borderPane.setBottom(box)
    //BorderPane.setAlignment(canvasPane,Pos.Center)
    menuPane.getChildren().add(borderPane)

    def displayDimensionsTooLarge(): Unit = {
        //dimensionsTooLargeLabel.setVisible(true)
        val fade: FadeTransition = new FadeTransition()
        fade.setDuration(Duration.millis(3000));  
        fade.setFromValue(10);  
        fade.setToValue(0)
        //fade.setNode(dimensionsTooLargeLabel)
        fade.play()
        //numColsField.setText("10"); numRowsField.setText("10")
    }

    def redrawMiniGrid(numCols: Int, numRows: Int): Unit = {
        MenuController.setMiniGridSize(numCols, numRows)
        canvasPane.getChildren().remove(gridCanvas)
        gridCanvas = MenuController.getMiniGridCanvas()
        gridCanvas.relocate(canvasPaneOffset,canvasPaneOffset)
        canvasPane.getChildren().add(gridCanvas)
    }
}
