package com.tbt.trainthebrain;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class LearnmodeController extends AppController implements Initializable {

    boolean underCheck;                                             // Wird jeweils auf true gesetzt wenn Antworten geprüft werden

    int questionIndex = 0;                                          // Aktueller (Fragen) index 0-based
    int questionsCount = 0;                                         // Anzahl (max) Fragen gem. Selektion
    ArrayList<AnswerBox> answerBoxes = new ArrayList<>();           // AntwortBoxen FXML Elemente
    ArrayList<Question> selectedQuestions = new ArrayList<>();      // Liste der Auswahl an Fragen die getroffen wurde
    ArrayList<QuestionResult> questionResults = new ArrayList<>();  // Beinhaltet alle Resultate des aktuellen Durchlaufs
    ArrayList<Text> answerTextNodes = new ArrayList<>();            // TextNodes innerhalb der answerBoxen für direkten Zugriff im Loop (unten)


    @FXML
    AnswerBox answerBox0,answerBox1,answerBox2,answerBox3;

    @FXML
    Button cancelLearnModeBtn,checkBtn,nextQuestBtn,endcardBtn;

    @FXML
    Text actualQuestionNum, questionCountText, questionText, answer0,answer1,answer2,answer3;

    @FXML
    GridPane answersGrid;

    Integer timeSeconds;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Alle AnswerBoxen zur Collection hinzufügen
        Collections.addAll(answerBoxes, answerBox0, answerBox1, answerBox2, answerBox3);
        Collections.addAll(answerTextNodes, answer0, answer1, answer2, answer3);
    }

    /**
     * Custom Method for view initialisation. Sets content that should be used.
     * Handles / sets the questions that should be played limited by <code>questionsCount</code>
     * Calls setNextQuestion(0) Method after setup
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     ArrayList
     * @see     Question
     * @see     #setNextQuestion(int) 
     * @param   questionsCount  <code>int</code> count of questions that should be played
     * @param   questions       <code>ArrayList</code> of <code>Question</code> elements from database
     */
    public void initLearnmode(int questionsCount, ArrayList<Question> questions){
        // setze questionsCount basierend auf Wert der Selektion & passe TextNode an
        this.questionsCount = questionsCount;
        actualQuestionNum.setText(Integer.toString(questionIndex+1));

        questionCountText.setText(Integer.toString(questionsCount));

        Collections.shuffle(questions);
        for (int i = 0; i < questionsCount; i++) {
            selectedQuestions.add(questions.get(i));
            Collections.shuffle(selectedQuestions.get(i).getAnswers());
        }

        // "Baue" Frage in die Scene ein:
        setNextQuestion(0);
    }

    /**
     * Helper Method that disables (locks) a Button object for the given time in seconds
     * Shows a countdown (second-wise) in place of the defined button text till the given seconds are passed and replaces then the countdown with the original button text
     *
     * Inspiration source: https://asgteach.com/2011/10/javafx-animation-and-binding-simple-countdown-timer-2/
     *
     * @author  Marco Rensch
     * @since   1.0
     * @see     Button
     * @see     Integer
     * @see     Timeline
     * @param   button      <code>Button</code> object that should be used
     * @param   seconds     <code>Integer</code> value in seconds, defines how long the button should be locked
     */
    private void setButtonActiveWithTimer(Button button, Integer seconds){
        String originalBtnText = button.getText();
        button.setMinWidth(button.getPrefWidth());
        button.setDisable(true);
        final Timeline tl = new Timeline(new KeyFrame(Duration.seconds(seconds), actionEvent -> button.setDisable(false)));
        tl.setCycleCount(1);
        tl.play();
        final Timeline countdownTl = new Timeline();
        timeSeconds = seconds;
        // update timerLabel
        button.setText(timeSeconds.toString());
        countdownTl.setCycleCount(Timeline.INDEFINITE);
        countdownTl.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
            timeSeconds--;
            // update timerLabel
            button.setText(timeSeconds.toString());
            if (timeSeconds <= 0) {
                countdownTl.stop();
                button.setText(originalBtnText);
            }
        }));
        countdownTl.playFromStart();
    }

    /**
     * Switches question object from selectedQuestions ArrayList inside same scene by index
     *
     * Replaces question text inside the scene with the text given by the actual object
     * Removes all given styles (selection,correct,false) from all <code>AnswerBox</code> objects in the scene and re-publish the new answers for the given question
     * Hides unused <code>AnswerBox</code> elements in the scene
     * Shows used <code>AnswerBox</code> elements in the scene
     * Sets underCheck to <code>false</code>
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     #selectedQuestions
     * @see     ArrayList
     * @see     AnswerBox
     * @param   qindex     <code>int</code> index of the actual question inside of the selectedQuestions ArrayList
     */
    private void setNextQuestion(int qindex){
        // Deaktiviere underCheck
        underCheck = false;
        // Deaktiviere Check Button mit Timer Delay für Enabling
        setButtonActiveWithTimer(checkBtn, 3);

        // Publiziere die Frage
        questionText.setText(selectedQuestions.get(qindex).getQuestion());

        // Loop über alle AnswerBoxen um Styling (falsch / richtig & selected) zurückzusetzen
        for (AnswerBox answerBox:answerBoxes) {
            // CleanUp aller antworten
            // Alle zuvor gesetzten correct / false / selected Klassen entfernen (Styling)
            answerBox.getStyleClass().remove("correct");
            answerBox.getStyleClass().remove("false");
            answerBox.getStyleClass().remove("false-selected");
            answerBox.getStyleClass().remove("selected");

            // Zuerst alle answerBoxen sicher einblenden (Styling)
            answerBox.setVisible(true);

            // Alle zuvor selektierten Boxen zurücksetzen (Funktion)
            answerBox.setIsNotSelected();
        }
        // Loop über die anzahl der antworten in der aktuellen Frage und setzen der AnswerBox Parameter
        for (int i = 0; i < selectedQuestions.get(qindex).getAnswers().size(); i++) {
            // Setze die Id der Antwort
            answerBoxes.get(i).setAnswerId(String.valueOf(selectedQuestions.get(qindex).getAnswers().get(i).getId()));
            // Setze ob die Antwort korrekt ist
            answerBoxes.get(i).setIsCorrectAnswer(selectedQuestions.get(qindex).getAnswers().get(i).getIsCorrect());
            // Setze Antworttext
            answerTextNodes.get(i).setText(selectedQuestions.get(qindex).getAnswers().get(i).getText());
        }

        // Blende "überflüssige" answerBoxes aus weil wir ggf. weniger als 4 antworten haben (Styling)
        if(selectedQuestions.get(qindex).getAnswers().size() < 4){
            for (int i = selectedQuestions.get(qindex).getAnswers().size(); i < 4 ; i++) {
                answerBoxes.get(i).setVisible(false);
            }
        }
        // Einblende Animationen
        fadeInTransition(questionText,1000,0);
        fadeInTransition(answersGrid, 1000,300);
    }

    /**
     * Click event handler for <code>AnswerBox</code> elements
     * If not under check this methods sets the selected class on clicked <code>AnswerBox</code> elements
     * Can handle clicks on <code>Text</code> Nodes inside <code>AnswerBox</code> elements aswell
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     MouseEvent
     * @see     #underCheck
     * @see     AnswerBox
     * @see     Node
     * @param   mouseEvent  Click event on an <code>AnswerBox</code> or <code>Text</code> node inside an <coder>AnswerBox</coder>
     */
    public void clickedOnAnswer(MouseEvent mouseEvent) {
        //Prüfe ob wir im Check Modus sind (Antworten prüfen wurde aktiviert)
        if(!underCheck) {
            // Identifiziere das geklickte Element
            Node clickedElNode = (Node) mouseEvent.getTarget();
            // Prüfe ob klick auf AnswerBox direkt oder auf TextNode (child) ausgeführt wurde & identifiziere den entsprechenden Container
            AnswerBox container = clickedElNode instanceof Text ? ((AnswerBox) clickedElNode.getParent()) : ((AnswerBox) clickedElNode);

            // Toggle selectedStatus
            container.setIsSelected();

            // Styling anwenden
            if (container.getStyleClass().contains("selected")) {
                container.getStyleClass().remove("selected");
            } else {
                container.getStyleClass().add("selected");
            }
        }
    }

    /**
     * Click event handler for answer check button, sets the GUI into checkmode by settings #underCheck to <code>true</code>
     * Sets styling of <code>AnswerBox</code> elements based in selection and isCorrect flag to correct or false
     * Hides check button after click and shows next button
     *
     * Calls calcQuestionPts Method to calculate user points of this question
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     AnswerBox
     * @see     #calcQuestionPts
     * @param   actionEvent     ActionEvent fired on click
     */
    public void checkAnswersClicked(ActionEvent actionEvent) {
        // Aktiviere underCheck
        underCheck = true;

        // Jede AnswerBox auf Status prüfen
        for (AnswerBox answerBox: answerBoxes) {
            if (answerBox.isSelected) {
                if (answerBox.isCorrect) {
                    answerBox.getStyleClass().add("correct");
                } else {
                    answerBox.getStyleClass().add("false-selected");
                }
            }else{
                if (answerBox.isCorrect) {
                    answerBox.getStyleClass().add("false");
                }
            }
        }
        // Den CheckBtn nun ausblenden
        checkBtn.setVisible(false);

        // Punkte Berechnen & ins Array übergeben:
        questionResults.add(calcQuestionPts(answerBoxes));

        // Wenn wir bei der letzten Frage angekommen sind zeigen wir den Button zur Endcard sonst Next Button
        if(questionIndex+1 < questionsCount){
            nextQuestBtn.setVisible(true);
        }else{
            endcardBtn.setVisible(true);
        }
    }

    /**
     * Click handler Method that sets question num counter up by one hides nextQuestion Button and shows check Button again in GUI before showing up the next question
     * Calls setNextQuestion Method
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     #setNextQuestion(int)
     * @param   actionEvent ActionEvent fired on click
     */
    public void switchQuestionClicked(ActionEvent actionEvent) {
        checkBtn.setVisible(true);
        nextQuestBtn.setVisible(false);
        questionIndex++;
        actualQuestionNum.setText(Integer.toString(questionIndex+1));

        setNextQuestion(questionIndex);
    }

    /**
     * Click Event handler that replaces the current scene by the learnmode-endcard scene
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     Stage
     * @see     Scene
     * @see     FXMLLoader
     * @param   actionEvent ActionEvent fired on click
     */
    public void goToEndScreenClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getTarget()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("learnmode-endcard.fxml"));
        try {
            Scene questioningScene = new Scene(loader.load());
            LearnmodeEndcardController sceneController = loader.getController();
            sceneController.initResults(questionResults);
            stage.setScene(questioningScene);
        }catch (IOException ioe){
            System.out.println("Could not load scene");
            ioe.printStackTrace();
        }
    }

    /**
     * Calculates the points for this question the user has claimed aswell as the max points possible by the given answer options
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     AnswerBox
     * @see     QuestionResult
     * @param   answerBoxes ArrayList of <code>AnswerBox</code> elements for this question
     * @return  <code>QuestionResult</code> object that holds gained points as well as max points for this question
     */
    public QuestionResult calcQuestionPts(ArrayList<AnswerBox> answerBoxes){
        QuestionResult qr = new QuestionResult();
        for (AnswerBox a: answerBoxes) {
            if(a.isVisible()) {
                qr.countAnswers++;
                if (a.getIsCorrectAnswer()) {
                    qr.countCorrectAnswers++;
                    if (a.getIsSelected()) {
                        qr.pts++;
                    } else {
                        qr.pts--;
                        qr.fullyCorrect = false;
                    }
                } else {
                    qr.countWrongAnswers++;
                    if (a.getIsSelected()) {
                        qr.pts--;
                        qr.fullyCorrect = false;
                    } else {
                        qr.pts++;
                    }
                }
            }
        }

        // Du kannst nicht weniger als 0 Punkte für eine Frage erzielen
        qr.pts = (qr.pts < 0) ? 0 : qr.pts;

        return qr;
    }
}
