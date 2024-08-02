package servie.track_servie.service;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import servie.track_servie.payload.dtos.LanguageDto;
// WORKING -> BUT OF NO USE FOR NOW

@Service
public class LanguageService
{
	@Autowired
	private RestTemplate restTemplate;
	@Value("${tmdb.api.key}")
	private String apiKey;
	private HttpHeaders headers = new HttpHeaders();
	private HttpEntity<?> httpEntity = new HttpEntity<>(headers, (MultiValueMap<String, String>) null);

	public List<LanguageDto> getAll()
	{
		HttpMethod httpMethod = Objects.requireNonNull(HttpMethod.GET);
		ResponseEntity<List<LanguageDto>> languagesResponse = restTemplate.exchange(
				"https://api.themoviedb.org/3/configuration/languages?api_key="+apiKey,
				httpMethod,
				httpEntity,
				new ParameterizedTypeReference<List<LanguageDto>>()
				{},
				(Object) null);
		List<LanguageDto> languages = languagesResponse.getBody();
		return languages;
	}
}
