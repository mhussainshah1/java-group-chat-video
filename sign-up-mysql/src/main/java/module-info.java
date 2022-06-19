module com.example.signupmysql {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.signupmysql to javafx.fxml;
    exports com.example.signupmysql;
}