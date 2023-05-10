package servie.track_servie.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import servie.track_servie.entities.Language;

@Service
public class LanguageService
{
    @Autowired
    private RestTemplate restTemplate;
    @Value("${tmdb.api.key}")
    private String apiKey;

    public List<Language> getAll()
    {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<List<Language>> languagesResponse = restTemplate.exchange("https://api.themoviedb.org/3/configuration/languages?api_key="+apiKey, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<Language>>()
        {});
        List<Language> languages = languagesResponse.getBody();
        return languages;
    }
}
