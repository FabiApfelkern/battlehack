package controllers;

import models.Meal;
import play.mvc.*;

import play.mvc.Result;
import views.html.*;


public class Application extends Controller {

    public Result index() {
        return ok(index.render(Meal.findAll()));
    }




}
