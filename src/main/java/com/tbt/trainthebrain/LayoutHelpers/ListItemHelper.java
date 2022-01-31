package com.tbt.trainthebrain.LayoutHelpers;

import com.tbt.trainthebrain.Answer;
import com.tbt.trainthebrain.Main;
import com.tbt.trainthebrain.Question;
import com.tbt.trainthebrain.QuestionEditController;
import com.tbt.trainthebrain.sqlcontroller.DBTasks;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;

public class ListItemHelper {
    /**
     * Creates a styled HBox containing the truncated Question Text including tooltips
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @see     HBox
     * @see     Question
     * @param   question The question that should be returned as list element
     * @return  <code>HBox</code> including Questiontext for Listings including Link to EditView
     */
    public HBox createSimpleListElementWithLink(Question question){
        /* Container */
        // Erstelle HBox als Basis:
        HBox questionElement = new HBox();
        // Binde die CSS Klasse an:
        questionElement.getStyleClass().add("editListQuestionContainer");

        //Erstelle eine AnchorPane für die innere Struktur
        AnchorPane anchorPane = new AnchorPane();
        HBox.setHgrow(anchorPane,Priority.ALWAYS);



        // Erstelle den Frage-Text als neues Text Element:
        Text questionText = new Text(question.getQuestionTextCutted(100, "..."));
        // Binde die CSS Klasse an:
        questionText.getStyleClass().add("editListQuestionText");

        HBox questionTextContainer = new HBox(questionText);
        questionTextContainer.getStyleClass().add("padding-small");

        // Erstelle Tooltip
        Tooltip editTip = new Tooltip("Frage bearbeiten");
        editTip.setShowDelay(Duration.millis(500.0));
        Tooltip.install(questionTextContainer, editTip);

        /* Answer Statistics */
        String countOfAnswers = Integer.toString(question.getAnswers().size());
        int correctAnswersCount = 0;
        ArrayList<Answer> answers = question.getAnswers();
        for (Answer a:answers){
            if(a.getIsCorrect()) correctAnswersCount++;
        }
        Text answersStatistic = new Text(correctAnswersCount + " | " + countOfAnswers);
        answersStatistic.getStyleClass().add("text");
        answersStatistic.getStyleClass().add("meta");

        HBox trashIcon = new HBox();
        trashIcon.getStyleClass().add("delete-icon");

        HBox answerMeta = new HBox();

        answerMeta.setAlignment(Pos.CENTER_RIGHT);
        answerMeta.setSpacing(10.0);
        answerMeta.getStyleClass().add("padding-small");
        answerMeta.getChildren().add(answersStatistic);
        answerMeta.getChildren().add(trashIcon);

        Tooltip answerstatsTip = new Tooltip("Korrekte Antworten | Antwort Optionen");
        answerstatsTip.setShowDelay(Duration.millis(200));
        Tooltip.install(answerMeta,answerstatsTip);

        /* Innere Struktur */
        AnchorPane.setLeftAnchor(questionTextContainer, 0.0);
        AnchorPane.setRightAnchor(questionTextContainer, 0.0);
        AnchorPane.setRightAnchor(answerMeta, 0.0);

        anchorPane.getChildren().add(questionTextContainer);
        anchorPane.getChildren().add(answerMeta);

        // Platziere Innere Struktur in der HBox
        questionElement.getChildren().add(anchorPane);

        HBox questionOutterElement = new HBox();
        // Binde die CSS Klasse an:
        questionOutterElement.getStyleClass().add("editListQuestionOutterContainer");
        questionOutterElement.getChildren().add(questionElement);
        HBox.setHgrow(questionElement,Priority.ALWAYS);

        // Click Events
        questionTextContainer.setOnMouseClicked(e -> switchToEditQMaskClick(e, question));
        trashIcon.setOnMouseClicked(e -> deleteQuestion(question.getId(), questionOutterElement));

        return questionOutterElement;
    }

    /**
     * Handles the click event and switches the scene to the question edit view by loading the question-edit.fxml
     * Loads the clicked question Object by using initWithData Method from Scene Controller
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @see     Question
     * @see     QuestionEditController#initWithData(Question)
     * @param   mouseEvent    Click event
     * @param   question      Related Question Object that should be used in the edit view
     */
    public void switchToEditQMaskClick(MouseEvent mouseEvent, Question question) {
        Stage stage = (Stage) ((Node) mouseEvent.getTarget()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("question-edit.fxml"));
        try {
            Scene questioningScene = new Scene(loader.load());
            // Load Controller from FXML
            QuestionEditController sceneController = loader.getController();
            // Set given Object as field values
            sceneController.initWithData(question);

            stage.setScene(questioningScene);
        }catch (IOException ioe){
            System.out.println("Could not load scene");
            ioe.printStackTrace();
        }
    }

    /**
     * Removes a list item <code>HBox</code> from the list by setting its height to 0 and setting visibility to false
     * calls the required Database Task to delete the question element aswell as its related answers in the database.
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @see     HBox
     * @param   id            The <code>id</code> of the question element that should be deleted
     * @param   outterElement The <code>HBox</code> that should be hidden in the GUI after click
     */
    public void deleteQuestion(int id, HBox outterElement){
        outterElement.setMinHeight(0);
        outterElement.setPrefHeight(0);
        outterElement.setMaxHeight(0);
        outterElement.setVisible(false);
        outterElement.setPadding(new Insets(0));

        DBTasks connection = new DBTasks();
        connection.deleteQuestionAndAnswersInDb(id);
    }
}
