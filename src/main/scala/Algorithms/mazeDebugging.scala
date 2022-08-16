package algorithms

import buildingBlocks._
import Direction._

/** Some functions for helping debug maze problems,
 *  can be deleted if not needed. */
object MazeDebugging {
    def mazeToString(maze: Maze): String = {
        var string = ""
        val size = maze.size
        val grid = maze.grid
        val width = size._1; val height = size._2
        for(y <- height-1 to 0 by -1){
            for(x <- 0 until width){
                if(!(grid(y)(x) contains N)) string += "+---"
                else string += "+   "
            }
            string += "+"
            string += "\n"
            for(x <- 0 until width){
                if(!(grid(y)(x) contains W)) string += "|   "
                else string += "    "
            }
            string += "|"
            string += "\n"
        }
        for(x <- 0 until width) string += "+---"
        string += "+"
        return string
    }
}
