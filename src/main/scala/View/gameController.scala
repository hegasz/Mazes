package view

import algorithms.buildingBlocks._
import algorithms.RecursiveBacktracking._
import scalafx.beans.property.ObjectProperty
import scalafx.scene.canvas.Canvas
import scalafx.scene.layout.StackPane

object GameController{
    var size: Size = (5,9)
    var maze: Maze = RecursiveBacktrackingMazeBuilder(size)
    val gridState = ObjectProperty(GameStack(size, maze, 900))
    var mazeBoxRatio: Double = 0.7 // require this is between 0 and 1
    var screenWidth: Double = 900; var screenHeight: Double = 700

    def boxSize: Double = {
        val limitingSize = screenWidth.min(screenHeight)
        val boxSize = limitingSize * mazeBoxRatio
        boxSize
    }

    def reSize(width: Double, height: Double): Unit = {
        screenWidth = width; screenHeight = height
        gridState.update(GameStack(size, maze, boxSize))
        GamePane.reSize(width, height, boxSize)
    }

    def updateBoxRatio(newRatio: Double): Unit = {
        if(newRatio > 0 && newRatio <=0.9){
            mazeBoxRatio = newRatio
            gridState.update(GameStack(size, maze, boxSize))
            GamePane.reSize(screenWidth, screenHeight, boxSize)
        }
    }

    def decreaseBoxRatio(decrement: Double): Unit = updateBoxRatio(mazeBoxRatio-decrement)
    def increaseBoxRatio(increment: Double): Unit = updateBoxRatio(mazeBoxRatio+increment)

    def changeMazeDimensions(numCols: Int, numRows: Int): Unit = {
        val newSize: Size = (numCols, numRows)
        size = newSize
        maze = RecursiveBacktrackingMazeBuilder(size)
        gridState.update(GameStack(size, maze, boxSize))
        GamePane.reSize(screenWidth, screenHeight, boxSize)
    }

    def getGameStack(): StackPane = gridState.value.gameStack

    def getMazeBoxRatio(): Double = mazeBoxRatio

    def moveLeft(): Unit = {
        gridState.value.movePlayerLeft()
        checkWin()
    }
    def moveRight(): Unit = {
        gridState.value.movePlayerRight()
        checkWin()
    }
    def moveUp(): Unit = {
        gridState.value.movePlayerUp()
        checkWin()
    }
    def moveDown(): Unit = {
        gridState.value.movePlayerDown()
        checkWin()

    }
    def checkWin(): Unit = {
        if(gridState.value.playerX == size._1-1 && gridState.value.playerY == size._2-1){
            SceneController.switchToEnd()
        }
    }
}
