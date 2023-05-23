package ao.play.freekick.Models;

public class RevenueDeviceData {
    String start, end, state, time, price;

    public RevenueDeviceData(String start, String end, String state, String time, String price) {
        this.start = start;
        this.end = end;
        this.state = state;
        this.time = time;
        this.price = price;
    }

    public RevenueDeviceData() {

    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
