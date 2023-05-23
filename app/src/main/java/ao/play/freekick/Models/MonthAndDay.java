package ao.play.freekick.Models;

public class MonthAndDay {
    private double price;
    private String number;
    private String arabic_name;
    private String english_name;
    private String duration;

    public MonthAndDay() {
    }

    public MonthAndDay(double price, String number, String arabic_name, String english_name, String duration) {
        this.price = price;
        this.number = number;
        this.arabic_name = arabic_name;
        this.english_name = english_name;
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }

    public String getNumber() {
        return number;
    }

    public String getArabic_name() {
        return arabic_name;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public String getDuration() {
        return duration;
    }
} //End MonthAndDay
