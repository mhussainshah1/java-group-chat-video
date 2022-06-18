module com.example.clientfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.clientfx to javafx.fxml;
    exports com.example.clientfx;
}