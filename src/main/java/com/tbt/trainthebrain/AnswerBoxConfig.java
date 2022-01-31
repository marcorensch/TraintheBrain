package com.tbt.trainthebrain;

public interface AnswerBoxConfig {

    public String getAnswerId();
    public void setAnswerId();

    public boolean getIsCorrectAnswer();
    public void setIsCorrectAnswer();

    boolean getIsSelected();
    void setIsSelected();
}
