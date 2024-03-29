package nz.co.logicons.tlp.mobile.stobyapp.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
 * @author by Allen
 */
public class UserDto {
    private String username;
    private String password;
    @SerializedName("fcmToken")
    @Expose()
    private String fcmToken;
    public UserDto(){}
    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public UserDto(String username, String password, String fcmToken) {
        this.username = username;
        this.password = password;
        this.fcmToken = fcmToken;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
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
        return "UserDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", fcmToken='" + fcmToken + '\'' +
                '}';
    }
}
