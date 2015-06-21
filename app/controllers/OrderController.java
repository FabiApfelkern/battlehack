package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.braintreegateway.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Account;
import models.Meal;
import models.Order;
import models.Transaction;
import models.service.BraintreeService;
import org.joda.time.DateTimeUtils;
import play.data.Form;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.math.BigDecimal;
import java.util.*;

public class OrderController extends Controller {

    @Transactional
    //@Security.Authenticated(Secured.class)
    public Result getAll() {
        List<Order> ordersList = new ArrayList<>();
        List<Order> orders = Order.findAll();
        for(Order o: orders){
            Order order = new Order();
            order.id = o.id;
            order.transaction = o.transaction;
            order.account = o.account;
            ordersList.add(order);
        }

        ObjectNode result = Json.newObject();
        result.put("state", "success");
        result.put("count", orders.size());
        result.set("result", Json.toJson(ordersList));
        return ok(result);
    }

    // return client_token
    // post: meal_id
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public Result post(){
        Form<Meal.MealOrder> form = Form.form(Meal.MealOrder.class).bindFromRequest();

        if(form.hasErrors()){
            ObjectNode result = Json.newObject();
            result.set("error", form.errorsAsJson());
            return badRequest(result);
        }

        Meal.MealOrder mealOrder = form.get();
        Order newOrder = new Order();

        newOrder.meal = Meal.findById(mealOrder.meal_id);

        newOrder.account = Secured.currentAccount();
        newOrder.create();

        Transaction newTransaction = new Transaction();
        newTransaction.createdAt = new Date();
        newTransaction.status = "open";
        newTransaction.create();

        newOrder.transaction = newTransaction;
        newOrder.update();

        // Generate Client-Token (nonce)
        CustomerRequest customerRequest = new CustomerRequest()
                .customerId(Secured.currentAccount().id.toString());
        com.braintreegateway.Result<Customer> result = BraintreeService.getInstance().getGateway().customer().create(customerRequest);

        ClientTokenRequest clientTokenRequest = new ClientTokenRequest().customerId(result.getTarget().getId());
        String generate = BraintreeService.getInstance().getGateway().clientToken().generate(clientTokenRequest);

        ObjectNode jsonResult = Json.newObject();
        jsonResult.put("state", "success");
        jsonResult.put("nonce", generate);
        jsonResult.put("order_id", newOrder.id);

        return ok(jsonResult);
    }

    // actual payment
    // post: order_id, payment_method_nonce
    public Result pay(Long orderId) {
        ObjectNode jsonResult = Json.newObject();

        Form<Order.OrderPay> form = Form.form(Order.OrderPay.class).bindFromRequest();

        if(form.hasErrors()){
            ObjectNode result = Json.newObject();
            result.set("error", form.errorsAsJson());
            return badRequest(result);
        }

        Order.OrderPay orderPublic = form.get();
        Order order = Order.findById(orderId);

        TransactionRequest request = new TransactionRequest()
                .amount(new BigDecimal(order.meal.price))
                .paymentMethodNonce(orderPublic.payment_method_nonce);

        com.braintreegateway.Result<com.braintreegateway.Transaction> result = BraintreeService.getInstance().getGateway().transaction().sale(request);

        BraintreeService.getInstance().getGateway().transaction();
        if (result.isSuccess()) {
            com.braintreegateway.Transaction transaction = result.getTarget();
            order.transaction.transaction_id = transaction.getId();
            order.transaction.paidAt = transaction.getCreatedAt();
            order.transaction.status = transaction.getStatus().name();
            order.transaction.update();
            jsonResult.put("state", "success");

            return ok(jsonResult);
        } else if (result.getTransaction() != null) {
            com.braintreegateway.Transaction transaction = result.getTransaction();
            System.out.println("Error processing transaction:");
            System.out.println("  Status: " + transaction.getStatus());
            System.out.println("  Code: " + transaction.getProcessorResponseCode());
            System.out.println("  Text: " + transaction.getProcessorResponseText());
        } else {
            for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
                System.out.println("Attribute: " + error.getAttribute());
                System.out.println("  Code: " + error.getCode());
                System.out.println("  Message: " + error.getMessage());
            }
        }
        jsonResult.put("state", "failed");

        return badRequest(jsonResult);

    }
}
