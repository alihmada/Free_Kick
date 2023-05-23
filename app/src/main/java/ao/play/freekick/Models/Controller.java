package ao.play.freekick.Models;

public class Controller {
    String id, timeOfLastRepair, body, rightAnalog, leftAnalog, xo, stock, option, share, table, r1r2, l1l2, socket, battery, innerCable, motherboard;

    public Controller() {
    }

    public Controller(String id, String timeOfLastRepair, String body, String rightAnalog, String leftAnalog, String xo, String stock, String option, String share, String table, String r1r2, String l1l2, String socket, String battery, String innerCable, String motherboard) {
        this.id = id;
        this.timeOfLastRepair = timeOfLastRepair;
        this.body = body;
        this.rightAnalog = rightAnalog;
        this.leftAnalog = leftAnalog;
        this.xo = xo;
        this.stock = stock;
        this.option = option;
        this.share = share;
        this.table = table;
        this.r1r2 = r1r2;
        this.l1l2 = l1l2;
        this.socket = socket;
        this.battery = battery;
        this.innerCable = innerCable;
        this.motherboard = motherboard;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimeOfLastRepair() {
        return timeOfLastRepair;
    }

    public void setTimeOfLastRepair(String timeOfLastRepair) {
        this.timeOfLastRepair = timeOfLastRepair;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getRightAnalog() {
        return rightAnalog;
    }

    public void setRightAnalog(String rightAnalog) {
        this.rightAnalog = rightAnalog;
    }

    public String getLeftAnalog() {
        return leftAnalog;
    }

    public void setLeftAnalog(String leftAnalog) {
        this.leftAnalog = leftAnalog;
    }

    public String getXo() {
        return xo;
    }

    public void setXo(String xo) {
        this.xo = xo;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getR1r2() {
        return r1r2;
    }

    public void setR1r2(String r1r2) {
        this.r1r2 = r1r2;
    }

    public String getL1l2() {
        return l1l2;
    }

    public void setL1l2(String l1l2) {
        this.l1l2 = l1l2;
    }

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getInnerCable() {
        return innerCable;
    }

    public void setInnerCable(String innerCable) {
        this.innerCable = innerCable;
    }

    public String getMotherboard() {
        return motherboard;
    }

    public void setMotherboard(String motherboard) {
        this.motherboard = motherboard;
    }
}
