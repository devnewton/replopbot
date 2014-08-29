package im.bci.camel.tribune;

import java.util.Collections;
import java.util.Comparator;

import im.bci.camel.tribune.backend.Board;
import im.bci.camel.tribune.backend.Post;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;

public class TribuneConsumer extends ScheduledPollConsumer {

    private final TribuneConfiguration configuration;
    private Client client;
    private long lastId = -1;

    public TribuneConsumer(Endpoint endpoint, Processor processor, TribuneConfiguration configuration) {
        super(endpoint, processor);
        this.configuration = configuration;
        this.setDelay(1000);
        client = ClientBuilder.newBuilder().build();
    }

    @Override
    protected int poll() throws Exception {
        WebTarget target = client.target(configuration.getGetUrl());
        Board board = target.request().get(Board.class);
        int nbPostPolled = 0;
        long maxId = -1;
        Collections.sort(board.getPost(), new Comparator<Post>() {

            public int compare(Post p1, Post p2) {
                return Long.compare(p1.getId(), p2.getId());
            }
        });
        for (Post post : board.getPost()) {
            if (post.getId() > lastId) {
                if (lastId > 0) {
                    Exchange ex = getEndpoint().createExchange();
                    ex.setIn(new TribuneMessage(post));
                    getProcessor().process(ex);
                    ++nbPostPolled;
                }
                maxId = Math.max(maxId, post.getId());
            }
        }
        lastId = Math.max(maxId, lastId);
        return nbPostPolled;
    }

}
