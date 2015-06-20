package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Account;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Http;
import play.mvc.Results;
import play.mvc.Security;

/**
 * Created by Fabi on 20.06.2015.
 */
public class Secured extends Security.Authenticator {

    public final static String AUTH_TOKEN_HEADER = "Authorization";

    @Override
    public String getUsername(Http.Context ctx) {
        Account account = null;
        String[] authTokenHeaderValues = ctx.request().headers().get(AUTH_TOKEN_HEADER);
        if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)) {
            account = Account.findByToken(authTokenHeaderValues[0]);
            if (account != null) {
                ctx.args.put("account", account);
                return account.id.toString();
            }
        }
        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return forbiddenMessage();
    }

    public static Account currentAccount() {
        Account account = (Account)Http.Context.current().args.get("account");
        if(account == null) {
            return null;
        } else {
            return account;
        }
    }

    public static Result forbiddenMessage(){
        ObjectNode result = Json.newObject();
        result.put("error", "Forbidden");
        return forbidden(result);
    }

    public static Boolean usersGet(){
        Account account = currentAccount();
        if(account == null){
            return false;
        }
        else if(account.id == 13) {
            return true;
        } else {
            return false;
        }
    }
}
