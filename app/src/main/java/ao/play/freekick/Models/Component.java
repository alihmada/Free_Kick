package ao.play.freekick.Models;

import com.google.gson.annotations.SerializedName;

public class Component {
    @SerializedName("good")
    private boolean good;
    @SerializedName("problem")
    private String problem;

    public Component() {
    }

    public Component(boolean good, String problem) {
        this.good = good;
        this.problem = problem;
    }

    public boolean isGood() {
        return good;
    }

    public void setGood(boolean good) {
        this.good = good;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }
}
