package com.tbt.trainthebrain;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LearnmodeSetupController extends AppController implements Initializable {
    @FXML
    Button counterPlusBtn, counterMinusBtn, startBtn, backBtn;
    @FXML
    TextField questionsToPlayCounter;
    @FXML
    Text countOfQuestions, howManyQuestionsText;
    @FXML
    VBox learnModeInfoContainer;
    @FXML
    HBox questionSetupOptionsContainer;

    int countOfQuestionsInDB = 0;
    int questionsCounter = 0;

    /**
     * Click Handler for scene switch including call to LearnmodeController's custom init that handles the the questions array
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     ActionEvent
     * @see     Stage
     * @see     Scene
     * @see     FXMLLoader
     * @see     LearnmodeController
     * @param   actionEvent Click event on Button that initiates the switch
     */
    public void startTrainingClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getTarget()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("learnmode.fxml"));
        try {
            Scene questioningScene = new Scene(loader.load());
            LearnmodeController sceneController = loader.getController();
            sceneController.initLearnmode(questionsCounter, questions);
            stage.setScene(questioningScene);
        }catch (IOException ioe){
            System.out.println("Could not load scene");
            ioe.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Hole die aktuelle Liste aller Fragen aus der Datenbank (vollständig)
        questions = getQuestionsFromDatabase();
        countOfQuestionsInDB = questions.size();

        if(countOfQuestionsInDB>0) {
            // TextField listener um sicherzustellen das nur nummern eingetragen werden
            questionsToPlayCounter.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.isEmpty() || (!newValue.isEmpty() && Integer.parseInt(newValue) > 0)) {
                    if (!newValue.matches("\\d*")) {
                        questionsToPlayCounter.setText("1");
                        questionsCounter = 1;
                    }else if(newValue.length() > 0 && (Integer.parseInt(newValue) > countOfQuestionsInDB || Integer.parseInt(newValue) < 1)) {
                        //make oldvalue compatible if needed
                        if(oldValue.isEmpty()){
                            if(Integer.parseInt(newValue) >  countOfQuestionsInDB){
                                oldValue = Integer.toString(countOfQuestionsInDB);
                            }else{
                                oldValue = "1";
                            }
                        }
                        // Anzahl der Fragen wird mit oldValue (bereinigt oben) aktualisiert weil der neue ungültig ist:
                        questionsToPlayCounter.setText(oldValue);
                        questionsCounter = Integer.parseInt(oldValue);
                    }else{
                        // Anzahl der Fragen wird mit newValue aktualisiert weil der Wert gültig ist:
                        questionsToPlayCounter.setText(newValue);
                        questionsCounter = Integer.parseInt(newValue);
                    }
                    // Aktiviere den Button für Start wenn ok
                    startBtn.setDisable(questionsToPlayCounter.getText().length() <= 0);

                }else {
                    startBtn.setDisable(true);
                }
            });
        }

        // Die Anzahl der Fragen werden in der Seite ausgegeben UND für die übergabe in die nächste Scene übernommen:
        countOfQuestions.setText(String.valueOf(countOfQuestionsInDB));
        questionsToPlayCounter.setText(String.valueOf(countOfQuestionsInDB));
        questionsCounter = countOfQuestionsInDB;

        // Text anpassen wenn keine Fragen eingetragen sind:
        if(countOfQuestionsInDB == 0){
            // Kontrollen zum Start ausblenden:
            questionSetupOptionsContainer.setVisible(false);
            // Button generieren für Switch To
            Button switchToEditBtn = new Button("Jetzt Fragen eintragen");
            switchToEditBtn.getStyleClass().addAll("btn", "btn-default", "large");
            switchToEditBtn.addEventHandler(ActionEvent.ACTION, this::switchToEditOverviewClicked);
            // Text anpassen
            howManyQuestionsText.setWrappingWidth(500.0);
            howManyQuestionsText.setTextAlignment(TextAlignment.CENTER);
            howManyQuestionsText.setText("Keine Fragen in der Datenbank gespeichert lege jetzt neue Fragen an um den Lernmodus starten zu können.");
            howManyQuestionsText.getStyleClass().add("text-error");
            learnModeInfoContainer.getChildren().add(switchToEditBtn);
        }else if(countOfQuestionsInDB < 5){
            // Text anpassen
            howManyQuestionsText.setText("Es sind nur sehr wenige Fragen in der Datenbank gespeichert, lege jetzt neue Fragen an.\nWie viele Fragen möchtest du spielen?");
            howManyQuestionsText.getStyleClass().add("text-warning");
            howManyQuestionsText.setTextAlignment(TextAlignment.CENTER);
        }
    }

    /**
     * Click Event for + / - Counter Buttons - sets new count of questions in questionsToPlayCounter <code>TextField</code>
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     ActionEvent
     * @see     #questionsToPlayCounter
     * @param   actionEvent <code>ActionEvent</code> that calls this method
     */
    public void counterChangeClicked(ActionEvent actionEvent) {
        int oldQCount = 0;

        if(questionsToPlayCounter.getText().length() > 0){
            oldQCount = Integer.parseInt(questionsToPlayCounter.getText());
        }

        // hole den Character im Button Text
        char c = ((Button) actionEvent.getSource()).getText().charAt(0);
        // Prüfe ob der char ein minus oder plus char ist
        // schreibe den neuen wert in das textfield
        if(c == '-' && --oldQCount > 0){
            questionsToPlayCounter.setText(String.valueOf(oldQCount--));
        }else if(c == '+' && ++oldQCount <= countOfQuestionsInDB){
            questionsToPlayCounter.setText(String.valueOf(oldQCount++));
        }
    }
}
