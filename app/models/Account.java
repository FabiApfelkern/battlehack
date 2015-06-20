package models;

import play.data.validation.Constraints;
import play.data.validation.Constraints.Required;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Fabi on 19.06.2015.
*/

@javax.persistence.Entity
public class Account {

    @Id
    @GeneratedValue
    public Long id;

    @Required
    public String name;

    @Required
    public String email;

    @SuppressWarnings("unchecked")
    public static List<Account> findAll() {
        return JPA.em().createQuery("FROM Account").getResultList();
    }

    public void create() {
        JPA.em().persist(this);
    }

}


