package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints.Required;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "_order")
public class Meal extends Model {

    @Id
    @GeneratedValue
    public Long id;

    @OneToOne
    public Transaction transaction;

    public Meal meal;

    @Required
    public String name;

    @ManyToOne
    public Account account;

    public static Finder<Long, Meal> find = new Finder<>(Meal.class);

    public static List<Meal> findAll() {
        return find.all();
    }

    public static Meal findById(Long id) {
        return find.byId(id);
    }

    public void create() {
        this.save();
    }

    public void update(){
        this.save();
    }


}


