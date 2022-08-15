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

        val frame = IntegerProperty(0)
        val screenDimensions = ObjectProperty(ScreenDimensions(900,700))

        frame.onChange {
            screenDimensions.update(ScreenDimensions(stage.getScene().getWidth(),stage.getScene().getHeight()))
            GameController.clockTick(frame.value)
        }

        stage = new JFXApp3.PrimaryStage

        screenDimensions.onChange(Platform.runLater {
            GameController.reSize(screenDimensions.value.width, screenDimensions.value.height)
        })

        val tempScene: Scene = new Scene(800,600)

        SceneController.scene = tempScene
        SceneController.switchToMenu()

        stage.setScene(tempScene)
        stage.show()

        gameLoop(() => frame.update(frame.value + 1))
        }
    }

