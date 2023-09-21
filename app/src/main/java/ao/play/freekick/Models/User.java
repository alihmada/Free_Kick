package ao.play.freekick.Models;

public class User {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean isAdmin;

    public User() {
    }

    public User(String firstName, String lastName, String phoneNumber, boolean isAdmin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.isAdmin = isAdmin;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

}
