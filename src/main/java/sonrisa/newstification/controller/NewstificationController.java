package sonrisa.newstification.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sonrisa.newstification.service.NotificationService;

import java.util.Map;

@RestController
@RequestMapping("/api/news")
public class NewstificationController {

    private final NotificationService notificationService;

    public NewstificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Value("${app.user.email}")
    private String userEmail;

    @Value("${app.user.slack-url}")
    private String userSlackWebhook;

    @PostMapping("/send-alert")
    public ResponseEntity<Map<String, String>> sendAlertToSelf(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody SendAlertRequest request) {

        String username = userDetails.getUsername();
        String destination = switch (request.channel()) {
            case EMAIL -> userEmail;
            case SLACK -> userSlackWebhook;
        };

        String subject = "Important News Alert for " + username + ": " + request.newsTitle();

        notificationService.dispatch(request.channel(), destination, subject, request.newsBody());

        return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", "Alert sent via " + request.channel() + " to authenticated user " + username
        ));
    }
}
