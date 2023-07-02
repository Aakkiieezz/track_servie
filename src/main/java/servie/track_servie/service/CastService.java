package servie.track_servie.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import servie.track_servie.entity.Cast;
import servie.track_servie.entity.credits.ServieCredits;
import servie.track_servie.repository.CastRepository;

@Service
public class CastService
{
    @Autowired
    private CastRepository castRepository;
    @Autowired
    private PersonService personService;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${tmdb.api.key}")
    private String apiKey;
    private HttpHeaders headers = new HttpHeaders();
    private HttpEntity<?> httpEntity = new HttpEntity<>(headers);

    public List<Cast> saveCastMembers(Integer tmdbId, String childtype)
    {
        ServieCredits servieCredits = new ServieCredits();
        List<Cast> casts = new ArrayList<>();
        ResponseEntity<ServieCredits> creditsResponse = restTemplate.exchange("https://api.themoviedb.org/3/"+childtype+"/"+tmdbId+"/credits?api_key="+apiKey, HttpMethod.GET, httpEntity, ServieCredits.class);
        if(creditsResponse.getStatusCode()==HttpStatus.OK)
        {
            servieCredits = creditsResponse.getBody();
            if(servieCredits!=null)
                casts = servieCredits.getCast();
        }
        personService.saveAllPersonsFromCast(casts);
        return castRepository.saveAll(casts);
    }
}