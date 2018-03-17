package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.security.Authenticator;
import daos.UserDao;
import jdk.nashorn.internal.ir.ObjectNode;
import models.Users;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TypedQuery;



public class UserController extends Controller {

//    private final static Logger.ALogger LOGGER = Logger.of(UserController.class);

    private UserDao userDao;


    @Inject
    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    public Result createUser() throws NoSuchAlgorithmException {

        Logger.debug("inside CreateUser method");

        final JsonNode jsonNode = request().body().asJson();
        final String userName = jsonNode.get("name").asText();
        final String email = jsonNode.get("email").asText();
        final String password = jsonNode.get("password").asText();

        if (null == userName) {
            return badRequest("Missing userName");
        }

        if (null == email) {
            return badRequest("Missing email");
        }

        if (null == password) {
            return badRequest("Missing password");
        }

    /*        if (null != userDao.getUserByName(userName) || null != userDao.getUserByEmail(email)) {
            Logger.debug("user already exists");
            return badRequest("user already exists");
        }*/

        Users user = new Users();
        user.setUserName(userName);
        user.setEmail(email);
        user.setRole(Users.Role.User);

        String salt = userDao.generateSalt();
        user.setSalt(salt);

        String hashedPassword = userDao.hashedPassword(password, salt, 30);
        user.setPassword(hashedPassword);

         userDao.persist(user);

        Logger.debug("User created");
        return ok();
        //return created(user.getUserName().toString());
    }


    @Transactional
    public Result login() throws NoSuchAlgorithmException {

        Logger.debug("inside login method");

        final JsonNode jsonNode = request().body().asJson();
        final String email = jsonNode.get("email").asText();
        final String pwd = jsonNode.get("password").asText();

        if (null == email) {
            return badRequest("Missing userName");
        }

        if (null == pwd) {
            return badRequest("Missing password");
        }

        Logger.debug("Entering email function");
        final Users user = userDao.getUserByEmail(email);
        Logger.debug("Got result in login");

        if (null != user) {

            String salt = user.getSalt().toString();

            String hashedPassword = userDao.hashedPassword(pwd, salt, 30);

            if (user.getPassword().equals(hashedPassword)) {

                String accessToken = userDao.generateAccessToken();
                user.setToken(accessToken);

                String refreshToken = userDao.generateRefreshToken();
                user.setRefreshToken(refreshToken);

                Long expiry = userDao.generateExpiryTime();
                user.setTokenExpire(expiry);

                userDao.persist(user);


                com.fasterxml.jackson.databind.node.ObjectNode result = Json.newObject();
                result.put("access_token", accessToken);
                result.put("token_expiry", expiry);
                result.put("refresh_token", refreshToken);
                result.put("role", user.getRole().toString());

                Logger.debug("end of login successful");

                return ok(result);
            }

            return unauthorized("incorrect password");
        }

        return unauthorized("login unsuccessful!!");

    }


    @Transactional
    @Authenticator
    public Result getCurrentUser() {

        Logger.debug("Get current user");

        final Users user = (Users) ctx().args.get("user");

        Logger.debug("User: {}", user);
        com.fasterxml.jackson.databind.node.ObjectNode json = Json.newObject();
        json.put("userName", user.getUserName());
        json.put("email", user.getEmail());
        json.put("role", user.getRole().toString());

        return ok(json);
    }

    @Transactional
    public Result getAllUsers() {

        final List<Users> users = userDao.findAll();

        final JsonNode jsonNode1 = Json.toJson(users);

        return ok(jsonNode1);
    }

    @Transactional
    @Authenticator
    public Result changePassword() throws NoSuchAlgorithmException {

        final JsonNode jsonNode = request().body().asJson();
        final String oldPassword = jsonNode.get("old_password").asText();
        final String newPassword = jsonNode.get("new_password").asText();

        if (null == oldPassword) {
            return badRequest("Missing current password");
        }

        if (null == newPassword) {
            return badRequest("Missing new password");
        }

        Logger.debug("Got current user");

        final Users user = (Users) ctx().args.get("user");

        String salt = user.getSalt();
        String hashedPassword = userDao.hashedPassword(oldPassword, salt, 30);

        if (user.getPassword().equals(hashedPassword)) {
            Logger.debug("passwords matched");

            String newHashedPassword = userDao.hashedPassword(newPassword, salt, 30);
            user.setPassword(newHashedPassword);
            userDao.persist(user);

            return ok("Changed the password");
        }

        return badRequest("enter correct password");

    }

    @Transactional
    public Result resetAccessToken() {

        final JsonNode jsonNode = request().body().asJson();
        final String refreshToken = jsonNode.get("refresh_token").asText();

        Users user = userDao.findRefreshToken(refreshToken);

        if (user.getRefreshToken().equals(refreshToken)) {

            String accessToken = userDao.generateAccessToken();
            user.setToken(accessToken);

            JsonNode json = Json.toJson(userDao.persist(user));

            return ok(json);

        }

        return badRequest();
    }

            /*@Transactional
            @Authenticator
            public Result updateRole(){
                final JsonNode user = (JsonNode) ctx().args.get("user");
                return ok();
            }*/
}