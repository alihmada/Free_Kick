package ao.play.freekick.Models;

public class CustomerDetails {
    private String id;
    private String letter;
    private String value;
    private String time;

    public CustomerDetails() {
    }

    public CustomerDetails(String id, String letter, String value, String time) {
        this.id = id;
        this.letter = letter;
        this.value = value;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
