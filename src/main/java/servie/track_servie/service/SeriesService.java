package servie.track_servie.service;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import servie.track_servie.payload.dtos.operationsSearch.SeriesPageDtos.SeasonDtoSearchSeriesPage;
import servie.track_servie.payload.dtos.operationsSearch.SeriesPageDtos.SeriesDtoSearchSeriesPage;

@Service
public class SeriesService
{
	private RestTemplate restTemplate;
	@Value("${tmdb.api.key}")
	private String apiKey;
	private HttpHeaders headers = new HttpHeaders();
	private HttpEntity<?> httpEntity = new HttpEntity<>(headers, (MultiValueMap<String, String>) null);

	public SeriesDtoSearchSeriesPage searchSeries(Integer tmdbId)
	{
		HttpMethod httpMethod = Objects.requireNonNull(HttpMethod.GET);
		ResponseEntity<SeriesDtoSearchSeriesPage> response = restTemplate.exchange("https://api.themoviedb.org/3/tv/"+tmdbId+"?api_key="+apiKey, httpMethod, httpEntity, SeriesDtoSearchSeriesPage.class);
		SeriesDtoSearchSeriesPage seriesDto = response.getBody();
		if(seriesDto!=null)
			for(SeasonDtoSearchSeriesPage seasonDto : seriesDto.getSeasons())
				seasonDto.setTmdbId(seriesDto.getTmdbId());
		return seriesDto;
	}
}