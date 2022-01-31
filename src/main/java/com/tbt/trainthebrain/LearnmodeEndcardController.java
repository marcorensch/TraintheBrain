package com.tbt.trainthebrain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LearnmodeEndcardController extends AppController implements Initializable {

    @FXML
    HBox summaryContent;

    @FXML
    Text percentCorrectText,userPointsText,maxPointsText;

    double countOfQuestions, maxPts, userPts;
    int fullyCorrectCount = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    /**
     * Custom Initialisation for Learnmode Endcard scene calculates user points and creates Pie chart for fully correct answers.
     * Places user and max points into the scene as well as calculating percentage of correct answered questions
     *
     *
     * @author  Marco Rensch
     * @author  Claudia Martinez
     * @since   1.0
     * @see     ArrayList
     * @param   results <code>ArrayList</code> object that holds the <code>QuestionResults</code> of each played <code>Question</code>
     */
    public void initResults(ArrayList<QuestionResult> results) {

        countOfQuestions = results.size();
        for (QuestionResult qr: results) {
            maxPts += qr.countAnswers;
            userPts += qr.pts;
            if(qr.fullyCorrect) fullyCorrectCount++;
        }

        DecimalFormat df = new DecimalFormat("#.#");

        percentCorrectText.setText(df.format((100 / countOfQuestions) * fullyCorrectCount) +"%");
        userPointsText.setText(Double.toString(userPts));
        maxPointsText.setText(Double.toString(maxPts));

        int incorrectAnswered = (int) (countOfQuestions - fullyCorrectCount);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                                                        new PieChart.Data(fullyCorrectCount + " korrekt", fullyCorrectCount),
                                                        new PieChart.Data(incorrectAnswered + " falsch", (countOfQuestions - fullyCorrectCount))
                                                    );
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("");

        summaryContent.getChildren().add(chart);

    }
}
