package ao.play.freekick.Models;

public class Month extends Period {
    private String arabic_name;
    private String english_name;

    public Month() {
    }

    public Month(String number, double price, String duration, String arabic_name, String english_name) {
        super(number, price, duration);
        this.arabic_name = arabic_name;
        this.english_name = english_name;
    }

    public String getArabic_name() {
        return arabic_name;
    }

    public void setArabic_name(String arabic_name) {
        this.arabic_name = arabic_name;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }
} //End Month
