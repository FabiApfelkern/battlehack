package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints.Required;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "_meal")
public class Restaurant extends Model {

    @Id
    @GeneratedValue
    public Long id;

    @Required
    public String name;

    @Required
    public BigDecimal price;

    public static Finder<Long, Restaurant> find = new Finder<>(Restaurant.class);

    public static List<Restaurant> findAll() {
        return find.all();
    }

    public static Restaurant findById(Long id) {
        return find.byId(id);
    }

    public void create() {
        this.save();
    }

    public void update(){
        this.save();
    }


}


