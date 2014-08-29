package im.bci.replopbot.conf;

public class ReplopbotTribuneConfiguration {

    private String getUrl = "http://devnewton.bci.im/fr/chat/xml";
    private String postUrl = "http://devnewton.bci.im/fr/chat/post";

    public String getGetUrl() {
        return getUrl;
    }

    public void setGetUrl(String getUrl) {
        this.getUrl = getUrl;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

   
}
