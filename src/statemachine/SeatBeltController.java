package statemachine;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

enum State {
    SEATED, IDLE, BELTED, BUZZER
}

public class SeatBeltController implements Initializable{
    
    private AudioClip buzzer;
    
    private Timeline timer;
    
    private final double timeout = 30000.0;
    
    private double timeElipsed = 0;

    @FXML
    private Button seat;

    @FXML
    private Button unseat;

    @FXML
    private Button belt;
    
    @FXML
    private Label state;

    @FXML
    private ProgressBar timerProgress;


    @FXML
    void seated(ActionEvent event) {
        state.setText(State.SEATED.name());
        // start 30 second timer.
        timer.playFromStart();
        // set buttons.
        seat.setDisable(true);
        belt.setDisable(false);
        unseat.setDisable(false);
    }

    @FXML
    void unseated(ActionEvent event) {
        state.setText(State.IDLE.name());
        // Disable timer
        stopTimer();
        // set buttons
        seat.setDisable(false);
        belt.setDisable(true);
        unseat.setDisable(true);
    }
    
    @FXML
    void belted(ActionEvent event) {
        state.setText(State.BELTED.name());
        // Disable timer
        stopTimer();
        // set timers
        belt.setDisable(true);
        unseat.setDisable(true);
        seat.setDisable(false);
    }
    
    private void stopTimer(){
        if(timer != null)
            timer.stop();
        timeElipsed = 0;
        buzzer.stop();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buzzer = new AudioClip(getClass().getResource("buzzer.mp3").toExternalForm());
        state.setText(State.IDLE.name());
        
        timer = new Timeline(new KeyFrame(Duration.millis(10), e->{
            timeElipsed += 10.0;
            timerProgress.setProgress(timeElipsed/timeout);
            if(timeElipsed >= timeout){
                timer.stop();
                buzzer.play();
                state.setText(State.BUZZER.name());
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
    }
}
