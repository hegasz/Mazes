package algorithms

/** Basic types and functions defining how mazes are constructed. */
object buildingBlocks{

    sealed trait Direction

    object Direction{
        case object N extends Direction
        case object E extends Direction
        case object S extends Direction
        case object W extends Direction
    }

    import Direction._

    /** Points in the grid consist of an x and a y coordinate,
     *  both starting from 0. */
    case class Point(val x: Int,  val y: Int)

    type Wall = (Point, Direction)

    type Size = (Int, Int)

    // 2D array representing all points in grid, storing set of direction
    // that ARE ACCESSIBLE from that point (not direction of walls). Note
    // you must call grid(y)(x), in the order opposite to usual coordinate use.
    // (0,0) is bottom left.
    type Grid = Array[Array[Set[Direction]]]

    def getEmptyGrid(numCols: Int, numRows: Int): Grid = {
        // note inverted x and y in tabulate call
        val grid: Grid = Array.tabulate[Set[Direction]](numRows,numCols)((_,_) => Set())
        return grid
    }

    case class Maze(grid: Grid, size: Size)

    /** Assigns an index to each grid point, starting from bottom left
     * and scanning left to right. (0,0) has index 0. */
    def gridToArray(point: Point, size: Size): Int = {
        require(isWithin(point,size))
        return point.y*size._1 + point.x
    }

    /** Converts from unique grid number back to point coordinates. */
    def arrayToGrid(index: Int, size: Size): Point = {
        require(index < size._1 * size._2)
        return Point(index % size._1, index / size._1)
    }

    /** Returns whether point is within size boundaries. */
    def isWithin(point: Point, size: Size) = {
        point.x >= 0 && point.y >= 0 && point.x < size._1 && point.y < size._2
    }

    def opposite(d: Direction): Direction = d match{
        case Direction.N => Direction.S
        case Direction.E => Direction.W
        case Direction.S => Direction.N
        case Direction.W => Direction.E
    }
    
    def move(p: Point, d: Direction): Point = d match{
        case Direction.N => Point(p.x, p.y+1)
        case Direction.E => Point(p.x+1, p.y)
        case Direction.S => Point(p.x, p.y-1)
        case Direction.W => Point(p.x-1, p.y)
    }
}

  
