package models;

import play.data.validation.Constraints;
import play.data.validation.Constraints.Required;
import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Fabi on 19.06.2015.
*/

@Entity
public class Account extends Model {

    @Id
    @GeneratedValue
    public Long id;

    @Required
    public String name;

    @Required
    public String email;

    public static Finder<Long,Account> find = new Finder<>(
            Long.class, Account.class
    );

    public static List<Account> findAll() {
        return find.all();
    }

    public void create() {
        this.save();
    }

}


