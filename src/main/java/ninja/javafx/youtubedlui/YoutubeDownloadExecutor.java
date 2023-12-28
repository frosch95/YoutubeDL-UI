package ninja.javafx.youtubedlui;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static ninja.javafx.youtubedlui.Configurations.config;

public class YoutubeDownloadExecutor {

    private String executablePath;
    private String url;

    private JsonObject jsonResult;

    public YoutubeDownloadExecutor(String url, Tool tool) {
        this.url = url.trim();
        this.executablePath = tool.path;
    }

    public List<VideoFormat> getFormats() {
        if (url.contains("youtube.com") || url.contains("youtu.be")) {
            var formats = getFormatsFromJsonResult();
            formats.add(new VideoFormat("bestvideo+bestaudio", "bestvideo+bestaudio"));
            return formats;
        } else {
            return getFormatsFromJsonResult();
        }
    }

    public String getTitle() {
        return jsonResult.get("title") == null ? "no title found" : jsonResult.get("title").getAsString();
    }

    public void download(String format, Consumer<String> messageConsumer) {
        try {
            var pb = new ProcessBuilder(executablePath, "-f", format, "--no-mtime", url, "-o", config().getOutput());
            System.out.println(pb.command().toString());
            pb.redirectErrorStream(true);
            var process = pb.start();
            var input = process.inputReader();
            input.lines().forEach(messageConsumer::accept);
            process.waitFor();
            var exit = process.exitValue();
            if (exit != 0) {
                throw new RuntimeException("something went wrong");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public void loadMetaData() {
        jsonResult = null;
        try {
            var pb = new ProcessBuilder(executablePath, "-j", url, "--no-warnings");
            pb.redirectErrorStream(true);
            var process = pb.start();

            var input = process.inputReader();
            var jsonResult = input.lines()
                    .filter(line -> {
                        System.out.println("THE LINE "+line);
                        return line.startsWith("{");
                    })
                    .map(line -> line + '\n').collect(Collectors.joining());
            process.waitFor();
            var exit = process.exitValue();
            System.out.println(jsonResult);
            if (exit == 0) {
                this.jsonResult = JsonParser.parseString(jsonResult).getAsJsonObject();
            } else {
                throw new RuntimeException("no information found");
            }
        } catch (final Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private List<VideoFormat> getFormatsFromJsonResult() {
        var formats = new ArrayList<VideoFormat>();
        var formatsList = jsonResult.get("formats");

        var jsonFormats = formatsList == null ? null : formatsList.getAsJsonArray();
        if (jsonFormats == null || jsonFormats.isEmpty()) {

            // maybe it has only one format
            var formatId = jsonResult.get("format_id");
            var format = jsonResult.get("format");

            if (formatId != null && format != null) {
                var videoFormat = new VideoFormat(formatId.getAsString(), format.getAsString());
                formats.add(videoFormat);
                return formats;
            }

            throw new RuntimeException("no formats found");
        }

        for (var jsonFormat: jsonFormats) {
            var jf = jsonFormat.getAsJsonObject();
            var videoFormat = new VideoFormat(jf.get("format_id").getAsString(), jf.get("format").getAsString());
            formats.add(videoFormat);
        }

        return formats;
    }

}
