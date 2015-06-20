package controllers;

import com.braintreegateway.*;
import com.braintreegateway.test.Nonce;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.service.BraintreeService;
import play.*;
import play.libs.Json;
import play.mvc.*;

import play.mvc.Result;
import views.html.*;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Map;


public class Application extends Controller {

    @Inject
    BraintreeService bs;

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    // return client_token
    public Result payButton(String customerId) {

        CustomerRequest customerRequest = new CustomerRequest()
                .customerId(customerId);
        com.braintreegateway.Result<Customer> result = BraintreeService.getInstance().getGateway().customer().create(customerRequest);

        ClientTokenRequest clientTokenRequest = new ClientTokenRequest().customerId(result.getTarget().getId());
        String generate = BraintreeService.getInstance().getGateway().clientToken().generate(clientTokenRequest);

        ObjectNode jsonResult = Json.newObject();
        jsonResult.put("nonce", generate);

        return ok(jsonResult);
    }

    public Result paymentMethods() {

        Map<String, String[]> values = request().body().asFormUrlEncoded();

        TransactionRequest request = new TransactionRequest()
                .amount(new BigDecimal("100.00"))
                .paymentMethodNonce(values.get("payment_method_nonce")[0]);

        com.braintreegateway.Result<Transaction> result = BraintreeService.getInstance().getGateway().transaction().sale(request);

        if (result.isSuccess()) {
            Transaction transaction = result.getTarget();
            System.out.println("Success!: " + transaction.getId());
            return ok(transaction.getId());
        } else if (result.getTransaction() != null) {
            Transaction transaction = result.getTransaction();
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
        return badRequest();

    }

}
