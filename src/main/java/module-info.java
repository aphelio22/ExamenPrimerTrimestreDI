module com.example.examenprimertrimestredi {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.examenprimertrimestredi to javafx.fxml;
    exports com.example.examenprimertrimestredi;
}