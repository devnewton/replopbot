package im.bci.camel.tribune;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

public class TribuneEndPoint extends DefaultEndpoint {

    private final TribuneConfiguration configuration;

    public TribuneEndPoint(String endpointuri, Component component, TribuneConfiguration configuration) {
        super(endpointuri, component);
        this.configuration = configuration;
    }

    public Producer createProducer() throws Exception {
        return new TribuneProducer(this, configuration);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new TribuneConsumer(this, processor, configuration);
    }

    public boolean isSingleton() {
        return true;
    }

}
