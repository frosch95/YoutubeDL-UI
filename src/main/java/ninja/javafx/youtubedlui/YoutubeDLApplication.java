package ninja.javafx.youtubedlui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class YoutubeDLApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(YoutubeDLApplication.class.getResource("youtubedl-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.getIcons().add(new Image(YoutubeDLApplication.class.getResourceAsStream("/ninja/javafx/youtubedlui/143830_download_cloud_icon.png")));
        stage.setTitle("YoutubeDL UI");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}