package view

import algorithms.buildingBlocks._
import algorithms.RecursiveBacktracking._
import algorithms.Kruskal._
import algorithms.BinaryTree._
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
    val queuedDirection = IntegerProperty(5)
    var controls = "discrete" // or pacman
    var pacmanSpeed = 4

    def resetDefaults(): Unit = {
        direction.value = 5
        queuedDirection.value = 5
    }

    // called everytime clock ticks in main window
    def clockTick(time: Int): Unit = {
        if(SceneController.activeScene == "game"){
            if(controls == "pacman"){
                if(time % pacmanSpeed == 0){ // run time slower than main clock
                    val attempt: Boolean = pacmanMoveDirection()
                    // if move not allowed, queue it
                    if(!attempt){
                        pacmanMoveQueuedDirection()
                        direction.value = queuedDirection.value
                        queuedDirection.value = 5
                    }
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

    def changeMazeAlgorithm(newAlgorithm: String): Unit = {
        newAlgorithm match {
            case "Randomised Recursive Backtracking (longer corridors)" => maze = RecursiveBacktrackingMazeBuilder(size)
            case "Randomised Kruskal's MST (shorter corridors)" => maze = KruskalMazeBuilder(size)
            case "Random Binary Tree (strong diagonal bias)" => maze = BinaryTreeMazeBuilder(size)
            case _ => 
        }
        gridState.update(GameStack(size, maze, boxSize))
        GamePane.updateMazeAlgorithm()
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

    /** Attempts to move player one grid square left
     * @return whether move was carried out. */
    def moveLeft(): Boolean = {
        val attempt: Boolean = gridState.value.movePlayerLeft()
        checkWin()
        return attempt
    }
    /** Attempts to move player one grid square right
     * @return whether move was carried out. */
    def moveRight(): Boolean = {
        val attempt: Boolean = gridState.value.movePlayerRight()
        checkWin()
        return attempt
    }
    /** Attempts to move player one grid square up
     * @return whether move was carried out. */
    def moveUp(): Boolean = {
        val attempt: Boolean = gridState.value.movePlayerUp()
        checkWin()
        return attempt
    }
    /** Attempts to move player one grid square down
     * @return whether move was carried out. */
    def moveDown(): Boolean = {
        val attempt: Boolean = gridState.value.movePlayerDown()
        checkWin()
        return attempt
    }
    // check whether player is at finish square
    def checkWin(): Unit = {
        if(gridState.value.playerX == size._1-1 && gridState.value.playerY == size._2-1){
            SceneController.switchToWon()
        }
    }

    def canMoveLeft(): Boolean = gridState.value.canMoveLeft()
    def canMoveRight(): Boolean = gridState.value.canMoveRight()
    def canMoveUp(): Boolean = gridState.value.canMoveUp()
    def canMoveDown(): Boolean = gridState.value.canMoveDown()
    

    def changeDirectionLeft(): Unit = {
        if(canMoveLeft()) direction.value = 1
        else queuedDirection.value = 1
    }
    def changeDirectionRight(): Unit = {
        if(canMoveRight()) direction.value = 2
        else queuedDirection.value = 2
    }
    def changeDirectionUp(): Unit = {
        if(canMoveUp()) direction.value = 3
        else queuedDirection.value = 3
    }
    def changeDirectionDown(): Unit = {
        if(canMoveDown()) direction.value = 4
        else queuedDirection.value = 4
    }

    def pacmanMoveDirection(): Boolean = {
        direction.value match {
            case 1 => moveLeft()
            case 2 => moveRight()
            case 3 => moveUp()
            case 4 => moveDown()
            case _ => false
        }
    }
    def pacmanMoveQueuedDirection(): Unit = {
        queuedDirection.value match {
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
