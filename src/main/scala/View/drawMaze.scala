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


object DrawMaze extends JFXApp3 {
    import scala.concurrent.ExecutionContext.Implicits.global
    def gameLoop(update: () => Unit): Unit =
        Future {
        update()
        Thread.sleep(80)
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

