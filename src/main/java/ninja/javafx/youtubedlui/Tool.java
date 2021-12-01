package ninja.javafx.youtubedlui;

public class Tool {

    public String name;
    public String path;

    public Tool(){
    }

    public Tool(String name, String path) {
        this.name = name;
        this.path = path;
    }

    @Override
    public String toString() {
        return name;
    }
}
