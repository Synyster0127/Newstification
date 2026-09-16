package sonrisa.newstification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class NewstificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewstificationApplication.class, args);
	}

	@Bean
	public RestTemplate slackTemplate() {
		return new RestTemplate();
	}
}
