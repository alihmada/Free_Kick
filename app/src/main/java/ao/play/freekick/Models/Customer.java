package ao.play.freekick.Models;

import java.io.Serializable;

public class Customer {
    private String id;
    private String name;
    private String forHim;
    private String forYou;

    public Customer() {

    }

    public Customer(String id, String name, String forHim, String forYou) {
        this.id = id;
        this.name = name;
        this.forHim = forHim;
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

    public String getForHim() {
        return forHim;
    }

    public void setForHim(String forHim) {
        this.forHim = forHim;
    }

    public String getForYou() {
        return forYou;
    }

    public void setForYou(String forYou) {
        this.forYou = forYou;
    }
}
