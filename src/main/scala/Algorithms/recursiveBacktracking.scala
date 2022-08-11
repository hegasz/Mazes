package algorithms

import buildingBlocks._
import Direction._
import scala.util.Random

import mazeDebugging.mazeToString

object RecursiveBacktracking{

    /** Generates a random maze using recursive backtracking.
     *  @param size maze dimensions as (width, height)
     *  @return a random maze generated by recursive backtracking */
    def RecursiveBacktrackingMazeBuilder(size: Size): Maze = {

        // note inverted x and y in tabulate call
        val grid: Grid = Array.tabulate[Set[Direction]](size._2,size._1)((_,_) => Set())

        val stack = scala.collection.mutable.Stack[Point]()
        val directions: Array[Direction] = Array(N,E,S,W)

        stack.push(Point(0,0))

        while(!stack.isEmpty){
            var point1 = stack.pop()
            var randomDirections = scala.util.Random.shuffle(directions)

            // if cell has an unvisited neighbour, find it
            // and store it and the direction to it in the
            // appropriately named variables.
            var foundUnvisitedNeighbour = false
            var unvisitedNeighbour: Point = null
            var unvisitedDirection: Direction = null
            for(direction <- randomDirections){
                if(!foundUnvisitedNeighbour){
                    var point2 = move(point1, direction)
                    // if point is valid and has not been visited yet
                    if(isWithin(point2, size) && grid(point2.y)(point2.x).isEmpty){
                        foundUnvisitedNeighbour = true
                        unvisitedNeighbour = point2
                        unvisitedDirection = direction
                    }
                }
            }

            // do nothing if cell didn't have an unvisited neighbour,
            // but if it did then break down the appropriate wall,
            // then add the original cell back to the stack and 
            // its neighbour we just visited onto the stack above it.
            if(foundUnvisitedNeighbour){
                grid(point1.y)(point1.x) += unvisitedDirection
                grid(unvisitedNeighbour.y)(unvisitedNeighbour.x) += opposite(unvisitedDirection)
                stack.push(point1)
                stack.push(unvisitedNeighbour)
            }
        }
        val maze: Maze = Maze(grid, size)
        return maze
    }
}
