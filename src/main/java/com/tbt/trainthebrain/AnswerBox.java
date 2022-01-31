package com.tbt.trainthebrain;

import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class AnswerBox extends HBox implements AnswerBoxConfig{
    String answerId;
    boolean isCorrect;
    boolean isSelected;

    @Override
    public String getAnswerId() {
        return answerId;
    }

    @Override
    public void setAnswerId() {

    }

    @Override
    public boolean getIsCorrectAnswer() {
        return isCorrect;
    }

    @Override
    public void setIsCorrectAnswer() {

    }

    @Override
    public boolean getIsSelected() {
        return isSelected;
    }

    @Override
    public void setIsSelected() {
        isSelected = !isSelected;
    }

    public void setIsNotSelected() {
        isSelected = false;
    }

    public void setIsCorrectAnswer(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}
