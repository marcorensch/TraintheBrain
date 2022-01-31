module com.tbt.trainthebrain {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens com.tbt.trainthebrain to javafx.fxml;
    exports com.tbt.trainthebrain;
    exports com.tbt.trainthebrain.sqlcontroller;
    opens com.tbt.trainthebrain.sqlcontroller to javafx.fxml;
}