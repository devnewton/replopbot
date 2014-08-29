package im.bci.camel.tribune;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.lang.ObjectUtils;

public class TribuneProducer extends DefaultProducer {

    private TribuneConfiguration configuration;
    private Client client;

    public TribuneProducer(Endpoint endpoint, TribuneConfiguration configuration) {
        super(endpoint);
        this.configuration = configuration;
        client = ClientBuilder.newBuilder().build();
    }

    public void process(Exchange exchange) throws Exception {
        String message = exchange.getIn().getBody(String.class);
        WebTarget target = client.target(configuration.getPostUrl());
        Form form = new Form();
        form.param("message", message);
        String sender = ObjectUtils.toString(exchange.getIn().getHeader(TribuneConstants.SENDER));
        if (!sender.isEmpty()) {
            form.param("nickname", sender);
        }
        target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
    }

}
