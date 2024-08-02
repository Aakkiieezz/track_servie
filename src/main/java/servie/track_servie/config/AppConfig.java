package servie.track_servie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AppConfig
{
	@Bean
	public RestTemplate getRestTemplate()
	{
		return new RestTemplate();
	}
}