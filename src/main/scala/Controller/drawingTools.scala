package controller

import algorithms.BuildingBlocks._
import algorithms.BuildingBlocks.Direction._
import scalafx.Includes._
import scalafx.scene.layout.StackPane
import scalafx.scene.canvas.Canvas
import scalafx.scene.text.Font
import javafx.scene.paint.Color

case class ScreenDimensions(var width: Double, var height: Double)

/** Class for creation of stackpanes containing game elements 
  * @param size size of maze
  * @param windowWidth width of window the object sits in
  * @param windowHeight height of window the object sits in
  * @param lineThicknessPercentage line thickness as percentage of grid square size
  */
case class GameStack(size: Size, mazeInput: Maze, val boxSize: Double, lineThicknessPercentage: Double = 5, borderThicknessMultiplier: Int = 1, forDisplayOnly: Boolean = false, lineColour: String = "E69A8DFF"){

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
    var playerX: Int = 0; var playerY: Int = 0 // player (0,0) is bottom left

    grid.setStroke(Color.web(lineColour))
    player.setFill(Color.web(lineColour))

    if(borderThicknessMultiplier != 1) drawBorder()

    if(forDisplayOnly) drawDisplayGrid()
    else drawGrid()

    drawPlayerAtGridCoords(0,0)
    

    // Draw canvas border
    def drawBorder(): Unit = {
        if(forDisplayOnly) grid.setStroke(Color.BLACK)
        grid.moveTo(0,0)
        grid.setLineWidth(lineWidth*borderThicknessMultiplier)
        grid.lineTo(0,boxSize)
        grid.lineTo(boxSize,boxSize)
        grid.lineTo(boxSize,0)
        grid.lineTo(0,0)
        grid.stroke()
        grid.setLineWidth(lineWidth)
        grid.setStroke(Color.web(lineColour))
    }

    // Turn a grid i index into canvas coordinates.
    // Indices start in top left as per graphics standard.
    def iToCoord(index: Int): Double = {
        require(index>=0 && index < numCols)
        (lineLength * index) + sideOffset
    }
    // Turn a grid j index into canvas coordinates.
    // Indices start in top left as per graphics standard.
    def jToCoord(index: Int): Double = {
        require(index>=0 && index < numRows)
        (lineLength * index) + topOffset
    }

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
        grid.strokeText("finish",x+lineLength/7,y+lineLength*0.55)
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
        grid.setStroke(Color.BLACK)
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
        grid.setStroke(Color.web(lineColour))
    }
    
    def drawPlayerAtGridCoords(i: Int, j: Int): Unit = {
        require(i >= 0 && i < numCols && j >= 0 && j < numRows)
        // y is now opposite to graphic coordinates
        val x: Double = iToCoord(i); val y: Double = jToCoord(numRows-1-j)
        player.clearRect(0, 0, boxSize, boxSize)
        val diameter: Double = lineLength/4
        player.fillOval(x+(lineLength*3/8), y+(lineLength*3/8), diameter, diameter)
    }

    /** Clears canvas and redraws player in new position
     * @return whether move was carried out */
    def movePlayerLeft(): Boolean = {
        val newPlayerX = playerX - 1
        val doesMazeAllow = maze.grid(playerY)(playerX) contains W
        if(newPlayerX >= 0 && doesMazeAllow){
            drawPlayerAtGridCoords(newPlayerX, playerY)
            playerX = newPlayerX
            return true
        }
        return false
    }
    /** Clears canvas and redraws player in new position
     * @return whether move was carried out */
    def movePlayerRight(): Boolean = {
        val newPlayerX = playerX + 1
        val doesMazeAllow = maze.grid(playerY)(playerX) contains E
        if(newPlayerX < numCols && doesMazeAllow){
            drawPlayerAtGridCoords(newPlayerX, playerY)
            playerX = newPlayerX
            return true
        }
        return false
    }
    /** Clears canvas and redraws player in new position
     * @return whether move was carried out */
    def movePlayerUp(): Boolean = {
        val newPlayerY = playerY + 1
        val doesMazeAllow = maze.grid(playerY)(playerX) contains N
        if(newPlayerY < numRows && doesMazeAllow){
            drawPlayerAtGridCoords(playerX, newPlayerY)
            playerY = newPlayerY
            return true
        }
        return false
    }
    /** Clears canvas and redraws player in new position
     * @return whether move was carried out */
    def movePlayerDown(): Boolean = {
        val newPlayerY = playerY - 1
        val doesMazeAllow = maze.grid(playerY)(playerX) contains S
        if(newPlayerY >= 0 && doesMazeAllow){
            drawPlayerAtGridCoords(playerX, newPlayerY)
            playerY = newPlayerY
            return true
        }
        return false
    }

    def canMoveLeft(): Boolean = {
        maze.grid(playerY)(playerX) contains W
    }
    def canMoveRight(): Boolean = {
        maze.grid(playerY)(playerX) contains E
    }
    def canMoveUp(): Boolean = {
        maze.grid(playerY)(playerX) contains N
    }
    def canMoveDown(): Boolean = {
        maze.grid(playerY)(playerX) contains S
    }
}