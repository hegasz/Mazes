package controller

import algorithms.BuildingBlocks._
import scalafx.scene.canvas.Canvas

object MenuController{
    private var size: Size = (10,10)
    private var miniGridBorderThicknessMultiplier: Int = 5
    private val boxSize: Double = 200
    private var miniGrid = GameStack(size, Maze(getEmptyGrid(size._1,size._2),size), boxSize, borderThicknessMultiplier = miniGridBorderThicknessMultiplier, forDisplayOnly = true)

    // set dimensions of mini preview grid
    def setMiniGridSize(numCols: Int, numRows: Int): Unit = {
        val newSize: Size = (numCols, numRows)
        size = newSize
        miniGrid = GameStack(size, Maze(getEmptyGrid(size._1,size._2),size), boxSize, borderThicknessMultiplier = miniGridBorderThicknessMultiplier, forDisplayOnly = true)
    }
    // get the preview grid canvas
    def getMiniGridCanvas(): Canvas = miniGrid.gridCanvas
    // get the size of the minigrid's box
    def getBoxSize(): Double = boxSize
}
