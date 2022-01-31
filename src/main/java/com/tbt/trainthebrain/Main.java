package com.tbt.trainthebrain;

import com.tbt.trainthebrain.sqlcontroller.ConnectionCheck;
import com.tbt.trainthebrain.sqlcontroller.DBTasks;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 960.0, 850.0);
        MainController sceneController = fxmlLoader.getController();
        stage.setTitle("TtB - Train the Brain");
        stage.setMinWidth(960.0);
        stage.setMinHeight(700.0);
        stage.setScene(scene);
        stage.show();

        // check Datenbankverbindung
        ConnectionCheck check = DBTasks.simpleDatabaseCheck();
        if(!check.isSuccessfull()){
            sceneController.statusDb = false;
            sceneController.learnModeBtn.setDisable(true);
            sceneController.editModeBtn.setDisable(true);
            sceneController.errText.setText("Fehler bei der Verbindung mit der Datenbank aufgetreten:");
            sceneController.errDesc.setText(check.getErrorText());
            sceneController.errorContainerOutter.setVisible(true);

            sceneController.fadeInTransition(sceneController.errorContainerOutter,1000,300);
        }

    }

    public static void main(String[] args) {
        launch();
    }
}