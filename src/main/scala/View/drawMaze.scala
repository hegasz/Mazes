package view

import algorithms.Kruskal._
import algorithms.RecursiveBacktracking._
import algorithms.buildingBlocks._
import algorithms.mazeDebugging._
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.Pane
import javafx.scene.layout.RowConstraints
import javafx.scene.paint.Color
import javafx.scene.shape._
import javafx.scene.transform.Scale
import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.MenuBar
import scalafx.scene.control.Menu
import scalafx.scene.layout.BorderPane
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.HBox
import scalafx.scene.layout.TilePane
import scalafx.scene.paint.Color._
import javafx.beans.value.ObservableValue
import javafx.beans.value.ChangeListener
import scalafx.beans.property.{IntegerProperty, ObjectProperty}

object DrawMaze extends JFXApp3 {

    override def start(): Unit = {
        stage = new JFXApp3.PrimaryStage {
            title = "Mazes"

            val size: Size = (5,4)
            val gridState = ObjectProperty(new GridDrawing(size, 900, 700))

            scene = new Scene(800,600) {

                val menuBar = new MenuBar
                val fileMenu = new Menu("File")
                menuBar.menus = List(fileMenu)

                val drawing = gridState.value
                val r: GridPane = drawing.grid
                drawing.initialiseGrid()
                val maze: Maze = RecursiveBacktrackingMazeBuilder(size)
                drawing.drawMazeGrid(maze)
                
                val rootPane = new BorderPane
                rootPane.top = menuBar
                rootPane.center = r
                root = rootPane

            
            }
        }

        val stageSizeListener: ChangeListener[Number] = (observable, oldValue, newValue) => {
            //println("Height: " + stage.getHeight() + " Width: " + stage.getWidth())
            //gridState.update(gridState.value.drawPlainGrid())
        }
        stage.widthProperty().onChange {println("Height: " + stage.getHeight() + " Width: " + stage.getWidth())}


        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener); 

    }
}

//fill = LightGreen
                
                //val size: Size = (5,4)
                //val drawing = new GridDrawing(size, 900, 700)
                //val r: GridPane = drawing.grid
                //drawing.initialiseGrid()

                //val maze: Maze = KruskalMazeBuilder(size)

                //val maze: Maze = RecursiveBacktrackingMazeBuilder(size)
                //println(mazeToString(maze))
                //drawing.drawMazeGrid(maze)