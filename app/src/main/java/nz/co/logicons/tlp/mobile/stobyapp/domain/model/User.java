package nz.co.logicons.tlp.mobile.stobyapp.domain.model;

/*
 * @author by Allen
 */
public class User {
    private String username;
    private String password;
    private String fcmToken;
    public User(){}
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public User(String username, String password, String fcmToken) {
        this.username = username;
        this.password = password;
        this.fcmToken = fcmToken;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
