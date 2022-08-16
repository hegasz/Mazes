package view


import controller.GameController
import controller.SceneController
import controller.ScreenDimensions
import scalafx.application.JFXApp3
import scalafx.application.Platform
import scalafx.beans.property.IntegerProperty
import scalafx.beans.property.ObjectProperty
import scalafx.scene.Scene
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


/**
  *  Main GUI file
  */
object DrawMaze extends JFXApp3 {
    
    // ticks to make the game run
    def gameLoop(update: () => Unit): Unit = Future {
            update()
            Thread.sleep(80)
        }.flatMap(_ => Future(gameLoop(update)))

    override def start(): Unit = {

        val frame = IntegerProperty(0)
        val screenDimensions = ObjectProperty(ScreenDimensions(900,700))
        // run on every tick of game clock
        frame.onChange {
            screenDimensions.update(ScreenDimensions(stage.getScene().getWidth(),stage.getScene().getHeight()))
            GameController.clockTick(frame.value)
        }
        // resize if screen dimensions have changed
        screenDimensions.onChange(Platform.runLater {
            GameController.reSize(screenDimensions.value.width, screenDimensions.value.height)
        })

        stage = new JFXApp3.PrimaryStage
        // create new scene with dimensions shown
        val scene: Scene = new Scene(800,600)

        SceneController.setScene(scene)
        SceneController.switchToMenu()
       
        stage.setScene(scene)
        stage.show()

        gameLoop(() => frame.update(frame.value + 1))
        }
    }

