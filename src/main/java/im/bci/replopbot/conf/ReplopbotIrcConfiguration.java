package im.bci.replopbot.conf;

public class ReplopbotIrcConfiguration {

    private String chan = "chat.freenode.net/#devnewton";

    public String getChan() {
        return chan;
    }

    public void setChan(String ircChan) {
        this.chan = ircChan;
    }
}
