package view

import algorithms.buildingBlocks._
import algorithms.RecursiveBacktracking._
import scalafx.beans.property.ObjectProperty

object GameController{
    val size: Size = (5,9)
    val maze: Maze = RecursiveBacktrackingMazeBuilder(size)
    val gridState = ObjectProperty(GridState(size, maze, 900))
    val mazeBoxRatio: Double = 0.7 // require this is between 0 and 1

    def reSize(width: Double, height: Double): Unit = {
        var limitingSize = width.min(height)
        var boxSize = limitingSize * mazeBoxRatio
        gridState.update(GridState(size, maze, boxSize))
        GamePane.reSize(width, height, boxSize)
    }

    def getGridState(): ObjectProperty[GridState] = gridState

    def getMazeBoxRatio(): Double = mazeBoxRatio
}
