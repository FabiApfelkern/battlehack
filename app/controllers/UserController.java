package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Account;
import play.*;
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
    public Result getAll() {
        List<Account> users = Account.findAll();
        return ok(Json.toJson(users));
    }

    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result post(){
        Form<Account> form = Form.form(Account.class).bindFromRequest();
        if(form.hasErrors()){
            ObjectNode result = Json.newObject();
            result.put("error", form.errorsAsJson());

            return badRequest(result);
        }
        JsonNode json = request().body().asJson();
        Account newUser = Json.fromJson(json, Account.class);
        newUser.create();
        return ok(Json.toJson(newUser));
    }
}
