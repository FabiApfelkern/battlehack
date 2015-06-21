package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints.Required;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "_order")
public class Order extends Model {

    @Id
    @GeneratedValue
    public Long id;

    @OneToOne
    public Transaction transaction;

    @ManyToOne
    public Meal meal;

    @ManyToOne
    public Account account;

    @ManyToOne
    public Restaurant restaurant;

    public static class OrderPay {
        @Required
        public String payment_method_nonce;
    }

    public static class OrderRestaurant {
        public Transaction transaction;
        public Meal meal;
        public Account account;
    }

    public static Finder<Long,Order> find = new Finder<>(Order.class);

    public static List<Order> findAll() {
        return find.all();
    }

    public static Order findById(Long id) {
        return find.byId(id);
    }

    public static List<Order> findByRestaurant(Long id) { return find.where().eq("restaurant_id", id).findList();}

    public void create() {
        this.save();
    }

    public void update(){
        this.save();
    }


}


