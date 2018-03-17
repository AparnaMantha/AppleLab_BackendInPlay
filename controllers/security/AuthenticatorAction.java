package controllers.security;

import com.google.inject.Inject;
import daos.UserDao;
import daos.SignUpDao;
import models.Users;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class AuthenticatorAction extends Action.Simple {

    private final static Logger.ALogger LOGGER = Logger.of(AuthenticatorAction.class);

    private UserDao userDao;

    private SignUpDao signUpDao;




    @Inject
    public AuthenticatorAction(SignUpDao signUpDao) { this.signUpDao= signUpDao; }


    public CompletionStage<Result> call(Http.Context ctx) {

        LOGGER.debug("AuthenticatorAction");


        final Optional<String> header = ctx.request().header("Authorization");
        LOGGER.debug("Header: {}", header);
        if (!header.isPresent()) {
            return CompletableFuture.completedFuture(unauthorized());
        }


        if (!header.get().startsWith("Bearer ")) {
            return CompletableFuture.completedFuture(unauthorized());
        }

        final String accessToken = header.get().substring(7);
        if (accessToken.isEmpty()) {
            return CompletableFuture.completedFuture(unauthorized());
        }
        LOGGER.debug("Access token: {}", accessToken);

        Users user = userDao.findAccessToken(accessToken);
        if (null == user) {
            return CompletableFuture.completedFuture(unauthorized());
        }

                /*Long expiryTime = new Timestamp(System.currentTimeMillis()).getTime();
                if ((expiryTime > user.findValue("expiry_token").asLong() )){
                    return CompletableFuture.completedFuture(unauthorized());
                }*/

        LOGGER.debug("Users: {}", user);

        ctx.args.put("user", user);

        return delegate.call(ctx);
    }

}