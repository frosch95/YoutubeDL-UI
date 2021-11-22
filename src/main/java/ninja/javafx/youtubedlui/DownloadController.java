package ninja.javafx.youtubedlui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static javafx.application.Platform.runLater;

public class DownloadController implements Initializable {

    @FXML
    private Label titleLabel;

    @FXML
    private Label infoLabel;

    @FXML
    private ProgressBar progress;

    @FXML
    private Button retryButton;

    private YoutubeDownloadExecutor youtubeDownloadExecutor;

    private String selected;

    public void download(String selected) {
        this.selected = selected;
        resetProgress();
        new Thread(() -> {
            try {
                setTitle(youtubeDownloadExecutor.getTitle());
                youtubeDownloadExecutor.download(selected, this::info);
                success();
            } catch (Exception e) {
                e.printStackTrace();
                error();
            }
        }).start();
    }

    private void resetProgress() {
        progress.styleProperty().setValue("");
        retryButton.setVisible(false);
        progress.setProgress(0.0);
        progress.setDisable(false);
        titleLabel.setDisable(false);
        infoLabel.setDisable(false);
    }

    private void setTitle(String title) {
        runLater(() -> titleLabel.setText(title));
    }

    private void setProgress(double progressValue) {
        runLater(() -> progress.setProgress(progressValue));
    }

    private void error() {
        runLater(() -> {
            progress.setStyle("-fx-accent: red");
            progress.setProgress(1.0);
            progress.setDisable(true);
            titleLabel.setDisable(true);
            infoLabel.setDisable(true);
            retryButton.setVisible(true);
        });
    }

    private void success() {
        runLater(() -> {
            progress.setStyle("-fx-accent: green");
            progress.setProgress(1.0);
            progress.setDisable(true);
            titleLabel.setDisable(true);
            infoLabel.setDisable(true);
            retryButton.setVisible(false);
        });
    }

    private void info(String text) {
        try {
            var pattern = Pattern.compile("\\s(.*?)%");
            var matcher = pattern.matcher(text);
            if (matcher.find())
            {
                var progressValue = Double.parseDouble(matcher.group(1).trim());
                progressValue = progressValue / 100;
                setProgress(progressValue);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        runLater(() -> infoLabel.setText(text));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setProgress(0);
        retryButton.setVisible(false);
    }

    public void setYoutubeDownloadExecutor(YoutubeDownloadExecutor youtubeDownloadExecutor) {
        this.youtubeDownloadExecutor = youtubeDownloadExecutor;
    }

    public void onRetryButtonClick() {
        download(selected);
    }
}
