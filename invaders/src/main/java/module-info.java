module com.usp {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.usp to javafx.fxml;
    exports com.usp;
}
