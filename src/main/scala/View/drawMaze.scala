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
import scalafx.scene.layout.AnchorPane
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.HBox
import scalafx.scene.layout.VBox
import scalafx.scene.layout.StackPane
import scalafx.scene.layout.TilePane
import scalafx.scene.paint.Color._
import javafx.beans.value.ObservableValue
import javafx.beans.value.ChangeListener
import scalafx.beans.property.{IntegerProperty, ObjectProperty, DoubleProperty}
import scala.concurrent.Future
import scalafx.scene.text.Text
import scalafx.scene.paint.LinearGradient
import scalafx.scene.paint.Stops
import scalafx.scene.effect.DropShadow
import javafx.geometry.Pos
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.Button




object DrawMaze extends JFXApp3 {
    import scala.concurrent.ExecutionContext.Implicits.global
    def gameLoop(update: () => Unit): Unit =
        Future {
        update()
        Thread.sleep(1000 / 25 * 2)
        }.flatMap(_ => Future(gameLoop(update)))

    override def start(): Unit = {

        val size: Size = (5,9)
        val maze: Maze = RecursiveBacktrackingMazeBuilder(size)
        val gridState = ObjectProperty(GridState(size, maze, 900))
        // ratio of screen filled up by central SQUARE box
        // decided by whichever of height and width is limiting
        val mazeBoxRatio: Double = 0.7 // require this is between 0 and 1
        
        val frame = IntegerProperty(0)
        val screenDimensions = ObjectProperty(ScreenDimensions(900,700))

        frame.onChange {
            screenDimensions.update(ScreenDimensions(stage.getScene().getWidth(),stage.getScene().getHeight()))
        }

        
        stage = new JFXApp3.PrimaryStage

        val gameScene: Scene = new Scene(800,600) {
            val borderPane: BorderPane = new BorderPane()
            val topBox: HBox = new HBox()
            topBox.setStyle("-fx-background-color: red;")
            val homeButton: Button = new Button("home")
            
            topBox.getChildren().add(homeButton)
            val bottomBox: HBox = new HBox()
            bottomBox.setStyle("-fx-background-color: green;")
            val leftBox: VBox = new VBox()
            leftBox.setStyle("-fx-background-color: blue;")
            val rightBox: VBox = new VBox()
            rightBox.setStyle("-fx-background-color: yellow;")
            val centerBox: Pane = new Pane()
            centerBox.setStyle("-fx-background-color: purple;")
            centerBox.getChildren().add(gridState.value.canvas)

            borderPane.setTop(topBox)
            borderPane.setBottom(bottomBox)
            borderPane.setLeft(leftBox)
            borderPane.setRight(rightBox)
            borderPane.setCenter(centerBox)
        
            content = borderPane

            // Resize grid and borders
            screenDimensions.onChange(Platform.runLater {
                var limitingSize = screenDimensions.value.width.min(screenDimensions.value.height)
                var boxSize = limitingSize * mazeBoxRatio

                gridState.update(GridState(size, maze, boxSize))
        
                var borderHeight: Double = 0; var borderWidth: Double = 0
                
                centerBox.getChildren().clear()
                centerBox.getChildren().add(gridState.value.canvas)

                centerBox.setMinWidth(boxSize)
                centerBox.setMinHeight(boxSize)
                centerBox.setMaxWidth(boxSize)
                centerBox.setMaxHeight(boxSize)

                borderHeight = (this.getHeight()-boxSize)/2
                borderWidth = (this.getWidth()-boxSize)/2

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

        val tempScene: Scene = new Scene(800,600)

        SceneController.scene = tempScene
        SceneController.switchToMenu()

        stage.setScene(tempScene)
        stage.show()

        gameLoop(() => frame.update(frame.value + 1))
        }
    }

