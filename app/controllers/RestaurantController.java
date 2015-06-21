package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import play.data.Form;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;


public class RestaurantController extends Controller {

    @Transactional
    //@Security.Authenticated(Secured.class)
    public Result getAll() {
        List<Restaurant> restaurantsList = Restaurant.findAll();

        ObjectNode restaurantResult = Json.newObject();
        restaurantResult.put("state", "success");
        restaurantResult.put("count", restaurantsList.size());
        restaurantResult.set("result", Json.toJson(restaurantsList));

        return ok(restaurantResult);
    }

    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result post(){
        Form<Restaurant> form = Form.form(Restaurant.class).bindFromRequest();

        if(form.hasErrors()){
            ObjectNode result = Json.newObject();
            result.set("error", form.errorsAsJson());
            return badRequest(result);
        }

        Restaurant ar = form.get();
        Restaurant newRestaurant = new Restaurant();
        newRestaurant.name = ar.name;
        newRestaurant.create();

        ObjectNode jsonResult = Json.newObject();
        jsonResult.put("state", "success");

        return ok(Json.toJson(jsonResult));
    }

    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result postMeals(Long restaurantId){

        Form<Meal> form = Form.form(Meal.class).bindFromRequest();
        List<Meal> mealList = Meal.findByRestaurant(restaurantId);

        if(form.hasErrors()){
            ObjectNode result = Json.newObject();
            result.set("error", form.errorsAsJson());
            return badRequest(result);
        }

        Meal mealRequest = form.get();
        Meal newMeal = new Meal();
        newMeal.name = mealRequest.name;
        newMeal.price = mealRequest.price;
        newMeal.restaurant = mealRequest.restaurant;

        newMeal.create();

        ObjectNode jsonResult = Json.newObject();
        jsonResult.put("state", "success");

        return ok(Json.toJson(jsonResult));
    }

    @Transactional
    //@Security.Authenticated(Secured.class)
    public Result getMeals(Long restaurantId) {
        List<Meal.MealList> saveMeals = new ArrayList<>();
        List<Meal> mealList = Meal.findByRestaurant(restaurantId);

        for(Meal meal: mealList){
            Meal.MealList mealsPublic = new Meal.MealList();
            mealsPublic.name = meal.name;
            mealsPublic.price = meal.price;
            saveMeals.add(mealsPublic);
        }

        ObjectNode result = Json.newObject();
        result.put("state", "success");
        result.put("count", saveMeals.size());
        result.set("result", Json.toJson(saveMeals));
        return ok(result);
    }

    @Transactional
    //@Security.Authenticated(Secured.class)
    public Result getOrders(Long restaurantId) {
        List<Order.OrderRestaurant> saveOrder = new ArrayList<>();
        List<Order> orderList = Order.findByRestaurant(restaurantId);

        for(Order order: orderList){
            Order.OrderRestaurant ordersPublic = new Order.OrderRestaurant();
            ordersPublic.account = order.account;
            ordersPublic.meal = order.meal;
            ordersPublic.transaction = order.transaction;
            saveOrder.add(ordersPublic);
        }

        ObjectNode result = Json.newObject();
        result.put("state", "success");
        result.put("count", saveOrder.size());
        result.set("result", Json.toJson(saveOrder));
        return ok(result);
    }
}
