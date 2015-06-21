package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints.Required;
import play.db.ebean.Transactional;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "_meal")
public class Meal extends Model {

    @Id
    @GeneratedValue
    public Long id;

    @Required
    public String name;

    @Required
    public Double price;

    @Required
    @ManyToOne
    public Restaurant restaurant;

    public static class MealOrder {
        @Required
        public Long id;
    }

    public static class MealList {
        @Required
        public String name;

        @Required
        public Double price;
    }

    public static Finder<Long, Meal> find = new Finder<>(Meal.class);

    public static List<Meal> findAll() {
        return find.all();
    }

    public static Meal findById(Long id) {
        return find.byId(id);
    }

    public static List<Meal> findByRestaurant(Long id) { return find.where().eq("restaurant_id", id).findList();}

    public void create() {
        this.save();
    }

    public void update(){
        this.save();
    }


}


