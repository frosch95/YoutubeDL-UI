module ninja.javafx.youtubedlui {
    requires javafx.controls;
    requires javafx.fxml;

    opens ninja.javafx.youtubedlui to javafx.fxml;
    exports ninja.javafx.youtubedlui;
}