module ninja.javafx.youtubedlui {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens ninja.javafx.youtubedlui to javafx.fxml, com.google.gson;
    exports ninja.javafx.youtubedlui;
}