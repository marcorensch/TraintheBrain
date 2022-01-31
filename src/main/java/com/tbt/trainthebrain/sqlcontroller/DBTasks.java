package com.tbt.trainthebrain.sqlcontroller;

import com.tbt.trainthebrain.Answer;
import com.tbt.trainthebrain.Question;

import java.sql.*;
import java.util.ArrayList;

public class DBTasks {
    /**
     * Deletes all Questions & Answers in the database
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since 1.0
     */
    public static void deleteAllQuestions() {
        String answersQuery =  "DELETE FROM tbl_answers";
        String questionsQuery =  "DELETE FROM tbl_questions";

        try (Connection con = DriverManager.getConnection(SQLConnectionData.getURL(), SQLConnectionData.getUSER(), SQLConnectionData.getPASSWORD());
             Statement statement = con.createStatement()) {

            statement.executeUpdate(answersQuery);
            statement.executeUpdate(questionsQuery);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Fehler beim löschen aller Daten");
            ex.printStackTrace();
        }
    }

    /**
     * Get all Questions including the related answers from database
     *
     * @author  Claudia Martinez
     * @author  Marco Rensch
     * @see     ArrayList
     * @see     Question
     * @since   1.0
     * @return  <code>Arraylist<Question></code> An Arraylist that holds all Question Objects
     */
    public ArrayList<Question> getAllQuestionsFromDb(){
        ArrayList<Question> questions = new ArrayList<>();
        String query =  "SELECT * FROM tbl_questions";

        try (Connection con = DriverManager.getConnection(SQLConnectionData.getURL(), SQLConnectionData.getUSER(), SQLConnectionData.getPASSWORD());
             Statement statement = con.createStatement()) {

            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                ArrayList<Answer> answers = getAllAnswersForQuestion(rs.getInt("question_id"));
                questions.add(
                        new Question(
                            rs.getInt("question_id"),
                            rs.getString("question_text"),
                            answers
                        )
                );
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Fehler beim laden aller Question aus der Datenbank:");
            ex.printStackTrace();
        }
        return questions;
    }

    /**
     * Get all answers for a certain question and returns an ArrayList of Answer objects
     *
     * @author  Claudia Martinez
     * @author  Marco Rensch
     * @see     Answer
     * @see     ArrayList
     * @since   1.0
     * @param   questionId     <code>int</code> that contains the related question id
     * @return  <code>ArrayList<Answer></code>  Arraylist that containts all related answers for this question
     */
    private ArrayList<Answer> getAllAnswersForQuestion(int questionId){
        ArrayList<Answer> answers = new ArrayList<>();
        String query =  "SELECT * FROM tbl_answers WHERE question_id = '"+questionId+"'";
        try (
                Connection con = DriverManager.getConnection(SQLConnectionData.getURL(), SQLConnectionData.getUSER(), SQLConnectionData.getPASSWORD());
                Statement statement = con.createStatement()
        ) {
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                answers.add(new Answer(
                                rs.getInt("answer_id"),
                                rs.getString("answer_text"),
                                rs.getBoolean("answer_correct"),
                                questionId
                        )
                );
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("UpdateAnswers hat Fehler geworfen:");
            ex.printStackTrace();
        }
        return answers;
    }

    /**
     * Adds one answer object to database
     *
     * @author  Claudia Martinez
     * @author  Marco Rensch
     * @see     Answer
     * @since   1.0
     * @param answer    Answer Object that should be added to the database
     */
    public void addOneAnswerInDB(Answer answer){

        String query = "INSERT INTO tbl_answers (answer_text, answer_correct, question_id) VALUE ('" + answer.getText() + "'," + answer.getIsCorrect() + "," + answer.getQuestionId() + ");";

        try (Connection con = DriverManager.getConnection(SQLConnectionData.getURL(), SQLConnectionData.getUSER(), SQLConnectionData.getPASSWORD());
             Statement statement = con.createStatement()) {

            statement.execute(query);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("addOneAnswerInDB hat Fehler geworfen:");
            ex.printStackTrace();
        }

    }

