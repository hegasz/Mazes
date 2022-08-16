package controller

import algorithms.BuildingBlocks._
import scalafx.scene.canvas.Canvas

object MenuController{
    var size: Size = (10,10)
    var miniGridBorderThicknessMultiplier: Int = 5
    val boxSize: Double = 200
    var miniGrid = GameStack(size, Maze(getEmptyGrid(size._1,size._2),size), boxSize, borderThicknessMultiplier = miniGridBorderThicknessMultiplier, forDisplayOnly = true)

    def setMiniGridSize(width: Int, height: Int): Unit = {
        val newSize: Size = (width, height)
        size = newSize
        miniGrid = GameStack(size, Maze(getEmptyGrid(size._1,size._2),size), boxSize, borderThicknessMultiplier = miniGridBorderThicknessMultiplier, forDisplayOnly = true)
    }

    def getMiniGridCanvas(): Canvas = miniGrid.gridCanvas
    
}
