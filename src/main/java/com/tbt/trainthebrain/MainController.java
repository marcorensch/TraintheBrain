package com.tbt.trainthebrain;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends AppController implements Initializable {

    @FXML
    AnchorPane mainRoot;

    @FXML
    HBox mainHeader, menuContainer, notes, errorContainerOutter;

    @FXML
    Button learnModeBtn, editModeBtn;

    @FXML
    Text errText, errDesc;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fadeInTransition(mainHeader, 1000,300);
        fadeInTransition(menuContainer, 1000,500);
        fadeInTransition(notes,1000,600);
    }
}