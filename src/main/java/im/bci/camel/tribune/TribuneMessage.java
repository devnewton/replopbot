package im.bci.camel.tribune;

import java.util.Map;
import org.apache.camel.impl.DefaultMessage;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TribuneMessage extends DefaultMessage {

    private final Element post;

    TribuneMessage(Element post) {
        this.post = post;
    }

    @Override
    protected Object createBody() {
        Elements message = post.select("message");
        for(Element a : message.select("a")) {
            a.text(a.attr("href"));
        }
        return message.text();
    }

    @Override
    protected void populateInitialHeaders(Map<String, Object> map) {
        String sender = post.select("login").text();
        if (StringUtils.isEmpty(sender)) {
            sender = post.select("info").text();
        }
        map.put(TribuneConstants.SENDER, sender);
    }
}
