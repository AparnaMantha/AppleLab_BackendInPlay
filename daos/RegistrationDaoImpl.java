package daos;

import com.google.inject.Inject;
import models.CrimeDetails;
import play.db.jpa.JPAApi;

public  class RegistrationDaoImpl implements Registration<CrimeDetails> {

    private JPAApi jpaApi;


   @Inject
    public RegistrationDaoImpl(JPAApi jpaApi) { this.jpaApi = jpaApi; }

    public CrimeDetails persist(CrimeDetails crime) {

        jpaApi.em().persist(crime);

        return crime;
    }


}

