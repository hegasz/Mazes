package Buildin

object buildingBlocks{

    sealed trait Direction

    object Direction{
        case object N extends Direction
        case object E extends Direction
        case object S extends Direction
        case object W extends Direction
    }

    import Direction._

    type Point = (Int, Int)
    type Wall = (Point, Direction)
    type Size = (Int, Int)

    
    def opposite(d: Direction): Direction = d match{
        case Direction.N => Direction.S
        case Direction.E => Direction.W
        case Direction.S => Direction.N
        case Direction.W => Direction.E
    }
    
    def move(p: Point, d: Direction): Point = d match{
        case Direction.N => (p._1, p._2+1)
        case Direction.E => (p._1+1, p._2)
        case Direction.S => (p._1, p._2-1)
        case Direction.W => (p._1-1, p._2)
    }
    
}

  
