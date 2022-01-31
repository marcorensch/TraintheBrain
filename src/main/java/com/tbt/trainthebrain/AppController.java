package com.tbt.trainthebrain;

import com.tbt.trainthebrain.sqlcontroller.DBTasks;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;

public class AppController {

    boolean statusDb;
    public ArrayList<Question> questions = new ArrayList<>();

    /**
     * Initiates a Database call that get an <code>ArrayList</code> of all <code>Question</code> Elements from database including its <code>Answer</code> objects
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     ArrayList
     * @see     Question
     * @return  <code>Arraylist</code> holding all <code>Question</code> objects including the related <code>Answer</code> objects
     */
    public ArrayList<Question> getQuestionsFromDatabase() {
        DBTasks con = new DBTasks();
        return con.getAllQuestionsFromDb();
    }

    /**
     * Click handler that initiates a switch to the main view
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     Scene
     * @see     Stage
     * @see     #switchBasicScenes(Stage, String)
     * @param   actionEvent The action event that initiates the scene switch
     */
    public void backToMainClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getTarget()).getScene().getWindow();
        switchBasicScenes(stage,"main.fxml" );
    }

    /**
     * Click handler that initiates a switch to the learnmode setup view
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     Scene
     * @see     Stage
     * @see     #switchBasicScenes(Stage, String)
     * @param   actionEvent The action event that initiates the scene switch
     */
    public void switchToLearningModeClick(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getTarget()).getScene().getWindow();
        switchBasicScenes(stage,"learnmode-setup.fxml" );
    }

    /**
     * Click handler that initiates a switch to the question edit view
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     Scene
     * @see     Stage
     * @see     #switchBasicScenes(Stage, String)
     * @param   actionEvent The action event that initiates the scene switch
     */
    public void addNewQuestionClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getTarget()).getScene().getWindow();
        switchBasicScenes(stage,"question-edit.fxml" );
    }

    /**
     * Click handler that initiates a switch to the question edit overview view
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     Scene
     * @see     Stage
     * @see     #switchBasicScenes(Stage, String)
     * @param   actionEvent The action event that initiates the scene switch
     */
    public void switchToEditOverviewClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getTarget()).getScene().getWindow();
        switchBasicScenes(stage,"question-edit-overview.fxml" );
    }

    /**
     * Method that does the effective scene switch with no additional data
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     FXMLLoader
     * @see     Stage
     * @see     Scene
     * @param   stage       <code>Stage</code> object that should be used to place the new scene
     * @param   fxmlName    <code>String</code> that contains the fxml filename that should be loaded in the new scene
     */
    private void switchBasicScenes(Stage stage, String fxmlName){
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlName));
        try {
            Scene questioningScene = new Scene(loader.load());
            stage.setScene(questioningScene);
        }catch (IOException ioe){
            System.out.println("Szene konnte nicht geladen werden: " + fxmlName);
            ioe.printStackTrace();
        }
    }

    /**
     * Helper Method that indicates a fade in transition for a given node
     *
     * @author  Marco Rensch
     * @since   1.0
     * @see     Node
     * @see     Integer
     * @param   element     The <code>Node</code> element that should be animated
     * @param   duration    <code>int</code> The transition time in milliseconds
     * @param   delay       <code>int</code> The delay for this transition in milliseconds
     */
    public void fadeInTransition(Node element, int duration, int delay){
        element.setOpacity(0);
        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.millis(duration));
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setCycleCount(1);
        fade.setAutoReverse(false);
        fade.setNode(element);
        if(delay > 0 ) {
            fade.setDelay(Duration.millis(delay));
        }
        fade.play();
    }
}
