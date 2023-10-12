package utils;

public class AutoSuggestBoxOption {
    private boolean onlyInAvailable;
    private boolean colorCoded;

    private String regex;

    public AutoSuggestBoxOption() {
        onlyInAvailable = false;
        colorCoded = false;
        regex = ".*";
    }

    public AutoSuggestBoxOption(boolean onlyInAvailable, boolean colorCoded) {
        this.onlyInAvailable = onlyInAvailable;
        this.colorCoded = colorCoded;
    }

    public AutoSuggestBoxOption(boolean onlyInAvailable, boolean colorCoded, String regex) {
        this.onlyInAvailable = onlyInAvailable;
        this.colorCoded = colorCoded;
        this.regex = regex;
    }

    public boolean isOnlyInAvailable() {
        return onlyInAvailable;
    }

    public void setOnlyInAvailable(boolean onlyInAvailable) {
        this.onlyInAvailable = onlyInAvailable;
    }

    public boolean isColorCoded() {
        return colorCoded;
    }

    public void setColorCoded(boolean colorCoded) {
        this.colorCoded = colorCoded;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}
