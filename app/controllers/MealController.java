package controllers;

import models.service.BraintreeService;
import play.mvc.*;

import play.mvc.Result;
import views.html.*;

import javax.inject.Inject;


public class MealController extends Controller {

    @Inject
    BraintreeService bs;

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }




}
