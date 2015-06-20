package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Meal;
import models.Order;
import models.Restaurant;
import play.data.Form;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;


public class RestaurantController extends Controller {

    @Transactional
    //@Security.Authenticated(Secured.class)
    public Result getAll() {

        List<Meal> mealList = new ArrayList<>();
        List<Meal> users = Meal.findAll();
        Form<Restaurant> form = Form.form(Restaurant.class).bindFromRequest();
        if(form.hasErrors()){
            ObjectNode result = Json.newObject();
            result.set("error", form.errorsAsJson());
            return badRequest(result);
        }
        Restaurant restaurant = form.get();

        List<Order> ordersList = new ArrayList<>();


        List<Order> ordersList = new ArrayList<>();
        List<Order> orders = Order.findAll();
        for(Order o: orders){
            Order order = new Order();
            order.name = o.name;
            order.id = o.id;
            order.transaction = o.transaction;
            order.account = o.account;
            ordersList.add(order);
        }

        ObjectNode result = Json.newObject();
        result.put("state", "success");
        result.put("count", orders.size());
        result.set("result", Json.toJson(ordersList));
        return ok(result);
    }




}
