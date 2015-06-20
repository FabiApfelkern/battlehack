package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Account;
import models.Util;
import play.data.Form;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Fabi on 19.06.2015.
 */
public class UserController extends Controller {

    @Transactional
    @Security.Authenticated(Secured.class)
    public Result getAll() {
        List<Account.AccountPublic> saveUsers = new ArrayList<>();
        List<Account> users = Account.findAll();
        for(Account  u: users){
            Account.AccountPublic ap = new Account.AccountPublic();
            ap.name = u.name;
            ap.email = u.email;
            ap.role = u.role;
            ap.id = u.id;
            saveUsers.add(ap);
        }
        ObjectNode result = Json.newObject();
        result.put("state", "success");
        result.put("count", users.size());
        result.set("result", Json.toJson(saveUsers));
        return ok(result);
    }

    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result post(){
        Form<Account.AccountRegister> form = Form.form(Account.AccountRegister.class).bindFromRequest();
        if(form.hasErrors()){
            ObjectNode result = Json.newObject();
            result.set("error", form.errorsAsJson());
            return badRequest(result);
        }
        Account.AccountRegister ar = form.get();
        Account newUser = new Account();
        newUser.name = ar.name;
        newUser.email= ar.email;
        newUser.role = ar.role;
        newUser.password = ar.password;
        newUser.token = Util.createToken();
        newUser.create();
        return ok(Json.toJson(newUser));
    }

    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result login(){
        Form<Account.AccountLogin> form = Form.form(Account.AccountLogin.class).bindFromRequest();
        if(form.hasErrors()){
            ObjectNode result = Json.newObject();
            result.set("error", form.errorsAsJson());
            return badRequest(result);
        }
        Account account = Account.findByEmail(form.get().email);
        ObjectNode result = Json.newObject();
        result.put("id", account.id);
        result.put("token", account.token);
        result.put("name", account.name);
        result.put("email", account.email);
        result.put("role", account.role.toString());
        return ok(result);
    }
}
