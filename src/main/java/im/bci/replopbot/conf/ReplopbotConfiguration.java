package im.bci.replopbot.conf;

public class ReplopbotConfiguration {

    private String nickname = "replopbot";

    private ReplopbotXmppConfiguration xmmp = new ReplopbotXmppConfiguration();

    private ReplopbotIrcConfiguration irc = new ReplopbotIrcConfiguration();

    private ReplopbotTribuneConfiguration tribune = new ReplopbotTribuneConfiguration();

    public ReplopbotIrcConfiguration getIrc() {
        return irc;
    }

    public void setIrc(ReplopbotIrcConfiguration irc) {
        this.irc = irc;
    }

    public ReplopbotXmppConfiguration getXmmp() {
        return xmmp;
    }

    public void setXmmp(ReplopbotXmppConfiguration xmmp) {
        this.xmmp = xmmp;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ReplopbotTribuneConfiguration getTribune() {
        return tribune;
    }

    public void setTribune(ReplopbotTribuneConfiguration tribune) {
        this.tribune = tribune;
    }

}
