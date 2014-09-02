package im.bci.camel.tribune;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.lang.ObjectUtils;
import org.jsoup.Jsoup;

public class TribuneProducer extends DefaultProducer {

    private final TribuneConfiguration configuration;

    public TribuneProducer(Endpoint endpoint, TribuneConfiguration configuration) {
        super(endpoint);
        this.configuration = configuration;
    }

    public void process(Exchange exchange) throws Exception {
        String message = exchange.getIn().getBody(String.class);
        Jsoup.connect(configuration.getPostUrl())
                .data("message", message)
                .data("nickname", ObjectUtils.toString(exchange.getIn().getHeader(TribuneConstants.SENDER)))
                .post();
    }

}
