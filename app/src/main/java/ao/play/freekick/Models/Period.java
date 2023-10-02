package ao.play.freekick.Models;

public class Period {
    private String number;
    private double price;
    private String duration;

    public Period() {
    }

    public Period(String number, double price, String duration) {
        this.number = number;
        this.price = price;
        this.duration = duration;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
