package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Date;

@Entity
public class CrimeDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;


    @Basic
    java.util.Date date;


    @Basic
    String type;

    @Basic
    String gender;

    @Basic
    String gender1;

    @Basic
    String address;


    @Basic
    String country;



   @Basic
    Double lat;

    @Basic
    Double lng;

    @Basic
    String time;




    //  @Basic
   //Timestamp time;




   // public CrimeDetails(String userName, String password) {
     //   this.userName = userName;
       // this.password = password;
    //}

    /*public CrimeDetails(String userName) {
        this.userName = userName;
    }*/


    public CrimeDetails () {

    }

  /* public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id=id;
    }
*/

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonIgnore
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public void setGender1(String gender1) {
        this.gender1 = gender1;
    }

    public String getGender1() {
        return gender1;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }


    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLat() {
        return lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLng() {
        return lng;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    /*public void setTime(Timestamp time) { this.time = time;
    }

    public Timestamp getTime() {
        return time;
    }*/



}