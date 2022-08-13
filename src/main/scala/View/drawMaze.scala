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
import scalafx.application.{JFXApp3, Platform}
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.MenuBar
import scalafx.scene.control.Menu
import scalafx.scene.layout.BorderPane
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.HBox
import scalafx.scene.layout.VBox
import scalafx.scene.layout.StackPane
import scalafx.scene.layout.TilePane
import scalafx.scene.paint.Color._
import javafx.beans.value.ObservableValue
import javafx.beans.value.ChangeListener
import scalafx.beans.property.{IntegerProperty, ObjectProperty}
import scala.concurrent.Future
import scalafx.scene.text.Text
import scalafx.scene.paint.LinearGradient
import scalafx.scene.paint.Stops
import scalafx.scene.effect.DropShadow
import javafx.geometry.Pos


object DrawMaze extends JFXApp3 {
    import scala.concurrent.ExecutionContext.Implicits.global
    def gameLoop(update: () => Unit): Unit =
        Future {
        update()
        Thread.sleep(1000 / 25 * 2)
        }.flatMap(_ => Future(gameLoop(update)))

    override def start(): Unit = {

        val size: Size = (5,30)
        val maze: Maze = RecursiveBacktrackingMazeBuilder(size)
        val gridState = ObjectProperty(new GridDrawing(size, maze, 900,700))


        // ratio of screen filled up by central SQUARE box
        // decided by whichever of height and width is limiting
        val mazeBoxRatio: Double = 0.7 // require this is between 0 and 1
        

        val frame = IntegerProperty(0)
        val direction = IntegerProperty(4) // right

        
        //gridState.update(new GridDrawing(size, maze, this.getWidth().toInt, this.getHeight.toInt))

        stage = new JFXApp3.PrimaryStage {
            title = "Mazes"

            scene = new Scene(800,600) {
                
                val borderPane: BorderPane = new BorderPane()
                val topBox: HBox = new HBox()
                topBox.setStyle("-fx-background-color: red;")
                val bottomBox: HBox = new HBox()
                bottomBox.setStyle("-fx-background-color: green;")
                val leftBox: VBox = new VBox()
                leftBox.setStyle("-fx-background-color: blue;")
                val rightBox: VBox = new VBox()
                rightBox.setStyle("-fx-background-color: yellow;")
                val centerBox: StackPane = new StackPane()
                centerBox.setStyle("-fx-background-color: purple;")
                centerBox.getChildren().add(gridState.value.grid)
                //centerBox.getChildren().add(new Rectangle(100,400))
                //centerBox.setAlignment(Pos.BOTTOM_CENTER)
                centerBox.setStyle("-fx-border-color: blue ; -fx-border-width: 5")

                borderPane.setTop(topBox)
                borderPane.setBottom(bottomBox)
                borderPane.setLeft(leftBox)
                borderPane.setRight(rightBox)
                borderPane.setCenter(centerBox)
                

                content = borderPane

                gridState.onChange(Platform.runLater {
                    val boxSize = gridState.value.boxSize

                    var borderHeight: Double = 0; var borderWidth: Double = 0
                    
                    centerBox.getChildren().clear()
                    centerBox.getChildren().add(gridState.value.grid)
                    //centerBox.getChildren().add(new Rectangle(boxSize/2,boxSize/3))

                    centerBox.setMinWidth(boxSize)
                    centerBox.setMinHeight(boxSize)
                    centerBox.setMaxWidth(boxSize)
                    centerBox.setMaxHeight(boxSize)

                    borderHeight = (this.getHeight()-boxSize)/2
                    borderWidth = (this.getWidth()-boxSize)/2

                    /**
                    if(gridWidth < gridHeight){ // width limiting factor
                        borderHeight = (this.getHeight()-minGridVal)/2
                        borderWidth = this.getWidth()*borderWidthRatio
                    }

                    else { // height limiting factor
                        borderHeight = this.getHeight()*borderHeightRatio
                        borderWidth = (this.getWidth()-minGridVal)/2
                    }*/

                    topBox.setMinHeight(borderHeight)
                    bottomBox.setMinHeight(borderHeight)
                    leftBox.setMinWidth(borderWidth)
                    rightBox.setMinWidth(borderWidth)
                    topBox.setMaxHeight(borderHeight)
                    bottomBox.setMaxHeight(borderHeight)
                    leftBox.setMaxWidth(borderWidth)
                    rightBox.setMaxWidth(borderWidth)    

                    borderPane.setCenter(centerBox)
                    content = borderPane
                })
            }

            frame.onChange {
                var limitingSize = stage.getWidth().min(stage.getHeight())
                var limitingSizeScaled = limitingSize * mazeBoxRatio
                gridState.update(new GridDrawing(size, maze, limitingSizeScaled))
            }

            gameLoop(() => frame.update(frame.value + 1))
        }

        //val stageSizeListener: ChangeListener[Number] = (observable, oldValue, newValue) => {
            //println("Height: " + stage.getHeight() + " Width: " + stage.getWidth())
            //gridState.update(gridState.value.drawPlainGrid())
        //}
        //stage.widthProperty().onChange{
        //    println(size)
        //    println("Height: " + stage.getHeight() + " Width: " + stage.getWidth())
        //    gridState.update(gridState.value.drawPlainGrid())
        //}


        //stage.widthProperty().addListener(stageSizeListener);
        //stage.heightProperty().addListener(stageSizeListener); 

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