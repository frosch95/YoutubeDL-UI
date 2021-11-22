package ninja.javafx.youtubedlui;

public record VideoFormat(String formatNumber, String formatDescription) {
    @Override
    public String toString() {
        return formatDescription;
    }
}
