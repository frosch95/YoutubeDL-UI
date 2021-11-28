package ninja.javafx.youtubedlui;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configurations {

    private final static Configurations INSTANCE = new Configurations();
    private Properties configProperties = new Properties();

    private Configurations() {
        try {
            configProperties.loadFromXML(new FileInputStream("./config.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Configurations config() {
        return INSTANCE;
    }

    public String getExecutable() {
        return configProperties.getProperty("executable", "youtube-dl.exe");
    }

    public String getOutputPath() {
        return configProperties.getProperty("outputPath", ".");
    }
}
