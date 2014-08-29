package im.bci.replopbot;

import im.bci.camel.tribune.TribuneConstants;
import im.bci.replopbot.conf.ReplopbotConfiguration;
import java.io.File;
import java.io.IOException;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.irc.IrcConstants;
import org.apache.camel.component.xmpp.XmppConstants;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

public class Replopbot {

    public static void main(String[] args) throws Exception {

        final ReplopbotConfiguration config = loadConfiguration();

        final String xmppEndPoint = "xmpp://" + config.getXmmp().getServer() + "/?room=" + config.getXmmp().getRoom() + "&nickname=" + config.getNickname() + "&user=" + config.getXmmp().getUser() + "&password=" + config.getXmmp().getPassword();
        final String ircEndPoint = "irc:" + config.getNickname() + "@" + config.getIrc().getChan();
        final String tribuneEndPoint = "tribune:devnewton?getUrl=" + config.getTribune().getGetUrl() + "&postUrl=" + config.getTribune().getPostUrl();

        final CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            public void configure() {

                // xmpp to irc and tribune
                from(xmppEndPoint).filter(new Predicate() {

                    public boolean matches(Exchange ex) {
                        String sender = ObjectUtils.toString(ex.getIn().getHeader(XmppConstants.FROM));
                        if (StringUtils.contains(sender, config.getNickname())) {
                            return false;
                        }
                        return true;
                    }
                }).process(new Processor() {

                    public void process(Exchange ex) throws Exception {
                        Message in = ex.getIn();
                        String sender = ObjectUtils.toString(in.getHeader(XmppConstants.FROM));
                        if (!sender.isEmpty()) {
                            in.setHeader(TribuneConstants.SENDER, config.getNickname() + "/" + sender);
                        }
                    }
                }).to(tribuneEndPoint).process(new Processor() {

                    public void process(Exchange ex) throws Exception {
                        Message in = ex.getIn();
                        String sender = ObjectUtils.toString(in.getHeader(XmppConstants.FROM));
                        if (!sender.isEmpty()) {
                            in.setBody(sender + ": " + in.getBody(String.class));
                        }
                    }
                }).to(ircEndPoint).process(new Processor() {

                    public void process(Exchange ex) throws Exception {
                        System.out.println(ex.getIn().getHeaders());
                        System.out.println(ex.getIn().getBody(String.class));
                    }
                });

                // irc to xmpp and tribune
                from(ircEndPoint).filter(new Predicate() {

                    public boolean matches(Exchange ex) {
                        String sender = ObjectUtils.toString(ex.getIn().getHeader(IrcConstants.IRC_USER_NICK));
                        if (StringUtils.equals(sender, "ChanServ")) {
                            return false;
                        }
                        if (StringUtils.contains(sender, config.getNickname())) {
                            return false;
                        }
                        return true;
                    }
                }).process(new Processor() {

                    public void process(Exchange ex) throws Exception {
                        Message in = ex.getIn();
                        String sender = ObjectUtils.toString(in.getHeader(IrcConstants.IRC_USER_NICK));
                        if (!sender.isEmpty()) {
                            in.setHeader(TribuneConstants.SENDER, config.getNickname() + "/" + sender);
                        }
                    }
                }).to(tribuneEndPoint).process(new Processor() {
                    public void process(Exchange ex) throws Exception {
                        Message in = ex.getIn();
                        String sender = ObjectUtils.toString(in.getHeader(IrcConstants.IRC_USER_NICK));
                        if (!sender.isEmpty()) {
                            in.setBody(sender + ": " + in.getBody(String.class));
                        }
                    }
                }).to(xmppEndPoint).process(new Processor() {

                    public void process(Exchange ex) throws Exception {
                        System.out.println(ex.getIn().getHeaders());
                        System.out.println(ex.getIn().getBody(String.class));
                    }
                });

                // tribune to irc and xmpp
                from(tribuneEndPoint).filter(new Predicate() {

                    public boolean matches(Exchange ex) {
                        String sender = ObjectUtils.toString(ex.getIn().getHeader(TribuneConstants.SENDER));
                        if (StringUtils.contains(sender, config.getNickname())) {
                            return false;
                        }
                        if (StringUtils.isBlank(sender)) { // probably some
                            // douchebag moules
                            return false;
                        }
                        return true;
                    }
                }).process(new Processor() {
                    public void process(Exchange ex) throws Exception {
                        Message in = ex.getIn();
                        String sender = ObjectUtils.toString(in.getHeader(TribuneConstants.SENDER));
                        if (!sender.isEmpty()) {
                            in.setBody(sender + ": " + in.getBody(String.class));
                        }
                    }
                }).to(xmppEndPoint).to(ircEndPoint).process(new Processor() {

                    public void process(Exchange ex) throws Exception {
                        System.out.println(ex.getIn().getHeaders());
                        System.out.println(ex.getIn().getBody(String.class));
                    }
                });
            }
        });

        context.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    context.stop();
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        });

        for (;;) {
            Thread.sleep(1000);
        }

    }

    private static ReplopbotConfiguration loadConfiguration() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(org.codehaus.jackson.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(org.codehaus.jackson.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        return mapper.readValue(new File("replopbot.conf"), ReplopbotConfiguration.class);
    }
}
