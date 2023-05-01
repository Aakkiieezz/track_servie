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
import servie.track_servie.payload.dtos.operationsImage.Image;
import servie.track_servie.payload.dtos.operationsImage.SeasonPageDtos.SeasonPostersDto;
import servie.track_servie.payload.dtos.operationsSearch.SeasonPageDtos.EpisodeDtoSearchSeasonPage;
import servie.track_servie.payload.dtos.operationsSearch.SeasonPageDtos.SeasonDtoSearchSeasonPage;
import servie.track_servie.payload.dtos.operationsSeasonPageDtos.SeasonDtoSeasonPage;
import servie.track_servie.entities.Season;
import servie.track_servie.entities.UserEpisodeData;
import servie.track_servie.payload.dtos.EntityDtoConversion;
import servie.track_servie.repository.SeasonRepository;
import servie.track_servie.repository.UserEpisodeDataRepository;

@Service
public class SeasonService
{
    @Autowired
    private SeasonRepository seasonRepository;
    @Autowired
    private UserEpisodeDataRepository userEpisodeDataRepository;
    @Autowired
    private EntityDtoConversion converter;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${tmdb.api.key}")
    private String apiKey;

    // Returns specific Season from the database which matches the criteria
    public SeasonDtoSeasonPage getSeason(Integer tmdbId, Integer seasonNumber)
    {
        Season season = seasonRepository.findByTmdbIdAndSeasonNumber(tmdbId, seasonNumber);
        SeasonDtoSeasonPage seasonDto = converter.seasonToDtoSeasonPage(season);
        return seasonDto;
    }

    // Toggles the watch value of a Season (after toggling watch value of all related Episodes)
    public void toggleSeasonWatch(Integer userId, Integer tmdbId, Integer seasonNumber)
    {
        Season season = seasonRepository.findByTmdbIdAndSeasonNumber(tmdbId, seasonNumber);
        // without user
        // List<Episode> episodes = episodeRepository.findByTmdbIdAndSeasonNumberAndWatched(tmdbId, seasonNumber, season.getWatched());
        // for(Episode episode : episodes)
        //     episode.setWatched(!season.getWatched());
        // with user
        List<UserEpisodeData> userEpisodeDatas = userEpisodeDataRepository.findByUserIdAndTmdbIdAndSeasonNumberAndWatched(userId, tmdbId, seasonNumber, season.getWatched());
        for(UserEpisodeData userEpisodeData : userEpisodeDatas)
            userEpisodeData.setWatched(!season.getWatched());
        season.setWatched(!season.getWatched());
        seasonRepository.save(season);
    }

    // Returns data of specific Season from the 3rd party api
    public SeasonDtoSearchSeasonPage searchSeason(Integer tmdbId, Integer seasonNumber)
    {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<SeasonDtoSearchSeasonPage> response = restTemplate.exchange("https://api.themoviedb.org/3/tv/"+tmdbId+"/season/"+seasonNumber+"?api_key="+apiKey, HttpMethod.GET, httpEntity, SeasonDtoSearchSeasonPage.class);
        SeasonDtoSearchSeasonPage seasonDto = response.getBody();
        if(seasonDto!=null)
            for(EpisodeDtoSearchSeasonPage episodeDto : seasonDto.getEpisodes())
                episodeDto.setTmdbId(tmdbId);
        return seasonDto;
    }

    // Returns list of img-Posters(for Season) from the 3rd party api
    public List<Image> getSeasonImages(Integer tmdbId, Integer seasonNumber)
    {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<SeasonPostersDto> response = restTemplate.exchange("https://api.themoviedb.org/3/tv/"+tmdbId+"/season/"+seasonNumber+"/images?api_key="+apiKey, HttpMethod.GET, httpEntity, SeasonPostersDto.class);
        SeasonPostersDto res = response.getBody();
        List<Image> images = new ArrayList<>();
        if(res!=null)
            images = res.getPosters();
        return images;
    }

    // Changes img-Poster of a Season
    public void changeImage(Integer tmdbId, Integer seasonNumber, String filePath)
    {
        Season season = seasonRepository.findByTmdbIdAndSeasonNumber(tmdbId, seasonNumber);
        season.setPosterPath(filePath);
        seasonRepository.save(season);
    }
}
