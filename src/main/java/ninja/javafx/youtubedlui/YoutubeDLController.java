package ninja.javafx.youtubedlui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.application.Platform.runLater;

public class YoutubeDLController implements Initializable {

    @FXML
    private TextField urlText;

    @FXML
    private Button searchButton;

    @FXML
    private ComboBox<VideoFormat> qualityChooser;

    @FXML
    private Button downloadButton;

    @FXML
    private VBox infoBox;

    @FXML
    private Label titleLabel;

    @FXML
    private ProgressIndicator progress;

    private YoutubeDownloadExecutor youtubeDownloadExecutor;



    @FXML
    protected void onSearchButtonClick() {
        clearSearch();
        new Thread(() -> {
            youtubeDownloadExecutor = new YoutubeDownloadExecutor(urlText.getText());
            try {
                searching(true);
                youtubeDownloadExecutor.loadMetaData();
                setTitle(youtubeDownloadExecutor.getTitle());
                successfulSearch(youtubeDownloadExecutor.getFormats());
            } catch (Exception e) {
                setTitle(e.getMessage());
                e.printStackTrace();
            }
           searching(false);
        }).start();
    }

    @FXML
    protected void onDownloadButtonClick() {
        var selected = (VideoFormat) qualityChooser.getSelectionModel().getSelectedItem();
        try {
            var fXMLLoader = new FXMLLoader();
            var downloadView = (Parent)fXMLLoader.load(this.getClass().getResource("download-view.fxml").openStream());
            infoBox.getChildren().add(0, downloadView);
            var controller = (DownloadController)fXMLLoader.getController();
            controller.setYoutubeDownloadExecutor(youtubeDownloadExecutor);
            controller.download(selected.formatNumber());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        searchButton.setDisable(true);
        qualityChooser.setDisable(true);
        downloadButton.setDisable(true);
        urlText.textProperty().addListener((observable, oldValue, newValue) -> {
            clearSearch();
            try {
                new URL(newValue.trim());
                searchButton.setDisable(false);
            } catch (MalformedURLException e) {
               // do nothing
            }
        });
    }

    private void searching(boolean doSearch) {
        runLater(() -> {
            searchButton.setDisable(doSearch);
            titleLabel.setVisible(!doSearch);
            progress.setVisible(doSearch);
        });
    }

    private void setTitle(String title) {
        runLater(() -> titleLabel.setText(title));
    }

    private void clearSearch() {
        runLater(() -> {
            qualityChooser.setDisable(true);
            downloadButton.setDisable(true);
            qualityChooser.getItems().clear();
            titleLabel.setText("");
        });
    }

    private void successfulSearch(List<VideoFormat> formats) {
        runLater(() -> {
            qualityChooser.setItems(FXCollections.observableArrayList(formats));
            qualityChooser.setDisable(false);
            qualityChooser.getSelectionModel().selectLast();
            downloadButton.setDisable(false);
        });
    }
}