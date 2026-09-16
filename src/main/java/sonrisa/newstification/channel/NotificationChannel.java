package sonrisa.newstification.channel;

public interface NotificationChannel {

    ChannelType getType();
    void send(String destination, String subject, String content);

}
