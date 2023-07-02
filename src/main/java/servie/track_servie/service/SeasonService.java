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
import servie.track_servie.payload.primaryKeys.ServieKey;
import servie.track_servie.payload.primaryKeys.UserSeasonDataKey;
import servie.track_servie.payload.primaryKeys.UserServieDataKey;
import servie.track_servie.entity.Season;
import servie.track_servie.entity.Series;
import servie.track_servie.entity.Servie;
import servie.track_servie.entity.User;
import servie.track_servie.entity.UserEpisodeData;
import servie.track_servie.entity.UserSeasonData;
import servie.track_servie.entity.UserServieData;
import servie.track_servie.exceptions.ResourceNotFoundException;
import servie.track_servie.payload.dtos.EntityDtoConversion;
import servie.track_servie.repository.SeasonRepository;
import servie.track_servie.repository.SeriesRepository;
import servie.track_servie.repository.ServieRepository;
import servie.track_servie.repository.UserEpisodeDataRepository;
import servie.track_servie.repository.UserRepository;
import servie.track_servie.repository.UserSeasonDataRepository;
import servie.track_servie.repository.UserServieDataRepository;

@Service
public class SeasonService
{
    @Autowired
    private SeasonRepository seasonRepository;
    @Autowired
    private UserEpisodeDataRepository userEpisodeDataRepository;
    @Autowired
    private UserSeasonDataRepository userSeasonDataRepository;
    @Autowired
    private UserServieDataRepository userServieDataRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ServieRepository servieRepository;
    @Autowired
    private SeriesRepository seriesRepository;
    @Autowired
    private EntityDtoConversion converter;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${tmdb.api.key}")
    private String apiKey;

    // Returns specific Season from the database which matches the criteria
    public SeasonDtoSeasonPage getSeason(Integer tmdbId, Integer seasonNumber)
    {
        // Season season = seasonRepository.findByTmdbIdAndSeasonNumber(tmdbId, seasonNumber);
        Series series = seriesRepository.findById(new ServieKey(tmdbId, "tv")).orElseThrow(() -> new ResourceNotFoundException("Series", "TmdbId", tmdbId.toString()));
        Season season = seasonRepository.findBySeriesAndSeasonNumber(series, seasonNumber);
        SeasonDtoSeasonPage seasonDto = converter.seasonToDtoSeasonPage(season);
        return seasonDto;
    }

    // Toggles the watch value of a Season (after toggling watch value of all related Episodes)
    public void toggleSeasonWatch(Integer userId, Integer tmdbId, Integer seasonNumber)
    {
        // Season season = seasonRepository.findByTmdbIdAndSeasonNumber(tmdbId, seasonNumber);
        Series series = seriesRepository.findById(new ServieKey(tmdbId, "tv")).orElseThrow(() -> new ResourceNotFoundException("Series", "TmdbId", tmdbId.toString()));
        Season season = seasonRepository.findBySeriesAndSeasonNumber(series, seasonNumber);
        // without user
        // List<Episode> episodes = episodeRepository.findByTmdbIdAndSeasonNumberAndWatched(tmdbId, seasonNumber, season.getWatched());
        // for(Episode episode : episodes)
        //     episode.setWatched(!season.getWatched());
        // with user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
        UserSeasonData userSeasonData = new UserSeasonData();
        UserServieData userServieData = new UserServieData();
        Servie servie = servieRepository.findById(new ServieKey(tmdbId, "tv")).get();
        userServieData = userServieDataRepository.findById(new UserServieDataKey(user, servie)).get();
        userSeasonData = userSeasonDataRepository.findById(new UserSeasonDataKey(userServieData, seasonNumber)).get();
        List<UserEpisodeData> userEpisodeDatas = userEpisodeDataRepository.findByUserSeasonDataAndWatched(userSeasonData, userSeasonData.getWatched());
        for(UserEpisodeData userEpisodeData : userEpisodeDatas)
            userEpisodeData.setWatched(!userSeasonData.getWatched());
        userSeasonData.setWatched(!userSeasonData.getWatched());
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
        // Season season = seasonRepository.findByTmdbIdAndSeasonNumber(tmdbId, seasonNumber);
        Series series = seriesRepository.findById(new ServieKey(tmdbId, "tv")).orElseThrow(() -> new ResourceNotFoundException("Series", "TmdbId", tmdbId.toString()));
        Season season = seasonRepository.findBySeriesAndSeasonNumber(series, seasonNumber);
        season.setPosterPath(filePath);
        seasonRepository.save(season);
    }
}
