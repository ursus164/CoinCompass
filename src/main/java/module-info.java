module data {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.apache.logging.log4j;
    requires org.json;
    requires com.fasterxml.jackson.databind;

    opens com.data.gui to javafx.fxml;
    exports com.data.gui;
}