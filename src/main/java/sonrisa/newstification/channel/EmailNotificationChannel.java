package sonrisa.newstification.channel;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationChannel implements NotificationChannel{

    private final JavaMailSender mailSender;

    public EmailNotificationChannel(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public ChannelType getType() {
        return ChannelType.EMAIL;
    }

    @Override
    public void send(String destination, String subject, String content) {
        System.out.printf("[EMAIL LOG] Sending email to '%s' | Subject: '%s'%n", destination, subject);
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(destination);
            mail.setSubject(subject);
            mail.setText(content);
            mailSender.send(mail);
        } catch (Exception e) {
            System.err.printf("[EMAIL WARN] Failed to send email to %s: %s%n", destination, e.getMessage());
        }
    }

}
