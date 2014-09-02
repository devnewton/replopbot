package im.bci.camel.tribune;

import java.util.Collections;
import java.util.Comparator;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class TribuneConsumer extends ScheduledPollConsumer {

    private final TribuneConfiguration configuration;
    private long lastId = -1;

    public TribuneConsumer(Endpoint endpoint, Processor processor, TribuneConfiguration configuration) {
        super(endpoint, processor);
        this.configuration = configuration;
        this.setDelay(1000);
    }

    @Override
    protected int poll() throws Exception {
        Document doc = Jsoup.connect(configuration.getGetUrl())
                .parser(Parser.xmlParser())
                .get();
        Elements posts = doc.select("post");
        int nbPostPolled = 0;
        long maxId = -1;
        Collections.sort(posts, new Comparator<Element>() {

            public int compare(Element p1, Element p2) {
                return p1.attr("id").compareTo(p2.attr("id"));
            }
        });
        for (Element post : posts) {
            long postId = Long.parseLong(post.attr("id"));
            if (postId > lastId) {
                if (lastId > 0) {
                    Exchange ex = getEndpoint().createExchange();
                    ex.setIn(new TribuneMessage(post));
                    getProcessor().process(ex);
                    ++nbPostPolled;
                }
                maxId = Math.max(maxId, postId);
            }
        }
        lastId = Math.max(maxId, lastId);
        return nbPostPolled;
    }

}
