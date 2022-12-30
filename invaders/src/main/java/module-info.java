module com.usp {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.usp to javafx.fxml;
    exports com.usp;

    opens com.usp.engine to javafx.fxml;
    exports com.usp.engine;

    opens com.usp.elements to com.usp.engine;
    exports com.usp.elements;
}
