package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints.Required;

import javax.persistence.*;
import java.util.List;

@Entity
public class Order extends Model {

    @Id
    @GeneratedValue
    public Long id;

    @Required
    public String name;

    @ManyToOne
    public Account account;

    @ManyToOne
    public BillingInformation billingInformation;

    public static Finder<Long,Order> find = new Finder<>(Order.class);

    public static List<Order> findAll() {;
        return find.all();
    }

    public static Order findById(Long id) {
        return find.byId(id);
    }

    public void create() {
        this.password = Util.md5(this.password);
        this.save();
    }

    public void update(){
        this.save();
    }


}


