package view

import algorithms.buildingBlocks._
import scalafx.scene.canvas.Canvas

object MenuController{
    var size: Size = (10,10)
    var miniGridBorderThicknessMultiplier: Int = 5
    val boxSize: Double = 200
    var miniGrid = GridCanvas(size, Maze(getEmptyGrid(size._1,size._2),size), boxSize, borderThicknessMultiplier = miniGridBorderThicknessMultiplier)

    def setMiniGridSize(width: Int, height: Int): Unit = {
        val newSize: Size = (width, height)
        size = newSize
        miniGrid = GridCanvas(size, Maze(getEmptyGrid(size._1,size._2),size), boxSize, borderThicknessMultiplier = miniGridBorderThicknessMultiplier)
    }

    def getMiniGridCanvas(): Canvas = miniGrid.canvas
    

}
