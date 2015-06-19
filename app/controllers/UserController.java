package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.User;
import play.*;
import play.data.Form;
import play.db.jpa.Transactional;
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
        List<User> users = User.findAll();
        return ok(Json.toJson(users));
    }

    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result post(){
        Form<User> form = Form.form(User.class).bindFromRequest();
        if(form.hasErrors()){
            ObjectNode result = Json.newObject();
            result.put("error", form.errorsAsJson());

            return badRequest(result);
        }
        JsonNode json = request().body().asJson();
        User newUser = Json.fromJson(json, User.class);
        newUser.create();
        return ok(Json.toJson(newUser));
    }
}
