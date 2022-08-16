package algorithms

/** Interface for disjoint set date structure */
trait DisjointSets[T]{

    // pre: x is in the collection of sets
    // post: returns x's set representative
    def find(x : T): T

    // pre: x and y are both in the collection of sets
    // post: the sets x and y are part of are merged into one
    def union(x: T, y: T): Unit

}

/** Implements disjoint set data structure where the elements
 *  of the collection of sets are the integers from 0 to n
 *  inclusive, without gaps. */
class DisjointSetsFixedElems(n: Int) extends DisjointSets[Int]{

    /** Abstraction: 
     *  We store each disjoint set as a tree in
     *  a forest. The `parent` array stores the parent of value
     *  i in its tree as parent(i). The root of a tree has itself
     *  as its parent. The `rank` array stores the rank of value
     *  i as rank(i) - this is an upper bound on its height, used
     *  to create flatter trees when merging.
     *  The common optimisations of path compression and union
     *  by rank are used in `find` and `union` respectively. */
    

    private val parent: Array[Int] = (0 until n).toArray
    private val rank = new Array[Int](n)

    // pre: 0 <= elem < n
    // post: returns root of tree elem is part of
    def find(elem : Int): Int = {

        require(elem >= 0 && elem < n)
        var x = elem
        var y = x

        while(parent(y) != y){
            y = parent(y)
        }
        // y is now the root of x's tree
        while(parent(x) != y){
            var temp = parent(x)
            parent(x) = y // set x's parent to be the root
            x = parent(x) // move x one up the tree
        }
        return y
    }

    // pre: 0 <= elem1, elem2 < n
    // post: elem1 and elem2 are part of one merged tree
    def union(elem1: Int, elem2: Int): Unit = {

        require(elem1 >= 0 && elem2 >= 0 && elem1 < n && elem2 < n)
        val x = parent(elem1)
        val y = parent(elem2)

        if(x != y){
            if(rank(x) < rank(y)){ 
                parent(x) = y
            }
            else if(rank(x) > rank(y)){
                parent(y) = x
            }
            else{ // x and y have same rank 
                // so arbitrarily put y under x and increase x's rank
                parent(y) = x
                rank(x) += 1
            }
        }
    }
}



// PEOPLE WANT A WORKING VERSION OF THIS
// SO YOU COULD MAYBE TRY TO WORK ON IT MORE
// AND ADD IT TO A GITHUB LIBRARY