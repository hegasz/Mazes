import algorithms.MazeDebugging._
import algorithms.BuildingBlocks._
import Direction._

class mazeDebuggingSuite extends munit.FunSuite {
    test("mazeToString test 1"){
        val testSize: Size = (3,4)
        val newValue: Set[Direction] = Set(N,W)
        val newValue2: Set[Direction] = Set(N)
        val newValue3: Set[Direction] = Set(W)
        val testGrid: Grid = Array(
            Array(Set(N,E), Set(W,E), newValue),
            Array(Set(N), newValue2, Set(S,N)),
            Array(newValue2, Set(E,S), Set(S,W)),
            Array(Set(S,E), Set(E,W), newValue3))
        val testMaze: Maze = Maze(testGrid, testSize)
        assert(mazeToString(testMaze) == """+---+---+---+
                                           #|           |
                                           #+   +---+---+
                                           #|   |       |
                                           #+   +   +   +
                                           #|   |   |   |
                                           #+   +---+   +
                                           #|           |
                                           #+---+---+---+""".stripMargin('#'))
    }
    test("mazeToString test 2"){
        val testSize: Size = (1,1)
        val newValue: Set[Direction] = Set()
        val testGrid: Grid = Array(
            Array(newValue))
        val testMaze: Maze = Maze(testGrid, testSize)
        assert(mazeToString(testMaze) == """+---+
                                           #|   |
                                           #+---+""".stripMargin('#'))
    }
}

/**
def main(args:Array[String]):Unit = {
        val testSize: Size = (3,4)
        val newValue: Set[Direction] = Set(N,W)
        val newValue2: Set[Direction] = Set(N)
        
        val testGrid: Grid = Array(
            Array(newValue, newValue, newValue),
            Array(newValue, newValue2, newValue),
            Array(newValue, newValue, newValue),
            Array(newValue, newValue, newValue2))
            
        val testMaze: Maze = Maze(testGrid, testSize)
        printMaze(KruskalMazeBuilder(7,7))
    }
*/

