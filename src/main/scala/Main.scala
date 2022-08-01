package Buildin

import Buildin.buildingBlocks._


object Main extends App{
    println("heello")

    val b: Point = (3,4)
    val d = Direction.withName("N")
    println(move(b,opposite(d)))
    
}
