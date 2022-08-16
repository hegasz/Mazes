package algorithms

import controller.GameController
import BuildingBlocks._
import Direction._
import scala.util.Random

object BinaryTree{

    /** Generates a random maze using a binary tree.
     *  @param size maze dimensions as (width, height)
     *  @return a random binary tree maze */
    def BinaryTreeMazeBuilder(size: Size): Maze = {

        if(GameController.cameraOn){
            GameController.constructionProcess.clear()
        }

        val grid: Grid = getEmptyGrid(size._1,size._2)
        val directions: Array[Direction] = Array(N,E)
        for(j <- 0 until size._2-1){
            for(i <- 0 until size._1){
                if(i == size._1-1){
                    // right side cells must go north
                    grid(j)(i) += N
                    grid(j+1)(i) += S
                    if(GameController.cameraOn){
                        // import to map clone so we do not enqueue a reference
                        // to the same grid.
                        GameController.constructionProcess.enqueue(grid.map(_.clone))
                    }
                }
                else{
                    // randomly pick either east or north and break that wall
                    var randomDirection = Random.shuffle(directions).head
                    var point1: Point = Point(i,j)
                    var point2: Point = move(point1, randomDirection)
                    grid(point1.y)(point1.x) += randomDirection
                    grid(point2.y)(point2.x) += opposite(randomDirection)
                    if(GameController.cameraOn){
                        GameController.constructionProcess.enqueue(grid.map(_.clone))
                    }
                }
            }
        }
        for(i <- 0 until size._1-1){
            // top cells must go east
            grid(size._2-1)(i) += E
            grid(size._2-1)(i+1) += W
            if(GameController.cameraOn){
                GameController.constructionProcess.enqueue(grid.map(_.clone))
            }
        }
        val maze: Maze = Maze(grid, size)
        return maze
    }
}
