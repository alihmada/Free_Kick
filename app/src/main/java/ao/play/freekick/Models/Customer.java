package ao.play.freekick.Models;

public class Customer {
    private String id;
    private String name;
    private String forMe;
    private String forYou;

    public Customer() {

    }

    public Customer(String id, String name, String forMe, String forYou) {
        this.id = id;
        this.name = name;
        this.forMe = forMe;
        this.forYou = forYou;
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

    public String getForMe() {
        return forMe;
    }

    public void setForMe(String forMe) {
        this.forMe = forMe;
    }

    public String getForYou() {
        return forYou;
    }

    public void setForYou(String forYou) {
        this.forYou = forYou;
    }
}
