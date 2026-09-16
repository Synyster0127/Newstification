package sonrisa.newstification.channel;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class SlackNotificationChannel implements NotificationChannel {

    private final RestTemplate slackTemplate;

    public SlackNotificationChannel(RestTemplate slackTemplate) {
        this.slackTemplate = slackTemplate;
    }

    @Override
    public ChannelType getType() {
        return ChannelType.SLACK;
    }

    @Override
    public void send(String destinationUrl, String subject, String content) {
        System.out.printf("[SLACK LOG] Posting to Webhook: %s%n", destinationUrl);
        try {
            String fullMessage = String.format("*%s*\n%s", subject, content);
            Map<String, String> payload = Map.of("text", fullMessage);
            slackTemplate.postForEntity(destinationUrl, payload, String.class);
        } catch (Exception e) {
            System.err.printf("[SLACK WARN] Failed to post to Slack: %s%n", e.getMessage());
        }
    }

}
