package sonrisa.newstification;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NewstificationApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private JavaMailSender mailSender;

	@MockitoBean
	private RestTemplate restTemplate;

	@Test
	@DisplayName("Should block unauthenticated requests with 401 Unauthorized")
	void testUnauthenticatedAccess() throws Exception {
		mockMvc.perform(post("/api/news/send-alert")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
                                {
                                    "channel": "EMAIL",
                                    "newsTitle": "Unauthenticated Test",
                                    "newsBody": "Should fail"
                                }
                                """))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("Should successfully authenticate and trigger an Email notification")
	void testSendEmailAlertSuccess() throws Exception {
		mockMvc.perform(post("/api/news/send-alert")
						.with(httpBasic("elek", "kecske"))
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
                                {
                                    "channel": "EMAIL",
                                    "newsTitle": "Market High",
                                    "newsBody": "S&P 500 hit a record high today."
                                }
                                """))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("SUCCESS"))
				.andExpect(jsonPath("$.message").value("Alert sent via EMAIL to authenticated user elek"));

		// Verify that JavaMailSender.send() was actually called with the correct details
		verify(mailSender, timeout(1000)).send(any(SimpleMailMessage.class));
	}

	@Test
	@DisplayName("Should successfully authenticate and trigger a Slack notification")
	void testSendSlackAlertSuccess() throws Exception {
		mockMvc.perform(post("/api/news/send-alert")
						.with(httpBasic("elek", "kecske"))
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
                                {
                                    "channel": "SLACK",
                                    "newsTitle": "Breaking News",
                                    "newsBody": "Major economic shift announced."
                                }
                                """))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("SUCCESS"))
				.andExpect(jsonPath("$.message").value("Alert sent via SLACK to authenticated user elek"));

		// Verify that RestTemplate.postForEntity() was called to post to the Slack Webhook
		verify(restTemplate, timeout(1000)).postForEntity(any(String.class), any(), any());
	}

}