    /**
     * Updates an answer in the database
     *
     * @author  Claudia Martinez
     * @author  Marco Rensch
     * @since   1.0
     * @param   questionId  The <code>id</code> of the related question
     * @param   answerText  The Text String of this answer
     * @param   isCorrect   <code>true</code> if answer is correct or <code>false</code> if answer is wrong
     * @param   answerId    Unique <code>id</code> of the answer that should be updated
     */
    public void updateAnswer(int questionId, String answerText, boolean isCorrect, int answerId){
        String query = "UPDATE tbl_answers SET answer_text='" + answerText + "', answer_correct=" + isCorrect + "" + " WHERE question_id=" + questionId + " AND answer_id=" + answerId;

        try (Connection con = DriverManager.getConnection(SQLConnectionData.getURL(), SQLConnectionData.getUSER(), SQLConnectionData.getPASSWORD());
             Statement statement = con.createStatement()) {
            boolean status = statement.execute(query);
            if(status){
                System.out.println("Antwort ("+answerId+") für Frage "+ questionId + " aktualisiert");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("UpdateAnswers hat Fehler geworfen:");
            ex.printStackTrace();

        }

    }

    /**
     * Overload Method that allows to update an answer by using given answer object
     *
     * @author  Claudia Martinez
     * @author  Marco Rensch
     * @since   1.0
     * @see #updateAnswer(int, String, boolean, int)
     * @see Answer
     * @param answer    <code>Answer</code> Object that should be updated in the database
     */
    public void updateAnswer(Answer answer){
        updateAnswer(answer.getQuestionId(), answer.getText(), answer.getIsCorrect(), answer.getId());
    }

    /**
     * Delete answer by id in database
     *
     * @author  Claudia Martinez
     * @author  Marco Rensch
     * @since   1.0
     * @param answerId      <code>id</code> of the element that should be deleted
     */
    public void deleteAnswerInDb(int answerId){
        String query = "DELETE FROM tbl_answers WHERE answer_id=" + answerId;

        try (Connection con = DriverManager.getConnection(SQLConnectionData.getURL(), SQLConnectionData.getUSER(), SQLConnectionData.getPASSWORD());
             Statement statement = con.createStatement()
        ){
            statement.execute(query);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("DeleteAnswerInDb hat Fehler geworfen:");
            ex.printStackTrace();
        }
    }

    /**
     * Adds a new question to the database and returns its <code>id</code>
     *
     * @author  Claudia Martinez
     * @author  Marco Rensch
     * @since   1.0
     * @param   questionText        <code>String</code> value that holds the question text
     * @return  <code>int</code>    <code>insertedId</code> id of the added question
     */
    public int insertNewQuestion(String questionText) {
        int insertedId = 0;
        String query = "INSERT INTO tbl_questions (question_text) VALUE ('" + questionText + "')";

        try (Connection con = DriverManager.getConnection(SQLConnectionData.getURL(), SQLConnectionData.getUSER(), SQLConnectionData.getPASSWORD());
             Statement statement = con.createStatement()) {
                statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
                ResultSet rs = statement.getGeneratedKeys();
                if(rs.next()){
                    insertedId = rs.getInt(1);
                }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("insertNewQuestion hat einen Fehler geworfen:");
            ex.printStackTrace();
        }
        return insertedId;
    }

    /**
     * updates the question text for the element with the given id
     *
     * @author  Claudia Martinez
     * @author  Marco Rensch
     * @since   1.0
     * @param questionId    <code>int</code> id of the question that should be updated
     * @param questionText  <code>String</code> new text that should be set
     */
    public void updateQuestion(int questionId, String questionText) {
        String query = "UPDATE tbl_questions SET question_text='" + questionText + "' WHERE question_id=" + questionId;

        try (Connection con = DriverManager.getConnection(SQLConnectionData.getURL(), SQLConnectionData.getUSER(), SQLConnectionData.getPASSWORD());
             Statement statement = con.createStatement()){
            statement.execute(query);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("UpdateQuestion Methode hat einen Fehler geworfen:");
            ex.printStackTrace();
        }
    }

    /**
     * Deletes a question and its answers in database
     *
     * @author  Claudia Martinez
     * @author  Marco Rensch
     * @since   1.0
     *
     * @param questionId    <code>id</code> of the question that should be deleted including its answers
     */
    public void deleteQuestionAndAnswersInDb(int questionId){
        String query = "DELETE FROM tbl_questions WHERE question_id =" + questionId;
        String query2 = "DELETE FROM tbl_answers WHERE question_id=" + questionId;

        try (Connection con = DriverManager.getConnection(SQLConnectionData.getURL(), SQLConnectionData.getUSER(), SQLConnectionData.getPASSWORD());
             Statement statement = con.createStatement();
             Statement statement2 = con.createStatement()) {

            statement.executeUpdate(query2);
            statement2.executeUpdate(query);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("deleteQuestionAndAnswersInDb hat Fehler geworfen:");
            ex.printStackTrace();
        }
    }

    /**
     * Simple database connectivity check that returns an object of type <code>ConnectionCheck</code> that holds the information for this check
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     ConnectionCheck
     * @return  ConnectionCheck      object that contains the status and an additional exception message text on fail
     */
    public static ConnectionCheck simpleDatabaseCheck(){
        ConnectionCheck response = new ConnectionCheck();
        try (Connection c = DriverManager.getConnection(SQLConnectionData.getURL(), SQLConnectionData.getUSER(), SQLConnectionData.getPASSWORD())) {
            response.setSuccessfull(true);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            response.setSuccessfull(false);
            response.setErrorText(ex.getMessage());
        }

        return response;
    }
}