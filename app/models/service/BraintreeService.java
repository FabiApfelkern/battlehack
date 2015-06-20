package models.service;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.typesafe.config.ConfigFactory;

public class BraintreeService {

    private static BraintreeService instance = null;
    private static BraintreeGateway gateway = null;

    public static BraintreeService getInstance() {
        if (instance == null) {
            instance = new BraintreeService();
        }
        return instance;
    }

    public BraintreeGateway getGateway() {
        return gateway;
    }

    public BraintreeService() {
        gateway = new com.braintreegateway.BraintreeGateway(
                Environment.SANDBOX,
                ConfigFactory.load().getString("braintree.merchantID"),
                ConfigFactory.load().getString("braintree.publicKey"),
                ConfigFactory.load().getString("braintree.privateKey")
        );
    }
}
