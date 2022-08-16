import algorithms.BuildingBlocks._
import Direction._

class BuildingBlocksSuite extends munit.FunSuite {
    test("getEmptyGrid test"){
        val empty: Array[Array[Set[Direction]]] = Array()
        assert(getEmptyGrid(0,0).isEmpty)
        assert(getEmptyGrid(5,0).isEmpty)
        assert(getEmptyGrid(0,3).forall(_.isEmpty))
        assert(getEmptyGrid(3,3).forall(_.forall(_.isEmpty)))
        assert(getEmptyGrid(3,5).size == 5)
        assert(getEmptyGrid(3,5)(0).size == 3)
    }
    test("isWithin tests"){
        assert(isWithin(Point(3,4),(4,5)) == true)
        assert(isWithin(Point(-3,4),(1,1)) == false)
        assert(isWithin(Point(0,-1),(0,0)) == false)
        assert(isWithin(Point(0,0),(1,1)) == true)
        assert(isWithin(Point(3,14),(4,5)) == false)
    }
    test("gridToArray tests"){
        assert(gridToArray(Point(3,4), (5,6)) == 23)
        assert(gridToArray(Point(0,0), (1,1)) == 0)
        assert(gridToArray(Point(2,0), (15,11)) == 2)
        assert(gridToArray(Point(0,4), (15,11)) == 60)
        intercept[IllegalArgumentException](gridToArray(Point(4,4),(2,4)))
    }
    test("arrayToGrid tests"){
        assert(arrayToGrid(64, (15,11)) == Point(4,4))
        assert(arrayToGrid(0, (5,1)) == Point(0,0))
        assert(arrayToGrid(1, (2,1)) == Point(1,0))
        assert(arrayToGrid(30, (15,3)) == Point(0,2))
        intercept[IllegalArgumentException](arrayToGrid(12,(3,4)))
    }
    test("arrayToGrid and gridToArray are inverses"){
        assert(arrayToGrid(gridToArray(Point(11,17),(23,25)),(23,25)) == Point(11,17))
        assert(gridToArray(arrayToGrid(63, (7,43)), (7,43)) == 63)
    }
    test("opposite works"){
        assert(opposite(N) == S)
        assert(opposite(E) == W)
        assert(opposite(S) == N)
        assert(opposite(W) == E)
    }
    test("move works"){
        assert(move(Point(3,4),N) == Point(3,5))
        assert(move(Point(0,0),E) == Point(1,0))
        assert(move(Point(1,1),S) == Point(1,0))
        assert(move(Point(13,4),W) == Point(12,4))
    }
}