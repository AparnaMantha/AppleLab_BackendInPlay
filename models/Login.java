package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.Basic;

public class Login {


    @Basic
    private String token;


    @JsonIgnore
      public String getToken() {
        return token;
    }

    public void setToken() {
      this.token = RandomStringUtils.randomAlphanumeric(22);
    }

}
