package com.tbt.trainthebrain;

import com.tbt.trainthebrain.sqlcontroller.DBTasks;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class QuestionEditController extends AppController implements Initializable {

    int limitForQuestionText = 150;
    int limitForAnswerText = 100;

    @FXML
    private TextField id, answerId0, answerId1, answerId2, answerId3;
    @FXML
    private TextArea questionText, answerText0, answerText1, answerText2, answerText3;
    @FXML
    private CheckBox answerCorrect0, answerCorrect1, answerCorrect2, answerCorrect3;
    @FXML
    private Text questionTextCounterText, questionTextLimitText;
    @FXML
    private Text answer0TextLimitText, answer1TextLimitText, answer2TextLimitText, answer3TextLimitText;
    @FXML
    private Text answer0TextCounterText, answer1TextCounterText, answer2TextCounterText, answer3TextCounterText;

    @FXML
    private Text checkTextMin2Answers, checkTextMin1True;

    @FXML
    private Button saveQuestionBtn;

    private final ArrayList<TextArea> answerTextsArray = new ArrayList<>();

    private final ArrayList<TextField> answerIdsArray = new ArrayList<>();

    private final ArrayList<CheckBox> isCorrectCheckBoxesArray = new ArrayList<>();

    /**
     * Click Handler for simple scene switch call without data --> Wrapper that calls switchToEditOverviewClicked
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     ActionEvent
     * @see     #switchToEditOverviewClicked(ActionEvent)
     * @param   actionEvent <code>ActionEvent</code> that initiates the call
     */
    public void cancelQuestionEditClick(ActionEvent actionEvent) {
        // Back to List of Questions Edit screen
        switchToEditOverviewClicked(actionEvent);
    }

    /**
     * Click Handler for simple scene switch to edit question overview without data.
     * Calls method that handles DB Save Event
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     ActionEvent
     * @see     #handleQuestionInDb
     * @param   actionEvent <code>ActionEvent</code> that triggers method call
     */
    public void saveQuestionEditClick(ActionEvent actionEvent) {
        handleQuestionInDb();
        // Back to List of Questions Edit screen
        switchToEditOverviewClicked(actionEvent);
    }

    /**
     * Checks based on the question id if this is a new question or an already saved one that needs to be updated ( id = 0 ==> new question)
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     #addAnswerFromCreatedQuestion(int)
     * @see     #createOrEditAnswer(int)
     */
    private void handleQuestionInDb() {
        //Wenn question id = 0 und Text hat Länge = insert Questiontext (wenn erzeugt brauche id von Question für Answers)
        int createdQuestionId;
        if (id.getText().isEmpty()) {
            id.setText("0");
        }
        int questionId = Integer.parseInt(id.getText());
        String question = questionText.getText().trim();
        DBTasks con = new DBTasks();
        if (questionId == 0 && !question.isEmpty()) {
            if (verifyIfMinTwoAnswers()) {
                if (verifyMinOneAnswerIsTrue()) {
                    createdQuestionId = con.insertNewQuestion(question);
                    addAnswerFromCreatedQuestion(createdQuestionId);
                }
            }
        } else if (questionId > 0 && !question.isEmpty()) {
            //Question wird geupdatet und Answers werden geupdatet gelöscht oder neue hinzugefügt
            con.updateQuestion(questionId, question);
            createOrEditAnswer(questionId);

        } else if (questionId > 0) {
            //Question und dazugehörige answers werden gelöscht
            con.deleteQuestionAndAnswersInDb(questionId);
        }
    }

    /**
     * Creates, edits or deletes answers in database for the given <code>questionId</code> based on GUI status
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     #createNewAnswerObjects(int)
     * @see     #verifyIfMinTwoAnswers()
     * @see     #verifyMinOneAnswerIsTrue()
     * @param   questionId  <code>int</code> of the actual question that will me modified
     */
    public void createOrEditAnswer(int questionId) {
        //erstellt neue Answers zu den bereits erstellten Questions
        //ändert bestehende Answers
        //löscht bestehende Answers, wenn kein Text vorhanden ist.
        ArrayList<Answer> answers = createNewAnswerObjects(questionId);
        DBTasks con = new DBTasks();
        if (verifyIfMinTwoAnswers()) {
            if (verifyMinOneAnswerIsTrue()) {
                for (Answer answer : answers) {
                    // erstellt, aktualisiert oder löscht Answer in DB
                    if (answer.getId() == 0 && !answer.getText().isEmpty()) {
                        con.addOneAnswerInDB(answer);
                    } else if (answer.getId() > 0 && !answer.getText().isEmpty()) {
                        con.updateAnswer(answer);
                    } else if (answer.getId() > 0 && answer.getText().isEmpty()) {
                        if (answer.getIsCorrect()) {
                            answer.setCorrect(false);
                            if (checkMinOneAnswerIsTrue(answers)){
                                con.deleteAnswerInDb(answer.getId());
                            }
                        } else {
                            con.deleteAnswerInDb(answer.getId());
                        }
                    }
                }
            }
        }
    }

    /**
     * Will add all answers to the database for the current question, used when a new question got saved
     *
     * @author  Claudia Martinez
     * @author  Marco Rensch
     * @since   1.0
     * @param   newCreatedQuestionId <code>int</code>   the new question id that got generated before
     */
    public void addAnswerFromCreatedQuestion(int newCreatedQuestionId) {
        // Erstellt Answers für neu erstellte Question mit neu erstellter Questionid
        //Diese Methode wird nur ausgeführt, wenn neue question erstellt wird.
        // Array von Answers mit answerObjekten, die questionId enthält von neu erstellter Question
        ArrayList<Answer> addAnswers = createNewAnswerObjects(newCreatedQuestionId);
        ArrayList<Integer> intAnswerIds = parsingAnswerIdIntoInteger();
        DBTasks con = new DBTasks();
        //wenn Answer id = 0 und Text hat Länge = neue Answer in Db hinzufügen (insert mit questionid und answerText)
        for (int i = 0; i < intAnswerIds.size(); i++) {
            if (intAnswerIds.get(i) == 0 && !answerTextsArray.get(i).getText().trim().isEmpty()) {
                con.addOneAnswerInDB(addAnswers.get(i));
            }
        }
    }

    // Verifiziert ob mindestens eine Answer als korrekt markiert wurde.

    /**
     * Verifies that at least one answer in the given set of answers are marked as correct
     *
     * @author  Claudia Martinez
     * @author  Marco Rensch
     * @since   1.0
     * @see     #isCorrectCheckBoxesArray
     * @see     CheckBox
     * @return  <code>true</code> if at least one answer is marked as correct <code>false</code> if no answer is marked as correct
     */
    public boolean verifyMinOneAnswerIsTrue() {
        boolean verify = false;
        for (CheckBox checkBox : isCorrectCheckBoxesArray) {
            if (checkBox.isSelected()) {
                verify = checkIfAnswerWithActiveCheckBoxHasAnswerText(checkBox);
                break;
            }
        }
        return verify;
    }

    /**
     * Checks if the related <code>TextArea</code> of an active <code>CheckBox</code> is not empty
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     TextArea
     * @see     #verifyMinOneAnswerIsTrue()
     * @param   checkBox    <code>CheckBox</code> element that is tickt
     * @return  <code>true</code> if related <code>TextArea</code> is not empty <code>false</code> if it is empty
     */
    private boolean checkIfAnswerWithActiveCheckBoxHasAnswerText(CheckBox checkBox){
        VBox parent = (VBox) checkBox.getParent().getParent();
        TextArea answerTextArea = (TextArea) parent.getChildren().get(1);

        return !answerTextArea.getText().trim().isEmpty();
    }

    /**
     * Checks if at least one answer from the set of answers inside an <code>ArrayList</code> is marked as correct
     *
     * @author  Claudia Martinez
     * @author  Marco Rensch
     * @since   1.0
     * @see     ArrayList
     * @see     Answer
     * @param   answers <code>ArrayList</code> of <code>Answer</code> objects that should be checked
     * @return  <code>true</code> if at least one answer is set as correct, <code>false</code> if no <coder>Answer</coder> in this <code>ArrayList</code> is set as correct <code>Answer</code>
     */
    public boolean checkMinOneAnswerIsTrue(ArrayList<Answer> answers) {
        boolean verify = false;
        for (Answer answer : answers) {
            if (answer.getIsCorrect()) {
                verify = true;
                break;
            }
        }
        return verify;
    }

    /**
     * Checks that at least two answerTexts got content
     *
     * @author  Claudia Martinez
     * @author  Marco Rensch
     * @since   1.0
     * @see     #answerTextsArray
     * @return  <code>true</code> if at least two Answertext <code>TextArea</code>'s has content and <code>false</code> if not.
     */
    public boolean verifyIfMinTwoAnswers() {
        boolean verify = false;
        int answersCount = 0;
        /* Check mit for Schleife und möglichem Abbruch */
        for (int i = 0; i < 3; i++) {
            if (!answerTextsArray.get(i).getText().trim().isEmpty()) {
                answersCount++;
            }
            if (answersCount == 2) {
                verify = true;
                break;
            }
        }
        return verify;
    }

    /**
     * Creates new <code>Answer</code> Objects including al necessary data
     *
     * @author  Claudia Martinez
     * @author  Marco Rensch
     * @since   1.0
     * @see     ArrayList
     * @see     Answer
     * @param   questionID  <code>int</code> id of the actual question
     * @return  <code>ArrayList</code> of newly created <code>Answer</code> objects
     */
    public ArrayList<Answer> createNewAnswerObjects(int questionID) {
        //erzeugt neue Answerobjekte befüllt sie mit answerId, text, isCorrect, question id von zugehöriger Frage
        //Füllt alle Answerojekte in eine Answer ArrayList
        ArrayList<Answer> answerAddList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            answerAddList.add(new Answer(
                    parsingAnswerIdIntoInteger().get(i),
                    answerTextsArray.get(i).getText().trim(),
                    isCorrectCheckBoxesArray.get(i).isSelected(),
                    questionID)
            );
        }
        return answerAddList;
    }

    /**
     * Adds all needed FXML Elements (answer texts, answer id's, answer checkboxes) into the related <code>ArrayList</code>
     *
     * @author  Claudia Martinez
     * @author  Marco Rensch
     * @since   1.0
     * @see     #answerTextsArray
     * @see     #answerIdsArray
     * @see     #isCorrectCheckBoxesArray
     */
    public void fillAnswerTextAndAnswerIdsCheckboxesInArray() {
        //AnswerText in ArrayList abgefüllt
        Collections.addAll(answerTextsArray, answerText0,answerText1,answerText2,answerText3);

        //AnswerId's in ArrayList abgefüllt
        Collections.addAll(answerIdsArray, answerId0,answerId1,answerId2,answerId3);

        //Checkboxes in ArrayList abgefüllt
        Collections.addAll(isCorrectCheckBoxesArray, answerCorrect0,answerCorrect1,answerCorrect2,answerCorrect3);
    }

    /**
     * Securily parses answer Id's from <code>TextField</code> into an <code>ArrayList</code> of Integers
     *
     * @author  Claudia Martinez
     * @author  Marco Rensch
     * @since   1.0
     * @see     ArrayList
     * @return  <code>ArrayList</code> of Integers for each answer
     */
    public ArrayList<Integer> parsingAnswerIdIntoInteger() {
        ArrayList<Integer> intAnswerIdsArray = new ArrayList<>();
        for (int i = 0; i < answerIdsArray.size(); i++) {
            int id = 0;
            if (!answerIdsArray.get(i).getText().trim().isEmpty()) {
                id = Integer.parseInt(answerIdsArray.get(i).getText());
            }
            intAnswerIdsArray.add(i, id);
        }
        return intAnswerIdsArray;
    }

    /**
     * KeyEvent listener that updates the char counter of the related <code>Textarea</code> and calls form validity check
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     TextArea
     * @see     KeyEvent
     * @see     #updateTextCounter(TextArea, int, Text)
     * @see     #checkFormValidity()
     * @param   keyEvent that triggers this method
     */
    public void updateQuestionTextCounterListener(KeyEvent keyEvent) {
        TextArea ta = ((TextArea) keyEvent.getTarget());
        updateTextCounter(ta, limitForQuestionText, questionTextCounterText);

        // Do the checks
        checkFormValidity();
    }

    /**
     * KeyEvent listener that updates the char counter of the related answer <code>Textarea</code> and calls form validity check
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     TextArea
     * @see     KeyEvent
     * @see     #updateTextCounter(TextArea, int, Text)
     * @see     #checkFormValidity()
     * @param   keyEvent that triggers this method
     */
    public void answerTextfieldsListener(KeyEvent keyEvent) {
        TextArea ta = ((TextArea) keyEvent.getTarget());
        Text counter = new Text("Error");
        CheckBox checkBox;
        // Find Counter Text for this answer
        VBox parent = (VBox) ta.getParent();
        // Get Child of type HBox (that contains our Text Nodes)
        for (Node child: parent.getChildren()) {
            // Set Checkbox "is correct" to not selected if answerText is now empty
            if (child instanceof AnchorPane) {
                checkBox = ((CheckBox) ((AnchorPane) child).getChildren().get(1));
                if(ta.getText().trim().isEmpty()) {
                    checkBox.setSelected(false);
                    checkBox.setDisable(true);
                }else{
                    checkBox.setDisable(false);
                }
            }
            if(child instanceof HBox){
                counter = (Text)((HBox) child).getChildren().get(0);
                break;
            }
        }
        updateTextCounter(ta, limitForAnswerText, counter);

        // Do the checks
        checkFormValidity();
    }

    /**
     * Method that does the defined checks in the edit form view
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     #verifyIfMinTwoAnswers()
     * @see     #verifyMinOneAnswerIsTrue()
     */
    @FXML
    private void checkFormValidity(){
        checkTextMin2Answers.setVisible(verifyIfMinTwoAnswers() && !questionText.getText().isEmpty());
        checkTextMin1True.setVisible(verifyMinOneAnswerIsTrue());
        saveQuestionBtn.setDisable(!verifyMinOneAnswerIsTrue() || !verifyIfMinTwoAnswers() || questionText.getText().isEmpty());
    }

    /**
     * Updates the related <code>Text</code> element with the new amount of chars in the <code>TextArea</code> and blocks input when defined limit is reached.
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     Text
     * @see     TextArea
     * @param   textArea      <code>TextArea</code> that got updated by user interaction
     * @param   limit         <code>int</code> value that sets the limit for the related <code>TextArea</code>
     * @param   counterText   <code>Text</code> Node that shopuld be updated with the new count of chars
     */
    public void updateTextCounter(TextArea textArea, int limit, Text counterText){
        String string = textArea.getText();
        int currentLength = textArea.getLength();

        if (currentLength > limit) {
            textArea.setText(string.substring(0, limit));
            textArea.positionCaret(string.length());
        }
        counterText.setText(Integer.toString(Math.min(limit, currentLength)));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fillAnswerTextAndAnswerIdsCheckboxesInArray();

        // Set question-text-length limits based on Params
        questionTextLimitText.setText(Integer.toString(limitForQuestionText));
        answer0TextLimitText.setText(Integer.toString(limitForAnswerText));
        answer1TextLimitText.setText(Integer.toString(limitForAnswerText));
        answer2TextLimitText.setText(Integer.toString(limitForAnswerText));
        answer3TextLimitText.setText(Integer.toString(limitForAnswerText));

    }

    /**
     * Custom Initiator with <code>Question</code> data - sets content of the given <code>Question</code> element in the form
     * Calls form validation on load
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     Question
     * @see     #checkFormValidity()
     * @param   question    holds the selected <code>Question</code> object and its data that should be setted in the form
     */
    public void initWithData(Question question) {
        id.setText(Integer.toString(question.getId()));
        questionText.setText(question.getQuestion());
        ArrayList<Answer> answers = question.getAnswers();
        //answers werden den answerText zugeteilt, wenn nur 3 answers bleibt 4. textarea einfach leer.
        for (int i = 0; i < answers.size(); i++) {
            answerTextsArray.get(i).setText(answers.get(i).getText());
            answerIdsArray.get(i).setText(String.valueOf(answers.get(i).getId()));
            isCorrectCheckBoxesArray.get(i).setSelected(answers.get(i).getIsCorrect());
            if(!answers.get(i).getText().isEmpty()){
                isCorrectCheckBoxesArray.get(i).setDisable(false);
            }
        }

        // Set used length based on content in Question
        updateTextCounter(questionText,limitForQuestionText,questionTextCounterText);

        // Set used length based on content in Answers
        updateTextCounter(answerText0,limitForAnswerText,answer0TextCounterText);
        updateTextCounter(answerText1,limitForAnswerText,answer1TextCounterText);
        updateTextCounter(answerText2,limitForAnswerText,answer2TextCounterText);
        updateTextCounter(answerText3,limitForAnswerText,answer3TextCounterText);

        // Do the checks
        checkFormValidity();
    }


}
