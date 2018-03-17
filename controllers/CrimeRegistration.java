package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import daos.RegistrationDaoImpl;
import java.text.SimpleDateFormat;
import java.util.Date;
import models.CrimeDetails;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
//import java.sql.Date;
import java.text.DateFormat;

public class CrimeRegistration extends Controller {

    private RegistrationDaoImpl RegistrationDao;

    @Inject
    public CrimeRegistration(RegistrationDaoImpl RegistrationDao) {
        this.RegistrationDao = RegistrationDao;
    }

   // static Integer id=1;
    @Transactional
    public Result registerCrime() throws Exception {
        final JsonNode jsonNode = request().body().asJson();
        Logger.debug("jsonNode {}", jsonNode);

        final String date = jsonNode.get("day").asText();
       java.util.Date date1 = new SimpleDateFormat("yyyy-mm-dd").parse(date);
       // DateFormat formatter = new SimpleDateFormat("yyyy--dd");
       // Date date1 = formatter.parse(date);
      //  System.out.println(date);


        final String type = jsonNode.get("type").asText();
        final String description = jsonNode.get("address").asText();
        final String gender = jsonNode.get("gender").asText();
        final String gender1 = jsonNode.get("gender1").asText();
//        final Integer year = jsonNode.get("year").asInt();
        final Double latitude = jsonNode.get("lat").asDouble();
        final Double longitude = jsonNode.get("lng").asDouble();
        final String country = jsonNode.get("country").asText();
        final String time = jsonNode.get("time").asText();
        //Calendar calendar = Calendar.getInstance();
        //java.util.Date now = calendar.getTime();
       // java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

       // id++;

        if (null == date) {
            return badRequest("Missing day");
        }

        if (null == date1) {
            return badRequest("Missing day");
        }

        if (null == type) {
            return badRequest("Missing crimetype");
        }

        if (null == description) {
            return badRequest("Missing description");
        }

        if (null == gender) {
            return badRequest("Missing gender of the Victim");
        }

        if (null == gender1) {
            return badRequest("Missing gender of the Criminal");
        }



        if (null == country) {
            return badRequest("Missing country");
        }

        if (null == latitude){
            return badRequest("Missing Latitude");
        }

        if (null == longitude){
            return badRequest("Missing Longitude");
        }

        if (null == time){
            return badRequest("Missing time of the day");
        }


        CrimeDetails crime = new CrimeDetails();


        crime .setType(type);
        crime .setAddress(description);
        crime .setGender(gender);
        crime .setGender1(gender1);
        crime .setLat(latitude);
        crime .setLng(longitude);
        crime .setCountry(country);
        crime .setTime(time);
        crime .setDate(date1);


        Logger.debug("date1    "+date1);

         RegistrationDao.persist(crime);
        com.fasterxml.jackson.databind.node.ObjectNode json = Json.newObject();

        //json.put("type", crime.getType());

       // return created(json);


        return ok();
    }






}