package sonrisa.newstification.service;

import org.springframework.stereotype.Service;
import sonrisa.newstification.channel.ChannelType;
import sonrisa.newstification.channel.NotificationChannel;

import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    private final Map<ChannelType, NotificationChannel> channels;

    public NotificationService(List<NotificationChannel> channelList) {
        this.channels = channelList.stream()
                .collect(java.util.stream.Collectors.toMap(NotificationChannel::getType, c -> c));
    }

    public void dispatch(ChannelType type, String destination, String subject, String content) {
        NotificationChannel channel = channels.get(type);
        if (channel == null) {
            throw new IllegalArgumentException("Unsupported channel type: " + type);
        }
        channel.send(destination, subject, content);
    }

}
