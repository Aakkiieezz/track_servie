package servie.track_servie.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
import servie.track_servie.payload.dtos.operationsSeasonPageDtos.EpisodeDtoSeasonPage;
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
	private ServieService servieService;
	@Autowired
	private RestTemplate restTemplate;
	@Value("${tmdb.api.key}")
	private String apiKey;
	@Value("${user-id}")
	private Integer userId;
	HttpHeaders headers = new HttpHeaders();
	HttpEntity<?> httpEntity = new HttpEntity<>(headers);

	public SeasonDtoSeasonPage getSeason(Integer tmdbId, Integer seasonNo)
	{
		// Season season = seasonRepository.findByTmdbIdAndSeasonNo(tmdbId, seasonNo);
		Series series = seriesRepository.findById(new ServieKey("tv", tmdbId)).orElseThrow(() -> new ResourceNotFoundException("Series", "TmdbId", tmdbId.toString()));
		Season season = seasonRepository.findBySeriesAndSeasonNo(series, seasonNo);
		SeasonDtoSeasonPage seasonDto = converter.seasonToDtoSeasonPage(season);
		seasonDto.setTmdbId(tmdbId);
		// -------------------------
		// >>> Very bad code :
		List<EpisodeDtoSeasonPage> episodesList = userEpisodeDataRepository.getEpisodesForSeasonPage(tmdbId, seasonNo);
		seasonDto.setEpisodes(episodesList);
		// -------------------------
		// >>> Very bad code :
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		Servie servie = servieRepository.findById(new ServieKey("tv", tmdbId)).orElseGet(() -> servieService.addServie("tv", tmdbId));
		UserServieData userServieData = userServieDataRepository.findById(new UserServieDataKey(user, servie)).orElse(new UserServieData(user, servie));
		Optional<UserSeasonData> userSeasonDataOptional = userSeasonDataRepository.findById(new UserSeasonDataKey(userServieData, seasonNo));
		if(userSeasonDataOptional.isPresent())
		{
			UserSeasonData userSeasonData = userSeasonDataOptional.get();
			if(userSeasonData.getSeasonNo()>0)
			{
				seasonDto.setEpisodesWatched(userSeasonData.getEpisodesWatched());
				seasonDto.setWatched(userSeasonData.getWatched());
			}
		}
		else
		{
			seasonDto.setEpisodesWatched(0);
			seasonDto.setWatched(false);
		}
		// -------------------------
		// >>> Very bad code
		seasonDto.setEpisodes(seasonDto.getEpisodes().stream()
				.sorted(Comparator.comparing(EpisodeDtoSeasonPage::getEpisodeNo))
				.collect(Collectors.toList()));
		// -------------------------
		return seasonDto;
	}

	// @Transactional
	// Modification Pattern:
	// * AkashPersistSeparatelyPattern -> Save each entity individually
	// AkashPersistTogetherPattern -> Save the parent entity along with all the modifications in the child entities
	//     issues - [fixable] couldn't save another season into a series already having a season
	public void toggleSeasonWatch(Integer userId, Integer tmdbId, Integer seasonNo)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		Servie servie = servieRepository.findById(new ServieKey("tv", tmdbId)).orElseGet(() -> servieService.addServie("tv", tmdbId));
		UserServieData userServieData = userServieDataRepository.findById(new UserServieDataKey(user, servie)).orElseGet(() -> userServieDataRepository.save(new UserServieData(user, servie)));
		UserSeasonData userSeasonData = userSeasonDataRepository.findById(new UserSeasonDataKey(userServieData, seasonNo)).orElseGet(() -> userSeasonDataRepository.save(new UserSeasonData(userServieData, seasonNo)));
		List<UserEpisodeData> userEpisodeDatas = userEpisodeDataRepository.getToggledEpisodes(tmdbId, seasonNo, !userSeasonData.getWatched());
		for(UserEpisodeData userEpisodeData : userEpisodeDatas)
			userEpisodeData.setUserSeasonData(userSeasonData);
		userEpisodeDataRepository.saveAll(userEpisodeDatas);
	}

	public List<Image> getSeasonImages(Integer tmdbId, Integer seasonNo)
	{
		ResponseEntity<SeasonPostersDto> response = restTemplate.exchange("https://api.themoviedb.org/3/tv/"+tmdbId+"/season/"+seasonNo+"/images?api_key="+apiKey, HttpMethod.GET, httpEntity, SeasonPostersDto.class);
		SeasonPostersDto res = response.getBody();
		List<Image> images = new ArrayList<>();
		if(res!=null)
			images = res.getPosters();
		return images;
	}

	public void changeImage(Integer tmdbId, Integer seasonNo, String filePath)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		Servie servie = servieRepository.findById(new ServieKey("tv", tmdbId)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey("tv", tmdbId).toString()));
		UserServieData userServieData = userServieDataRepository.findById(new UserServieDataKey(user, servie)).get();
		// ??? what if user don't have this season
		UserSeasonData userSeasonData = userSeasonDataRepository.findById(new UserSeasonDataKey(userServieData, seasonNo)).get();
		userSeasonData.setPosterPath(filePath);
		userSeasonDataRepository.save(userSeasonData);
	}
}
