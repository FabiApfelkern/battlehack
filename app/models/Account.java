package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import models.enums.Roles;
import play.data.validation.Constraints;
import play.data.validation.Constraints.Required;
import com.avaje.ebean.Model;
import play.db.ebean.Transactional;

import javax.inject.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
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
    @Constraints.Email
    public String email;

    @Required
    public String token;

    @Required
    public String password;

    @Required
    @Enumerated(EnumType.ORDINAL)
    public Roles role;

    public static class AccountPublic {
        public Long id;
        public String name;
        public String email;
        public Roles role;
    }

    public static class AccountRegister {
        @Required
        public String name;

        @Required
        @Constraints.Email
        public String email;

        @Required
        public String password;

        @Required
        @Enumerated(EnumType.ORDINAL)
        public Roles role;

        @Transactional
        public String validate() {
            Account account = Account.findByEmail(email);
            if (account != null) {
                return "Email Address already in use.";
            }
            return null;
        }
    }

    public static class AccountLogin {
        @Required
        public String email;

        @Required
        public String password;

        @Transactional
        public String validate() {
            if (Account.authenticate(email, password) == null) {
                return "Either or email or your password is not valid.";
            }
            return null;
        }
    }

    public static Finder<Long,Account> find = new Finder<>(Account.class);

    public static List<Account> findAll() {;
        return find.all();
    }

    public static Account findById(Long id) {
        return find.byId(id);
    }

    public static Account findByToken(String token){
        List<Account> result = find.where().eq("token", token).findList();
        if(result.size() == 1){
            return result.get(0);
        } else {
            return null;
        }
    }

    public static Account findByEmail(String email){
        List<Account> result = find.where().eq("email", email).findList();
        if(result.size() == 1){
            return result.get(0);
        } else {
            return null;
        }
    }

    public static Account authenticate(String email, String password) {
        Account account = Account.findByEmail(email);
        if(account == null) {
            return null;
        }
        String md5Pass = Util.md5(password);
        if(md5Pass.equals(account.password)){
            return account;
        } else {
            return null;
        }
    }

    public void create() {
        this.password = Util.md5(this.password);
        this.save();
    }

    public void update(){
        this.save();
    }


}


