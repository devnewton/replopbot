package im.bci.camel.tribune;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

public class TribuneComponent extends DefaultComponent {

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        TribuneConfiguration configuration = new TribuneConfiguration();
        setProperties(configuration, parameters);
        TribuneEndPoint endPoint = new TribuneEndPoint(uri, this, configuration);
        return endPoint;
    }

}
