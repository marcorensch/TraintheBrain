package com.tbt.trainthebrain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class QuestionEditControllerTest {


   @Test
    public void checkMinOneAnswerIsTrue_OneAnswerIsTrueReturnTrue(){
       QuestionEditController qec = new QuestionEditController();

       ArrayList<Answer> answers = new ArrayList<>();

       answers.add(new Answer(0, "", true, 0));
       answers.add(new Answer(0, "", false, 0));

       boolean result = qec.checkMinOneAnswerIsTrue(answers);
       Assertions.assertTrue(result);
   }

   @Test
   public void checkMinOneAnswerIsTrue_NoAnswerIsTrueReturnFalse(){
      QuestionEditController qec = new QuestionEditController();

      ArrayList<Answer> answers = new ArrayList<>();
      answers.add(new Answer(0, "", false, 0));
      answers.add(new Answer(0, "", false, 0));

      boolean result = qec.checkMinOneAnswerIsTrue(answers);
      Assertions.assertFalse(result);
   }




}