package view

import algorithms.buildingBlocks._
import algorithms.RecursiveBacktracking._
import scalafx.beans.property.ObjectProperty
import scalafx.scene.canvas.Canvas
import scalafx.scene.layout.StackPane
import scalafx.beans.property.IntegerProperty

object GameController{
    var size: Size = (5,9)
    var maze: Maze = RecursiveBacktrackingMazeBuilder(size)
    val gridState = ObjectProperty(GameStack(size, maze, 900))
    var mazeBoxRatio: Double = 0.7 // require this is between 0 and 1
    var screenWidth: Double = 900; var screenHeight: Double = 700
    val direction = IntegerProperty(5) // 1-left, 2-right, 3-up, 4-down, any other value do nothing
    var controls = "discrete" // or pacman
    var pacmanSpeed = 4

    def resetDefaults(): Unit = {
        direction.value = 5
    }

    // called everytime clock ticks in main window
    def clockTick(time: Int): Unit = {
        if(SceneController.activeScene == "game"){
            if(controls == "pacman"){
                if(time % pacmanSpeed == 0){ // run time slower than main clock
                    pacmanMove()
                }
            }
        }
    }

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

    def handleLeft(): Unit = {
        if(controls == "discrete") moveLeft()
        else if(controls == "pacman") changeDirectionLeft()
    }
    def handleRight(): Unit = {
        if(controls == "discrete") moveRight()
        else if(controls == "pacman") changeDirectionRight()
    }
    def handleUp(): Unit = {
        if(controls == "discrete") moveUp()
        else if(controls == "pacman") changeDirectionUp()
    }
    def handleDown(): Unit = {
        if(controls == "discrete") moveDown()
        else if(controls == "pacman") changeDirectionDown()
    }


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
            SceneController.switchToWon()
        }
    }

    def changeDirectionLeft(): Unit = {
        direction.value = 1
    }
    def changeDirectionRight(): Unit = {
        direction.value = 2
    }
    def changeDirectionUp(): Unit = {
        direction.value = 3
    }
    def changeDirectionDown(): Unit = {
        direction.value = 4
    }

    def pacmanMove(): Unit = {
        direction.value match {
            case 1 => moveLeft()
            case 2 => moveRight()
            case 3 => moveUp()
            case 4 => moveDown()
            case _ => 
        }
    }

    def setPacmanSpeed(newSpeed: Int): Unit = {
        require(newSpeed >= 1 && newSpeed <= 10)
        pacmanSpeed = newSpeed
    }

    def setDiscreteControls(): Unit = controls = "discrete"
    def setPacmanControls(): Unit = {controls = "pacman"; direction.value = 5}
}
