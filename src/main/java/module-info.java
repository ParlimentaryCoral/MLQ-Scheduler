module com.example.mlqschedulingso {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.mlqschedulingso to javafx.fxml;
    exports com.example.mlqschedulingso;
}