package daos;


import com.google.inject.Inject;
import models.Users;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.List;
import play.Logger;

public class UserDao implements SignUpDao<Users> {


    private JPAApi jpaApi;


    @Inject
    public UserDao(JPAApi jpaApi) { this.jpaApi = jpaApi; }


    public Users persist(Users user) {

        jpaApi.em().persist(user);

        return user;
    }


    public List<Users> findAll() {

        TypedQuery<Users> query = jpaApi.em().createQuery("SELECT u FROM User u", Users.class);
        List<Users> users = query.getResultList();

        return users;
    }

    @Transactional
    public Users getUserByName(String name) {

        Logger.debug("inside username method");
        String str = "SELECT u FROM User u WHERE u.userName = :name";
        TypedQuery<Users> query;
        query = jpaApi.em().createQuery(str, Users.class);
        query.setParameter("name", name);

        List<Users> result = query.getResultList();

        if(result.isEmpty()){
            return null;
        }

        Users user = result.get(0);
        return user;
    }

    @Transactional
    public Users getUserByEmail(String email) {

       // String str = "SELECT u FROM User u WHERE u.email = :name";
        //TypedQuery<Users> query = jpaApi.em().createQuery(str, Users.class);
        //query.setParameter("name", email);
        Users user = jpaApi.em().find(Users.class, email);
        //List<Users> result = query.getResultList();

        /*if(result.isEmpty()){
            return null;
        }

        Users user = result.get(0);*/
        return user;
    }

    public static String generateAccessToken() {


        SecureRandom random = new SecureRandom();

        long longToken = Math.abs( random.nextLong() );
        String token = Long.toString( longToken, 16 );

        return token;
    }

    public static String generateRefreshToken ( ){

        SecureRandom random = new SecureRandom();

        long refToken = Math.abs(random.nextLong());
        final String refreshToken = Long.toString(refToken, 16);

        return refreshToken;
    }

    public static Long generateExpiryTime ( ){

        Timestamp timestamp = new Timestamp(System.currentTimeMillis() + (12*60*60*1000));
        Long expiry = timestamp.getTime() ;

        return expiry;
    }

    public Users findAccessToken(String accessToken){

        String str = "SELECT u FROM User AS u WHERE u.token = :token";
        TypedQuery<Users> query = jpaApi.em().createQuery(str, Users.class);
        query.setParameter("token", accessToken);

        List<Users> result = query.getResultList();

        if(result.isEmpty()){
            return null;
        }

        Users user = result.get(0);
        return user;

    }

    public Users findRefreshToken(String refreshToken){

        String str = "SELECT u FROM User AS u WHERE u.refreshToken = :token";
        TypedQuery<Users> query = jpaApi.em().createQuery(str, Users.class);
        query.setParameter("token", refreshToken);

        List<Users> result = query.getResultList();

        if(result.isEmpty()){
            return null;
        }

        Users user = result.get(0);
        return user;
    }

    public String generateSalt(){

        SecureRandom random = new SecureRandom();

        Integer intSalt = Math.abs( random.nextInt() );
        String salt = Integer.toString( intSalt, 8 );

        return salt;
    }

    public String hashedPassword(String password, String salt, int iteration) throws NoSuchAlgorithmException {

        String saltPwd = salt.concat(password);

        MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
        final byte[] hash = mDigest.digest(saltPwd.getBytes(StandardCharsets.UTF_8));

        StringBuffer hexString = new StringBuffer();

        for(int i=0; i< iteration; i++){

            String hex = Integer.toHexString(0xff & hash[i] );
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public void updatePassword(String newPassword, String name) {

        Query query = jpaApi.em().createQuery( "UPDATE User u SET u.password = :password WHERE u.userName = :name");
        query.setParameter("password", newPassword);
        query.setParameter("name", name);
        query.executeUpdate();


    }

}