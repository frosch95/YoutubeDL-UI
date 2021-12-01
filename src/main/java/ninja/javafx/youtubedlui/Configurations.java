package ninja.javafx.youtubedlui;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import static java.nio.file.Files.exists;
import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;

public class Configurations {

    private final static Configurations INSTANCE = new Configurations();
    private Configuration config;

    private Configurations() {
        try {
            var path = get("./config.json");
            if (exists(path)) {
                var reader = newBufferedReader(path);
                var gson = new Gson();
                config = gson.fromJson(reader, Configuration.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Configurations config() {
        return INSTANCE;
    }

    public List<Tool> getExecutables() {
        return null != config ? asList(config.tools) : asList(new Tool("youtube-dl", "youtube-dl.exe"));
    }

    public String getOutput() {
        return getOutputPath() + "/" + getOutputTemplate();
    }

    public String getOutputPath() {
        return null != config ? config.outputPath : ".";
    }

    public String getOutputTemplate() {
        return null != config ? config.outputTemplate : "%(title)s-%(id)s.%(ext)s";
    }
}
