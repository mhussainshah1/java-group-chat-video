module com.example.serverfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.serverfx to javafx.fxml;
    exports com.example.serverfx;
}