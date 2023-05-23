package ao.play.freekick.Classes;

public class Device {
    private String header, startingTime, endingTime;
    private int spinnerVisibility, spinnerIndex;
    private boolean solo;
    private boolean payment;
    private boolean running;

    public Device() {
    }

    public Device(String header, String startingTime, String endingTime, int spinnerVisibility, int spinnerIndex, boolean solo, boolean payment, boolean running) {
        this.header = header;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.spinnerVisibility = spinnerVisibility;
        this.spinnerIndex = spinnerIndex;
        this.solo = solo;
        this.payment = payment;
        this.running = running;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
    }

    public String getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(String endingTime) {
        this.endingTime = endingTime;
    }

    public int getSpinnerVisibility() {
        return spinnerVisibility;
    }

    public void setSpinnerVisibility(int spinnerVisibility) {
        this.spinnerVisibility = spinnerVisibility;
    }

    public int getSpinnerIndex() {
        return spinnerIndex;
    }

    public void setSpinnerIndex(int spinnerIndex) {
        this.spinnerIndex = spinnerIndex;
    }

    public boolean isSolo() {
        return solo;
    }

    public void setSolo(boolean solo) {
        this.solo = solo;
    }

    public boolean isPayment() {
        return payment;
    }

    public void setPayment(boolean payment) {
        this.payment = payment;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
