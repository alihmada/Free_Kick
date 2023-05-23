package ao.play.freekick.Models;

public class YearAndDevice {
    private String number;
    private double price;
    private String duration;

    public YearAndDevice() {
    }

    public YearAndDevice(String number, double price, String duration) {
        this.number = number;
        this.price = price;
        this.duration = duration;
    }

    public String getNumber() {
        return number;
    }

    public double getPrice() {
        return price;
    }

    public String getDuration() {
        return duration;
    }
}
