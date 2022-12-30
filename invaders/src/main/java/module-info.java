module com.usp {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.usp to javafx.fxml;
    exports com.usp;
}
