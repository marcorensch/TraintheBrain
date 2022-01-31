package com.tbt.trainthebrain;

import java.util.ArrayList;

public class Question {
    private int id;
    private String question;
    private ArrayList<Answer> answers;

    public Question(int id, String question, ArrayList<Answer> answers) {
        this.id = id;
        this.question = question;
        this.answers = answers;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public String getQuestionTextCutted(int i, String ellipsis) {
        if(question.length() > i){
            return question.substring(0, i) + ellipsis;
        }

        return question;
    }
}


