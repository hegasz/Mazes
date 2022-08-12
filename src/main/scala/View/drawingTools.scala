package view

import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.paint.Color._
import javafx.scene.shape._
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.TilePane
import scalafx.scene.layout.BorderPane
import scalafx.scene.layout.HBox
import scalafx.geometry.Insets
import scalafx.scene.control.Button
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.RowConstraints
import javafx.scene.Group
import javafx.scene.Node
import algorithms.buildingBlocks._
import algorithms.buildingBlocks.Direction._

import javafx.scene.layout.Pane
import javafx.scene.paint.Color

/**
  * 
  *
  * @param size
  * @param windowWidth
  * @param windowHeight
  * @param lineThicknessPercentage
  */
class GridDrawing(size: Size, windowWidth: Int, windowHeight: Int, lineThicknessPercentage: Double = 5){
    private val numCols: Int = size._1; private val numRows: Int = size._2
    private val lineLength: Int = (windowWidth/numCols).min(windowHeight/numRows)
    private val lineWidth: Double = (lineThicknessPercentage/100)*lineLength

    // NOTE:
    // GridPane coordinates start in the top left, so are numbered as follows:
    // (0,0) (1,0) etc
    // (0,1) (1,1) etc
    //  etc   etc
    // THIS IS DIFFERENT TO MAZE GRID WHICH STARTS IN BOTTOM LEFT
    val grid = new GridPane()
    initialiseGrid()

    def makeClosedCell: Pane = {
        val canvas: Pane = new Pane()
        canvas.setPrefSize(lineLength,lineLength)
        val wallN = Line(0,0,lineLength,0)
        wallN.setStrokeWidth(lineWidth)
        val wallW = Line(0,0,0,lineLength)
        wallW.setStrokeWidth(lineWidth)
        canvas.getChildren().addAll(wallN,wallW)
        return canvas
    }

    def makeOpenW: Pane = {
        val canvas: Pane = new Pane()
        canvas.setPrefSize(lineLength,lineLength)
        val wallN: Line = new Line(0,0,lineLength,0)
        wallN.setStrokeWidth(lineWidth)
        canvas.getChildren().add(wallN)
        return canvas
    }

    def makeOpenN: Pane = {
        val canvas: Pane = new Pane()
        canvas.setPrefSize(lineLength,lineLength)
        val wallW: Line = new Line(0,0,0,lineLength)
        wallW.setStrokeWidth(lineWidth)
        canvas.getChildren().add(wallW)
        return canvas
    }

    def initialiseGrid(): Unit = {
        for (i <- 0 until numCols) {
            val colConst = new ColumnConstraints()
            colConst.setMinWidth(lineLength)
            colConst.setMaxWidth(lineLength)

            grid.getColumnConstraints().add(colConst)
        }
        for (i <- 0 until numRows) {
            val rowConst = new RowConstraints()
            rowConst.setMinHeight(lineLength)
            rowConst.setMaxHeight(lineLength)
            grid.getRowConstraints().add(rowConst)      
        }
    }
    
    // ridiculously stupid way to retrieve object at a 
    // certain coordinate in grid, but seems to be the
    // only way to do it.
    def getFromGrid(x: Int, y: Int): Node = {
        for(node <- grid.getChildren()){
            if (GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node) == y){
                return node
            }
        }
        return null
    }

    def setOpenW(x: Int, y: Int): Unit = {
        val openW: Pane = makeOpenW
        grid.getChildren().remove(getFromGrid(x, y))
        grid.add(openW,x,y)
    }
    def setOpenN(x: Int, y: Int): Unit = {
        val openN: Pane = makeOpenN
        grid.getChildren().remove(getFromGrid(x, y))
        grid.add(openN,x,y)
    }
    def setClosedCell(x: Int, y: Int): Unit = {
        val closedCell: Pane = makeClosedCell
        grid.getChildren().remove(getFromGrid(x, y))
        grid.add(closedCell,x,y)
    }
    def setOpenCell(x: Int, y: Int): Unit = {
        grid.getChildren().remove(getFromGrid(x, y))
    }

    def drawPlainGrid(): Unit = {
        for(j <- 0 until numRows){
            for(i <- 0 until numCols){
                grid.add(makeClosedCell,i,j)
            }
            grid.add(makeOpenN,numCols,j)
        }
        for(i <- 0 until numCols){
            grid.add(makeOpenW,i,numRows)
        }
    }

    def drawMazeGrid(maze: Maze): Unit = {
        val mazeSize = maze.size
        require(mazeSize == size)
        val mazeGrid = maze.grid
        
        for(j <- 0 until numRows){
            var mazeGridJ = numRows - 1 - j
            for(i <- 0 until numCols){
                var dirSet = mazeGrid(mazeGridJ)(i)
                if(!((dirSet contains N) && (dirSet contains W))){
                    if(dirSet contains N) grid.add(makeOpenN,i,j)
                    else if(dirSet contains W) grid.add(makeOpenW,i,j)
                    else grid.add(makeClosedCell,i,j)
                }
            }
            grid.add(makeOpenN,numCols,j)
        }
        for(i <- 0 until numCols){
            grid.add(makeOpenW,i,numRows)
        }
        
    }
}

// make some shapes have a sprite in the middle
// and when you move to that cell replace the canvas
// with e.g. openNwithSprite