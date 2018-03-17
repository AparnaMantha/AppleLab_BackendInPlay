package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import models.CrimeDetails;
import java.util.List;
import play.db.jpa.JPAApi;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import play.core.routing.HandlerInvokerFactory;
import play.mvc.Result;
import java.lang.*;
public class ExploreMap extends Controller {

    private JPAApi jpaApi;

    @com.google.inject.Inject
    public ExploreMap(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }


    @Transactional
    public Result getAllDetails() {

        TypedQuery<CrimeDetails> query = jpaApi.em().createQuery("SELECT c FROM CrimeDetails c", CrimeDetails.class);
        List<CrimeDetails> details = query.getResultList();

        final JsonNode json = Json.toJson(details);

        return ok(json);
    }


    @Transactional
    public Result getParticularDetails() {


        final JsonNode jsonNode = request().body().asJson();

        final String type = jsonNode.get("crime1").asText();

        final String time = jsonNode.get("time").asText();

        //  final String past = jsonNode.get("daterange").asText();

        String[] temp = type.split(" ");
        //for(int i=0;i<temp.length-1;i++)
        // Logger.debug(temp[i]);


        String[] temp1;
        String delimeter1 = " ";
        temp1 = time.split(delimeter1);

        String[] temp2 = type.split(" ");


        int len = temp.length;
        int len1 = temp1.length;
        // int len2 = temp1.length;

        String query = "type=";
        String query1 = "time=";
        // String query2 = "daterange=";

        for (String a : temp) {
            query += "'";
            query += a;
            query += "'";
            query += "or type=";
        }
        query += "'";
        query += temp[len - 1];
        query += "'";
        Logger.debug(query);


        for (int i = 0; i < len1 - 1; i++) {
            query1 += "'";
            query1 += temp1[i];
            query1 += "'";
            query1 += "or time=";
        }
        query1 += "'";
        query1 += temp1[len1 - 1];
        query1 += "'";
        Logger.debug(query1);


       /* for (String a : temp) {
            query += "'";
            query += a;
            query += "'";
            query += "or type=";
        }
        query += "'";
        query += temp[len - 1];
        query += "'";
        Logger.debug(query); */

        if (type != "" & time != "") {

            TypedQuery<CrimeDetails> det = jpaApi.em().createQuery("SELECT c FROM CrimeDetails c where ( " + query + " " +
                    ") and ( " + query1 + ")", CrimeDetails.class);
            List<CrimeDetails> details = det.getResultList();

            final JsonNode json = Json.toJson(details);

            return ok(json);
        } else if (type == "") {

            TypedQuery<CrimeDetails> det = jpaApi.em().createQuery("SELECT c FROM CrimeDetails c where " + query1, CrimeDetails.class);
            List<CrimeDetails> details = det.getResultList();

            final JsonNode json = Json.toJson(details);

            return ok(json);

        } else if (time == "") {


            TypedQuery<CrimeDetails> det = jpaApi.em().createQuery("SELECT c FROM CrimeDetails c where " + query, CrimeDetails.class);
            List<CrimeDetails> details = det.getResultList();

            final JsonNode json = Json.toJson(details);

            return ok(json);


        }
        return ok();
    }
}

/*
    @Transactional
    public Result getNearByRestaurants() {

        Logger.debug("inside getNearByRestaurants ");

        JsonNode jsonNode = request().body().asJson();
        final Double latitude = jsonNode.get("lat").asDouble();
        final Double longitude = jsonNode.get("lng").asDouble();

        Logger.debug("lat & long: " + String.valueOf(latitude + " " + longitude));

        List<F.Tuple<Restaurant, Double>> restaurants = restaurantDao.nearByRestaurants(latitude, longitude);

        if (null == restaurants) {
            return badRequest("no restaurants to display");
        }

        final JsonNode jsonNode1 = Json.toJson(restaurants);
        Logger.debug(String.valueOf(jsonNode1));

        return ok(jsonNode1);


    }
    */
