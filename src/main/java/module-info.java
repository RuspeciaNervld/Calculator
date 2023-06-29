module com.example.calculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.calculator to javafx.fxml;
    exports com.calculator;
    exports com.calculator.models;
    opens com.calculator.models to javafx.fxml;
}