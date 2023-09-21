package ao.play.freekick.Models;

import com.google.gson.annotations.SerializedName;

public class Controller {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("timeOfLastRepair")
    private String timeOfLastRepair;
    @SerializedName("triangle")
    private Component triangle;
    @SerializedName("circle")
    private Component circle;
    @SerializedName("cross")
    private Component cross;
    @SerializedName("square")
    private Component square;
    @SerializedName("up")
    private Component up;
    @SerializedName("right")
    private Component right;
    @SerializedName("down")
    private Component down;
    @SerializedName("left")
    private Component left;
    @SerializedName("r1")
    private Component r1;
    @SerializedName("r2")
    private Component r2;
    @SerializedName("l1")
    private Component l1;
    @SerializedName("l2")
    private Component l2;
    @SerializedName("options")
    private Component options;
    @SerializedName("share")
    private Component share;
    @SerializedName("ps")
    private Component ps;
    @SerializedName("body")
    private Component body;
    @SerializedName("touchPad")
    private Component touchPad;
    @SerializedName("socket")
    private Component socket;
    @SerializedName("rightAnalog")
    private Component rightAnalog;
    @SerializedName("leftAnalog")
    private Component leftAnalog;
    @SerializedName("battery")
    private Component battery;
    @SerializedName("innerCable")
    private Component innerCable;
    @SerializedName("tableCable")
    private Component tableCable;
    @SerializedName("motherboard")
    private Component motherboard;

    public Controller() {
    }

    public Controller(String id, String name, String timeOfLastRepair, Component triangle, Component circle, Component cross, Component square, Component up, Component right, Component down, Component left, Component r1, Component r2, Component l1, Component l2, Component options, Component share, Component ps, Component body, Component touchPad, Component socket, Component rightAnalog, Component leftAnalog, Component battery, Component innerCable, Component tableCable, Component motherboard) {
        this.id = id;
        this.name = name;
        this.timeOfLastRepair = timeOfLastRepair;
        this.triangle = triangle;
        this.circle = circle;
        this.cross = cross;
        this.square = square;
        this.up = up;
        this.right = right;
        this.down = down;
        this.left = left;
        this.r1 = r1;
        this.r2 = r2;
        this.l1 = l1;
        this.l2 = l2;
        this.options = options;
        this.share = share;
        this.ps = ps;
        this.body = body;
        this.touchPad = touchPad;
        this.socket = socket;
        this.rightAnalog = rightAnalog;
        this.leftAnalog = leftAnalog;
        this.battery = battery;
        this.innerCable = innerCable;
        this.tableCable = tableCable;
        this.motherboard = motherboard;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeOfLastRepair() {
        return timeOfLastRepair;
    }

    public void setTimeOfLastRepair(String timeOfLastRepair) {
        this.timeOfLastRepair = timeOfLastRepair;
    }

    public Component getTriangle() {
        return triangle;
    }

    public void setTriangle(Component triangle) {
        this.triangle = triangle;
    }

    public Component getCircle() {
        return circle;
    }

    public void setCircle(Component circle) {
        this.circle = circle;
    }

    public Component getCross() {
        return cross;
    }

    public void setCross(Component cross) {
        this.cross = cross;
    }

    public Component getSquare() {
        return square;
    }

    public void setSquare(Component square) {
        this.square = square;
    }

    public Component getUp() {
        return up;
    }

    public void setUp(Component up) {
        this.up = up;
    }

    public Component getRight() {
        return right;
    }

    public void setRight(Component right) {
        this.right = right;
    }

    public Component getDown() {
        return down;
    }

    public void setDown(Component down) {
        this.down = down;
    }

    public Component getLeft() {
        return left;
    }

    public void setLeft(Component left) {
        this.left = left;
    }

    public Component getR1() {
        return r1;
    }

    public void setR1(Component r1) {
        this.r1 = r1;
    }

    public Component getR2() {
        return r2;
    }

    public void setR2(Component r2) {
        this.r2 = r2;
    }

    public Component getL1() {
        return l1;
    }

    public void setL1(Component l1) {
        this.l1 = l1;
    }

    public Component getL2() {
        return l2;
    }

    public void setL2(Component l2) {
        this.l2 = l2;
    }

    public Component getOptions() {
        return options;
    }

    public void setOptions(Component options) {
        this.options = options;
    }

    public Component getShare() {
        return share;
    }

    public void setShare(Component share) {
        this.share = share;
    }

    public Component getPs() {
        return ps;
    }

    public void setPs(Component ps) {
        this.ps = ps;
    }

    public Component getBody() {
        return body;
    }

    public void setBody(Component body) {
        this.body = body;
    }

    public Component getTouchPad() {
        return touchPad;
    }

    public void setTouchPad(Component touchPad) {
        this.touchPad = touchPad;
    }

    public Component getSocket() {
        return socket;
    }

    public void setSocket(Component socket) {
        this.socket = socket;
    }

    public Component getRightAnalog() {
        return rightAnalog;
    }

    public void setRightAnalog(Component rightAnalog) {
        this.rightAnalog = rightAnalog;
    }

    public Component getLeftAnalog() {
        return leftAnalog;
    }

    public void setLeftAnalog(Component leftAnalog) {
        this.leftAnalog = leftAnalog;
    }

    public Component getBattery() {
        return battery;
    }

    public void setBattery(Component battery) {
        this.battery = battery;
    }

    public Component getInnerCable() {
        return innerCable;
    }

    public void setInnerCable(Component innerCable) {
        this.innerCable = innerCable;
    }

    public Component getTableCable() {
        return tableCable;
    }

    public void setTableCable(Component tableCable) {
        this.tableCable = tableCable;
    }

    public Component getMotherboard() {
        return motherboard;
    }

    public void setMotherboard(Component motherboard) {
        this.motherboard = motherboard;
    }
}
