package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
//import org.json.simple.JSONObject;
public class Analysis extends Controller {


    private JPAApi jpaApi;
    int y;

    @com.google.inject.Inject
    public Analysis(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }


    @Transactional
    public Result getAnalysis() {
        Query query11 = jpaApi.em().createQuery("SELECT distinct  EXTRACT(year FROM date)  from CrimeDetails order by  EXTRACT(year FROM date) desc ");
        List<Integer> year1 = (List<Integer>) query11.getResultList();
        Logger.debug(""+year1);
        com.fasterxml.jackson.databind.node.ObjectNode result = Json.newObject();
      //  ObjectNode result = Json.newObject();
        Logger.debug(""+year1);
       /* HashMap<Integer,Integer> hah=new HashMap<>();
        hah.put(year1.get(0),6);
        hah.put(year1.get(1),6);
        final JsonNode json = Json.toJson(hah);*/

        //  hah.put(year1.get(),6);
       for(int i=0;i<2;i++) {




           Query query = jpaApi.em().createQuery("SELECT COUNT(*)  FROM CrimeDetails c where (type='assault' or type='murder' or type='Kidnapping' or type='Robbery' or type=' Sexual Assault' or type=' Sexual abuse') and ( EXTRACT(year FROM date)=" + year1.get(i) + " ) ");
            List<String> details = query.getResultList();


           result.put(String.valueOf(year1.get(i)), String.valueOf(details));


        }
        return ok(result);
//return ok(json);
    }
}
