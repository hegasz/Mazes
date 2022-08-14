package view

import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.paint.Color._
import javafx.scene.shape._
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.TilePane
import scalafx.scene.layout.BorderPane
import scalafx.scene.layout.StackPane
import scalafx.scene.layout.HBox
import scalafx.geometry.Insets
import scalafx.scene.control.Button
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.RowConstraints
import javafx.scene.Group
import javafx.scene.Node
import algorithms.buildingBlocks._
import algorithms.buildingBlocks.Direction._
import scalafx.scene.canvas.Canvas
import scalafx.scene.text.Font

import javafx.scene.layout.Pane
import javafx.scene.paint.Color

case class ScreenDimensions(var width: Double, var height: Double)

/**
  * 
  *
  * @param size
  * @param windowWidth
  * @param windowHeight
  * @param lineThicknessPercentage
  */
case class GridCanvas(size: Size, mazeInput: Maze, val boxSize: Double, lineThicknessPercentage: Double = 5, borderThicknessMultiplier: Int = 1, forDisplayOnly: Boolean = false){

    // TO-DO: CLEAN UP - QUITE A LOT OF THESE METHODS NOT ACTUALLY NEEDED

    private val numCols: Int = size._1; private val numRows: Int = size._2
    private var lineLength: Double = boxSize/((numCols).max(numRows))
    private var lineWidth: Double = (lineThicknessPercentage/100)*lineLength
    private val sideOffset: Double = (boxSize - (lineLength * numCols))/2
    private val topOffset: Double = (boxSize - (lineLength * numRows))/2

    var gameStack: StackPane = new StackPane
    gameStack.setMinWidth(boxSize)
    gameStack.setMaxWidth(boxSize)
    gameStack.setMinHeight(boxSize)
    gameStack.setMaxHeight(boxSize)

    var gridCanvas: Canvas = new Canvas(boxSize, boxSize)
    var playerCanvas: Canvas = new Canvas(boxSize, boxSize)

    gameStack.getChildren().addAll(gridCanvas, playerCanvas)
    
    var maze: Maze = mazeInput
    var grid = gridCanvas.getGraphicsContext2D()
    var player = playerCanvas.getGraphicsContext2D()

    grid.setStroke(Color.BLACK)

    if(borderThicknessMultiplier != 1) drawBorder()

    if(forDisplayOnly) drawDisplayGrid()
    else drawGrid()

    

    // Draw canvas border
    def drawBorder(): Unit = {
        grid.moveTo(0,0)
        grid.setLineWidth(lineWidth*borderThicknessMultiplier)
        grid.lineTo(0,boxSize)
        grid.lineTo(boxSize,boxSize)
        grid.lineTo(boxSize,0)
        grid.lineTo(0,0)
        grid.stroke()
        grid.setLineWidth(lineWidth)
    }

    // Turn a grid i index into canvas coordinates.
    // Indices start in top left as per graphics standard.
    def iToCoord(index: Int): Double = (lineLength * index) + sideOffset
    // Turn a grid j index into canvas coordinates.
    // Indices start in top left as per graphics standard.
    def jToCoord(index: Int): Double = (lineLength * index) + topOffset

    def drawTopLeftCorner(i: Int, j: Int): Unit = {
        val x: Double = iToCoord(i); val y: Double = jToCoord(j) // (x,y) is top left of this cell
        grid.beginPath()
        grid.moveTo(x,y+lineLength)
        grid.lineTo(x,y)
        grid.lineTo(x+lineLength,y)
        grid.stroke()
    }

    def drawN(i: Int, j: Int): Unit = {
        val x: Double = iToCoord(i); val y: Double = jToCoord(j)
        grid.beginPath()
        grid.moveTo(x,y)
        grid.lineTo(x+lineLength,y)
        grid.stroke()
    }

    def drawW(i: Int, j: Int): Unit = {
        val x: Double = iToCoord(i); val y: Double = jToCoord(j)
        grid.beginPath()
        grid.moveTo(x,y)
        grid.lineTo(x,y+lineLength)
        grid.stroke()
    }

    def drawE(i: Int, j: Int): Unit = {
        val x: Double = iToCoord(i); val y: Double = jToCoord(j)
        grid.beginPath()
        grid.moveTo(x+lineLength,y)
        grid.lineTo(x+lineLength,y+lineLength)
        grid.stroke()
    }

    def drawS(i: Int, j: Int): Unit = {
        val x: Double = iToCoord(i); val y: Double = jToCoord(j)
        grid.beginPath()
        grid.moveTo(x,y+lineLength)
        grid.lineTo(x+lineLength,y+lineLength)
        grid.stroke()
    }

    def drawSouthEastCorner(i: Int, j: Int): Unit = {
        val x: Double = iToCoord(i); val y: Double = jToCoord(j)
        grid.beginPath()
        grid.moveTo(x,y+lineLength)
        grid.lineTo(x+lineLength,y+lineLength)
        grid.lineTo(x+lineLength,y)
        grid.stroke()
    }

    def drawStart(): Unit = {
        val x: Double = iToCoord(0); val y: Double = jToCoord(numRows-1)
        grid.setLineWidth(lineWidth/4)
        grid.setFont(Font.font("Monospaced", (lineLength/5).toInt))
        grid.strokeText("start",x+lineLength*(0.2),y+lineLength*0.55)
        grid.setLineWidth(lineWidth)
    }

    def drawFinish(): Unit = {
        val x: Double = iToCoord(numCols-1); val y: Double = jToCoord(0)
        grid.setLineWidth(lineWidth/4)
        grid.setFont(Font.font("Monospaced", (lineLength/5).toInt))
        grid.strokeText("finish",x+lineLength/7,y+lineLength/2)
        grid.setLineWidth(lineWidth)
    }

    // draw maze grid onto canvas
    def drawGrid(): Unit = {
        val mazeGrid = maze.grid
        for(j <- 0 until numRows){
            var mazeGridJ = numRows - 1 - j
            for(i <- 0 until numCols){
                var dirSet = mazeGrid(mazeGridJ)(i)
                if(!(dirSet contains N) && !(dirSet contains W)) drawTopLeftCorner(i,j)
                else if((dirSet contains N) && !(dirSet contains W)) drawW(i,j)
                else if(!(dirSet contains N) && (dirSet contains W)) drawN(i,j)
            }
            drawE(numCols-1,j)
        }
        for(i <- 0 until numCols){
            drawS(i,numRows-1)
        }
        drawStart()
        drawFinish()
    }

    // draw display maze grid (no start or stop tiles or game elements) onto canvas
    def drawDisplayGrid(): Unit = {
        val mazeGrid = maze.grid
        for(j <- 0 until numRows){
            var mazeGridJ = numRows - 1 - j
            for(i <- 0 until numCols){
                var dirSet = mazeGrid(mazeGridJ)(i)
                if(!(dirSet contains N) && !(dirSet contains W)) drawTopLeftCorner(i,j)
                else if((dirSet contains N) && !(dirSet contains W)) drawW(i,j)
                else if(!(dirSet contains N) && (dirSet contains W)) drawN(i,j)
            }
            drawE(numCols-1,j)
        }
        for(i <- 0 until numCols){
            drawS(i,numRows-1)
        }
    }
    
}