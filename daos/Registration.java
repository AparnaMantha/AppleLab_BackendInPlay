package daos;

import models.CrimeDetails;

import java.util.List;

public interface Registration<E>  {

    E persist(E entity);

   // List<Users> findAll();

}
