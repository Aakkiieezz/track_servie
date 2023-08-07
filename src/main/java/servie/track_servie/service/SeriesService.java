package servie.track_servie.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import servie.track_servie.payload.dtos.operationsSearch.SeriesPageDtos.SeasonDtoSearchSeriesPage;
import servie.track_servie.payload.dtos.operationsSearch.SeriesPageDtos.SeriesDtoSearchSeriesPage;

@Service
public class SeriesService
{
    private RestTemplate restTemplate;
    @Value("${tmdb.api.key}")
    private String apiKey;
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<?> httpEntity = new HttpEntity<>(headers);

    public SeriesDtoSearchSeriesPage searchSeries(Integer tmdbId)
    {
        ResponseEntity<SeriesDtoSearchSeriesPage> response = restTemplate.exchange("https://api.themoviedb.org/3/tv/"+tmdbId+"?api_key="+apiKey, HttpMethod.GET, httpEntity, SeriesDtoSearchSeriesPage.class);
        SeriesDtoSearchSeriesPage seriesDto = response.getBody();
        if(seriesDto!=null)
            for(SeasonDtoSearchSeriesPage seasonDto : seriesDto.getSeasons())
                seasonDto.setTmdbId(seriesDto.getTmdbId());
        return seriesDto;
    }
}