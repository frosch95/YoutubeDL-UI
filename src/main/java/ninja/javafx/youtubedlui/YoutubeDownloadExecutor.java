package ninja.javafx.youtubedlui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class YoutubeDownloadExecutor {

    private String executablePath = "youtube-dl.exe";
    private String url;
    private String cachedTitle;

    public YoutubeDownloadExecutor(String url) {
        this.url = url.trim();
    }

    public List<VideoFormat> getFormats() {
        if (url.contains("youtube.com")) {
            var formats = loadFormatsFromWeb();
            formats.add(new VideoFormat("bestvideo+bestaudio", "bestvideo+bestaudio"));
            return formats;
        } else {
            return loadFormatsFromWeb();
        }
    }

    public String getTitle() {
        if (cachedTitle != null) return cachedTitle;
        try {
            var pb = new ProcessBuilder(executablePath, "-e", url);
            pb.redirectErrorStream(true);
            var process = pb.start();
            var input = process.inputReader();
            String title = input.readLine();
            process.waitFor();
            var exit = process.exitValue();
            if (exit != 0) {
                throw new RuntimeException("Cannot get title!");
            } else {
                cachedTitle = title;
                return cachedTitle == null ? "" : cachedTitle;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public void download(String format, Consumer<String> messageConsumer) {
        try {
            var pb = new ProcessBuilder(executablePath, "-f", format, url);
            pb.redirectErrorStream(true);
            var process = pb.start();
            var input = process.inputReader();
            String line;
            while ((line = input.readLine()) != null) {
                messageConsumer.accept(line);
            }
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

    private List<VideoFormat> loadFormatsFromWeb() {
        var formats = new ArrayList<VideoFormat>();
        try {
            var pb = new ProcessBuilder(executablePath, "-F", url);
            pb.redirectErrorStream(true);
            var process = pb.start();

            var input = process.inputReader();

            boolean startFormats = false;
            String line;
            while ((line = input.readLine()) != null) {
                var result = line.split("\\s+");

                if (result.length > 0) {
                    if (startFormats) {
                        formats.add(new VideoFormat(result[0], line));
                    } else {
                        if (result[0].equals("format")) {
                            startFormats = true;
                        }
                    }
                }
            }
            process.waitFor();
            var exit = process.exitValue();
            if (exit == 0 && !formats.isEmpty()) {
                return formats;
            } else {
                throw new RuntimeException("no formats found");
            }
        } catch (final Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
