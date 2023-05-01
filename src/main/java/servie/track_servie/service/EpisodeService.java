package servie.track_servie.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import servie.track_servie.payload.dtos.operationsEpisodePageDtos.EpisodeDtoEpisodePage;
import servie.track_servie.payload.dtos.operationsImage.Image;
import servie.track_servie.payload.dtos.operationsImage.EpisodePageDtos.EpisodeStillsDto;
import servie.track_servie.payload.dtos.operationsSearch.EpisodePageDtos.EpisodeDtoSearchEpisodePage;
import servie.track_servie.entities.Episode;
import servie.track_servie.entities.UserEpisodeData;
import servie.track_servie.payload.dtos.EntityDtoConversion;
import servie.track_servie.repository.EpisodeRepository;
import servie.track_servie.repository.UserEpisodeDataRepository;

@Service
public class EpisodeService
{
    @Autowired
    private EpisodeRepository episodeRepository;
    @Autowired
    private UserEpisodeDataRepository userEpisodeDataRepository;
    @Autowired
    private EntityDtoConversion converter;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${tmdb.api.key}")
    private String apiKey;

    // Returns specific Episode from the database which matches the criteria
    public EpisodeDtoEpisodePage getEpisode(Integer tmdbId, Integer seasonNumber, Integer episodeNumber)
    {
        Episode episode = episodeRepository.findByTmdbIdAndSeasonNumberAndEpisodeNumber(tmdbId, seasonNumber, episodeNumber);
        EpisodeDtoEpisodePage episodeDto = converter.episodeToDtoEpisodePage(episode);
        return episodeDto;
    }

    // Toggles the watch value of an Episode
    public void toggleEpisodeWatch(Integer userId, Integer tmdbId, Integer seasonNumber, Integer episodeNumber)
    {
        // without user
        // Episode episode = episodeRepository.findByTmdbIdAndSeasonNumberAndEpisodeNumber(tmdbId, seasonNumber, episodeNumber);
        // episode.setWatched(!episode.getWatched());
        // episodeRepository.save(episode);
        // with user
        UserEpisodeData userEpisodeData = userEpisodeDataRepository.findByUserIdAndTmdbIdAndSeasonNumberAndEpisodeNumber(userId, tmdbId, seasonNumber, episodeNumber);
        userEpisodeData.setWatched(!userEpisodeData.getWatched());
        userEpisodeDataRepository.save(userEpisodeData);
    }

    // Returns data of specific Episode from the 3rd party api
    public EpisodeDtoSearchEpisodePage searchEpisode(Integer tmdbId, Integer seasonNumber, Integer episodeNumber)
    {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<EpisodeDtoSearchEpisodePage> response = restTemplate.exchange("https://api.themoviedb.org/3/tv/"+tmdbId+"/season/"+seasonNumber+"/episode/"+episodeNumber+"?api_key="+apiKey, HttpMethod.GET, httpEntity, EpisodeDtoSearchEpisodePage.class);
        EpisodeDtoSearchEpisodePage episodeDto = response.getBody();
        if(episodeDto!=null)
            episodeDto.setTmdbId(tmdbId);
        return episodeDto;
    }

    // Returns list of img-Stills(for Episode) from the 3rd party api
    public List<Image> getEpisodeImages(Integer tmdbId, Integer seasonNumber, Integer episodeNumber)
    {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<EpisodeStillsDto> response = restTemplate.exchange("https://api.themoviedb.org/3/tv/"+tmdbId+"/season/"+seasonNumber+"/episode/"+episodeNumber+"/images?api_key="+apiKey, HttpMethod.GET, httpEntity, EpisodeStillsDto.class);
        EpisodeStillsDto res = response.getBody();
        List<Image> images = new ArrayList<>();
        if(res!=null)
            images = res.getStills();
        return images;
    }

    // Changes Still of an Episode
    public void changeImage(Integer tmdbId, Integer seasonNumber, Integer episodeNumber, String filePath)
    {
        Episode episode = episodeRepository.findByTmdbIdAndSeasonNumberAndEpisodeNumber(tmdbId, seasonNumber, episodeNumber);
        episode.setStillPath(filePath);
        episodeRepository.save(episode);
    }
}