import algorithms.DisjointSetsFixedElems

class disjointSetsSuite extends munit.FunSuite {
    test("each element is root of its own tree initially"){
        val sets = new DisjointSetsFixedElems(5)
        for(i <- 0 until 5) assert(sets.find(i) == i)
    }

    test("path compression is taking place"){
        val sets = new DisjointSetsFixedElems(4)
        sets.union(0,1)
        sets.union(1,2)
        assert(sets.find(2) == 0)
        assert(sets.find(3) == 3)
        sets.union(2,3)
        assert(sets.find(3) == 0)
    }

    test("union is occuring by rank"){
        val sets = new DisjointSetsFixedElems(5)
        sets.union(0,1)
        sets.union(0,2)
        sets.union(3,4)
        assert(sets.find(2) == 0)
        assert(sets.find(4) == 3)
        sets.union(0,4)
        assert(sets.find(4) == 0)
    }

    test("out of bound indices throw illegal argument exceptions"){
        val sets = new DisjointSetsFixedElems(4)
        intercept[IllegalArgumentException]{sets.find(-2)}
        intercept[IllegalArgumentException]{sets.find(4)}
        intercept[IllegalArgumentException]{sets.union(-1,2)}
        intercept[IllegalArgumentException]{sets.union(3,-2)}
        intercept[IllegalArgumentException]{sets.union(4,0)}
        intercept[IllegalArgumentException]{sets.union(1,7)}
    }
}