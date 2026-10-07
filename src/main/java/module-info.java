module com.example.card24game {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.card24game to javafx.fxml;
    exports com.example.card24game;
}