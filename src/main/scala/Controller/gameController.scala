package controller

import view.GamePane
import algorithms.BuildingBlocks._
import algorithms.Kruskal._
import algorithms.BinaryTree._
import algorithms.RecursiveBacktracking._
import scalafx.beans.property.ObjectProperty
import scalafx.scene.layout.StackPane
import scalafx.beans.property.IntegerProperty
import scalafx.application.Platform
import scala.collection.mutable.Queue
import javafx.concurrent.Task

object GameController{
    private var size: Size = (10,10)
    private var maze: Maze = Maze(getEmptyGrid(size._1,size._2),size)
    private val gridState = ObjectProperty(GameStack(size, maze, 900))
    private var mazeBoxRatio: Double = 0.7 // require this is between 0 and 1
    private var screenWidth: Double = 900; var screenHeight: Double = 700
    private val direction = IntegerProperty(5) // 1-left, 2-right, 3-up, 4-down, any other value does nothing
    private val queuedDirection = IntegerProperty(5)
    private var controls = "discrete" // or pacman
    private var pacmanSpeed = 4
    var cameraOn: Boolean = true
    val constructionProcess: Queue[Grid] = new Queue[Grid]()
    private var mazeAlgorithm: String = ""
    private var movementAllowed: Boolean = false
    
    // reset directions to a neutral value
    def resetDefaults(): Unit = {
        direction.value = 5
        queuedDirection.value = 5
    }

    // called everytime clock ticks in main window
    def clockTick(time: Int): Unit = {
        if(SceneController.getActiveScene == "game"){
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

    // calculate siez of game box
    def boxSize: Double = {
        val limitingSize = screenWidth.min(screenHeight)
        val boxSize = limitingSize * mazeBoxRatio
        boxSize
    }

    // resize game screen
    def reSize(width: Double, height: Double): Unit = {
        screenWidth = width; screenHeight = height
        gridState.update(GameStack(size, maze, boxSize))
        GamePane.reSize(width, height, boxSize)
    }

    // create requested maze and initialise game screen
    def startMaze(numCols: Int, numRows: Int, algorithm: String): Unit = {
        val newSize: Size = (numCols, numRows)
        size = newSize
        // set the grid to empty
        maze = Maze(getEmptyGrid(size._1,size._2),size)
        gridState.update(GameStack(size, maze, boxSize))
        GamePane.reSize(screenWidth, screenHeight, boxSize)
        algorithm match {
            case "Randomised Recursive Backtracking (longer corridors)" =>{
                mazeAlgorithm = algorithm
                maze = RecursiveBacktrackingMazeBuilder(size)
            }
            case "Randomised Kruskal's MST (shorter corridors)" =>{
                mazeAlgorithm = algorithm
                maze = KruskalMazeBuilder(size)
            }
            case "Random Binary Tree (strong diagonal bias)" =>{
                mazeAlgorithm = algorithm
                maze = BinaryTreeMazeBuilder(size)
            }
            case _ => 
        }
        if(size._1*size._2 <= 8100) animateConstruction()
        else{ // do not animate for mmazes that are too large
            gridState.update(GameStack(size, maze, boxSize))
            GamePane.updateMaze()
        }
    }

    // play animation of maze construction
    def animateConstruction(): Unit = {
        movementAllowed = false
        val numCells: Int = size._1*size._2
        var timePerMove: Long = 0
        if(numCells <= 2000) timePerMove = 2000/numCells
        else timePerMove = 2

        val task: Task[Unit] = new Task[Unit]() {
            override def call(): Unit = {
                while(!constructionProcess.isEmpty){
                    Thread.sleep(timePerMove)
                    var currentGrid: Grid = constructionProcess.dequeue()
                    Platform.runLater(() => {
                        gridState.update(GameStack(size, Maze(currentGrid,size), boxSize))
                        GamePane.updateMaze()
                    })
                }
            }
        }
        task.setOnSucceeded(e => {
            movementAllowed = true
        })
        new Thread(task).start()
    }


    // change how much of the screen the maze takes up
    def updateBoxRatio(newRatio: Double): Unit = {
        if(newRatio > 0 && newRatio <=0.8){
            mazeBoxRatio = newRatio
            gridState.update(GameStack(size, maze, boxSize))
            GamePane.reSize(screenWidth, screenHeight, boxSize)
        }
    }

    def decreaseBoxRatio(): Unit = updateBoxRatio(mazeBoxRatio-0.1)
    def increaseBoxRatio(): Unit = updateBoxRatio(mazeBoxRatio+0.1)

    // return the stackpane that all game elements are on
    def getGameStack(): StackPane = gridState.value.gameStack

    def getMazeBoxRatio(): Double = mazeBoxRatio

    // handle a left hand key press
    def handleLeft(): Unit = {
        if(movementAllowed){
            if(controls == "discrete") moveLeft()
            else if(controls == "pacman") changeDirectionLeft()
        }
    }
    // handle a right hand key press
    def handleRight(): Unit = {
        if(movementAllowed){
            if(controls == "discrete") moveRight()
            else if(controls == "pacman") changeDirectionRight()
        }
    }
    // handle an up key press
    def handleUp(): Unit = {
        if(movementAllowed){
            if(controls == "discrete") moveUp()
            else if(controls == "pacman") changeDirectionUp()
        }
    }
    // handle a down key press
    def handleDown(): Unit = {
        if(movementAllowed){
            if(controls == "discrete") moveDown()
            else if(controls == "pacman") changeDirectionDown()
        }
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

    // can player currently move left?
    def canMoveLeft(): Boolean = gridState.value.canMoveLeft()
    // can player currently move right?
    def canMoveRight(): Boolean = gridState.value.canMoveRight()
    // can player currently move up?
    def canMoveUp(): Boolean = gridState.value.canMoveUp()
    // can player currently move down?
    def canMoveDown(): Boolean = gridState.value.canMoveDown()
    
    // change direction to left (for pacman controls)
    def changeDirectionLeft(): Unit = {
        if(canMoveLeft()) direction.value = 1
        else queuedDirection.value = 1
    }
    // change direction to right (for pacman controls)
    def changeDirectionRight(): Unit = {
        if(canMoveRight()) direction.value = 2
        else queuedDirection.value = 2
    }
    // change direction to up (for pacman controls)
    def changeDirectionUp(): Unit = {
        if(canMoveUp()) direction.value = 3
        else queuedDirection.value = 3
    }
    // change direction to down (for pacman controls)
    def changeDirectionDown(): Unit = {
        if(canMoveDown()) direction.value = 4
        else queuedDirection.value = 4
    }
    // move in primary pacman direction
    def pacmanMoveDirection(): Boolean = {
        direction.value match {
            case 1 => moveLeft()
            case 2 => moveRight()
            case 3 => moveUp()
            case 4 => moveDown()
            case _ => false
        }
    }
    // move in queued pacman direction
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
