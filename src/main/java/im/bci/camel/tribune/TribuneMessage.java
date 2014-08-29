package im.bci.camel.tribune;

import java.util.Map;

import im.bci.camel.tribune.backend.Post;

import org.apache.camel.impl.DefaultMessage;
import org.apache.commons.lang.StringUtils;

public class TribuneMessage extends DefaultMessage {

    private Post post;

    public TribuneMessage(Post post) {
        this.post = post;
    }
    
    @Override
    protected Object createBody() {
        return post.getMessage();
    }
    
    @Override
    protected void populateInitialHeaders(Map<String, Object> map) {
        String sender = post.getLogin();
        if(StringUtils.isEmpty(sender)) {
            sender = post.getInfo();
        }
        map.put(TribuneConstants.SENDER, sender);
    }
}
