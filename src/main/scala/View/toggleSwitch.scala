package view

import scalafx.Includes._
import javafx.event.Event;

import scalafx.geometry.Pos;
import scalafx.scene.control.Button;
import scalafx.scene.layout.StackPane;
import scalafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.event.EventHandler
import scalafx.beans.property.BooleanProperty.apply
import scalafx.beans.property.BooleanProperty

class ToggleSwitch(onColour: String, offColour: String, thumbColour: String){
    val back: Rectangle = new Rectangle(30, 10, Color.Red)
    val button: Button = new Button()
    val buttonStyleOff: String = s"-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 0.2, 0.0, 0.0, 2); -fx-background-color: #$thumbColour;"
    val buttonStyleOn: String = s"-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 0.2, 0.0, 0.0, 2); -fx-background-color: #$thumbColour;"
    var state = BooleanProperty(false)
    var switch: StackPane = new StackPane()

    switch.getChildren().addAll(back, button)
    switch.setMinSize(30, 15)
    switch.setMaxSize(30, 15)
    back.maxWidth(30)
    back.minWidth(30)
    back.maxHeight(10)
    back.minHeight(10)
    back.setArcHeight(back.getHeight())
    back.setArcWidth(back.getHeight())
    back.setFill(Color.valueOf(s"#$offColour"))
    val r: Double = 2.0;
    button.setShape(new Circle(r));
    StackPane.setAlignment(button, Pos.CenterLeft);
    button.setMaxSize(15, 15);
    button.setMinSize(15, 15);
    button.setStyle(buttonStyleOff);

    val click: EventHandler[Event] = new EventHandler[Event]() {
            override def handle(e: Event): Unit = {
                if (state.value) {
                    button.setStyle(buttonStyleOff)
                    back.setFill(Color.valueOf(s"#$offColour"))
                    StackPane.setAlignment(button, Pos.CenterLeft)
                    state.value = false
                } else {
                    button.setStyle(buttonStyleOn)
                    back.setFill(Color.valueOf(s"#$onColour"))
                    StackPane.setAlignment(button, Pos.CenterRight)
                    state.value = true
                }
            }
        }

        button.setFocusTraversable(false)
        switch.setOnMouseClicked(click)
        button.setOnMouseClicked(click)
    }

    
