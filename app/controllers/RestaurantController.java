package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Account;
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
        List<Restaurant> restaurantsList = Restaurant.findAll();

        ObjectNode restaurantResult = Json.newObject();
        restaurantResult.put("state", "success");
        restaurantResult.put("count", restaurantsList.size());
        restaurantResult.set("result", Json.toJson(restaurantsList));

        return ok(restaurantResult);
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
}
