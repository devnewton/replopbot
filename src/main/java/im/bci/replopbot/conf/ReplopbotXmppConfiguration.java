package im.bci.replopbot.conf;

public class ReplopbotXmppConfiguration {
    private String server = "bci.im";
    private String room = "devnewton@conference.bci.im";
    private String user = "replopbot";
    private String password = "re plop plop";

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
