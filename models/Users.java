package models;

import com.fasterxml.jackson.annotation.*;
import scala.Int;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import play.Logger;

@Entity
//@JsonIgnoreProperties({ "password" , "email" , "salt", "role"})
public class Users {

    public enum Role {
        Admin, Client, User
    }

    @Id
    String  email;

    @Basic
    String password;

    @Basic
    String userName;

    @Basic
    Role role;

    @Basic
    @JsonProperty("access_token")
    private String token;

    @Basic
    @JsonProperty("token_expiry")
    private Long tokenExpire;

    @Basic
    @JsonProperty("refresh_token")
    private String refreshToken;

    @Basic
    private String salt;

    //@JsonManagedReference
    //@OneToMany(mappedBy = "owner")
    //private List<CrimeDetails> crimeDetails;


    public Users() {

    }

    public Users (String userName){
        this.userName = userName;
    }

    /*public Users(String userName, List<CrimeDetails> crimeDetails) {
        this.userName = userName;
        this.crimeDetails = crimeDetails;
    }
*/
    public String getUserName() {

        Logger.debug("inside username method");
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {  this.token = token; }

    public Long getTokenExpire() {
        return tokenExpire;
    }

    public void setTokenExpire(Long tokenExpire) { this.tokenExpire = tokenExpire; }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    /*public List<CrimeDetails> getCrimeDetails() {
        return crimeDetails;
    }

    public void setCrimeDetails(List<CrimeDetails> crimeDetails) {
        this.crimeDetails = crimeDetails;
    }*/
}