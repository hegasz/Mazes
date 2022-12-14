package algorithms

import controller.GameController
import BuildingBlocks._
import Direction._
import scala.util.Random


object Kruskal{

    /** Generates a random maze using Kruskal's algorithm.
     *  @param size maze dimensions as (width, height)
     *  @return a random maze generated by Kruskal's algorithm */
    def KruskalMazeBuilder(size: Size): Maze = {

        if(GameController.cameraOn){
            GameController.constructionProcess.clear()
        }

        val grid: Grid = getEmptyGrid(size._1,size._2)
        val numCells: Int = size._1*size._2

        val wallsArray: Array[Wall] = new Array[Wall](numCells * 2)
        
        // Add N and W walls for every point in grid to an wallsArray
        var index = 0
        for(i <- 0 until size._1){
            for(j <- 0 until size._2){
                var wallN: Wall = (Point(i,j),N)
                var wallW: Wall = (Point(i,j),W)
                wallsArray(index) = wallN
                wallsArray(index+1) = wallW
                index += 2
            }
        }

        // randomly shuffle wallsArray
        val shuffledCells = scala.util.Random.shuffle(wallsArray)

        val sets = new DisjointSetsFixedElems(numCells)

        // basically popping walls off front of array (but by traversing it)
        for(index <- 0 until numCells*2){
            var wall: Wall = shuffledCells(index)
            var point1: Point = wall._1
            var direction: Direction = wall._2
            // the point on other side of this wall 
            var point2: Point = move(point1, direction)

            if(isWithin(point2, size)){
                // convert point coordinates to unique numbering
                // to be able to use more efficient disjoint set
                // implementation
                var id1 = gridToArray(point1, size)
                var id2 = gridToArray(point2, size)
                // if points are not in the same set
                if(sets.find(id1) != sets.find(id2)){
                    // combine the two points' sets
                    sets.union(id1, id2)
                    // break down this wall by adding accessibility
                    // between the two points into the grid
                    grid(point1.y)(point1.x) += direction
                    grid(point2.y)(point2.x) += opposite(direction)
                    if(GameController.cameraOn){
                        // import to map clone so we do not enqueue a reference
                        // to the same grid.
                        GameController.constructionProcess.enqueue(grid.map(_.clone))
                    }
                }
            }  
        }
        val maze: Maze = Maze(grid, size)
        return maze
    }
}