package servie.track_servie.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import lombok.NonNull;
import servie.track_servie.payload.dtos.operationsImage.Image;
import servie.track_servie.payload.dtos.operationsImage.EpisodePageDtos.EpisodeStillsDto;
import servie.track_servie.payload.primaryKeys.ServieKey;
import servie.track_servie.payload.primaryKeys.UserEpisodeDataKey;
import servie.track_servie.payload.primaryKeys.UserSeasonDataKey;
import servie.track_servie.payload.primaryKeys.UserServieDataKey;
import servie.track_servie.entity.Episode;
import servie.track_servie.entity.Servie;
import servie.track_servie.entity.User;
import servie.track_servie.entity.UserEpisodeData;
import servie.track_servie.entity.UserSeasonData;
import servie.track_servie.entity.UserServieData;
import servie.track_servie.exceptions.ResourceNotFoundException;
import servie.track_servie.payload.dtos.EntityDtoConversion;
import servie.track_servie.payload.dtos.episodePageDtos.EpisodeDtoEpisodePage;
import servie.track_servie.repository.EpisodeRepository;
import servie.track_servie.repository.ServieRepository;
import servie.track_servie.repository.UserEpisodeDataRepository;
import servie.track_servie.repository.UserRepository;
import servie.track_servie.repository.UserSeasonDataRepository;
import servie.track_servie.repository.UserServieDataRepository;

@Service
public class EpisodeService
{
	@Autowired
	private EpisodeRepository episodeRepository;
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
	private EntityDtoConversion converter;
	@Autowired
	private RestTemplate restTemplate;
	@Value("${tmdb.api.key}")
	private String apiKey;
	private HttpHeaders headers = new HttpHeaders();
	private HttpEntity<?> httpEntity = new HttpEntity<>(headers, (MultiValueMap<String, String>) null);

	public EpisodeDtoEpisodePage getEpisode(Integer tmdbId, Integer seasonNo, Integer episodeNo)
	{
		Episode episode = episodeRepository.findByTmdbIdAndSeasonNoAndEpisodeNo(tmdbId, seasonNo, episodeNo);
		EpisodeDtoEpisodePage episodeDto = converter.episodeToDtoEpisodePage(episode);
		return episodeDto;
	}

	// @Transactional
	// Pattern :
	// * AkashPersistSeparatelyPattern -> Save each entity individually
	// AkashPersistTogetherPattern -> Save the parent entity along with all the modifications in the child entities
	public void toggleEpisodeWatch(@NonNull Integer userId, Integer tmdbId, Integer seasonNo, Integer episodeNo)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		Servie servie = servieRepository.findById(new ServieKey("tv", tmdbId)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey("tv", tmdbId).toString()));
		UserServieData userServieData = userServieDataRepository.findById(new UserServieDataKey(user, servie)).orElseGet(() -> userServieDataRepository.save(new UserServieData(user, servie)));
		UserSeasonData userSeasonData = userSeasonDataRepository.findById(new UserSeasonDataKey(userServieData, seasonNo)).orElseGet(() -> userSeasonDataRepository.save(new UserSeasonData(userServieData, seasonNo)));
		UserEpisodeData userEpisodeData = userEpisodeDataRepository.findById(new UserEpisodeDataKey(userSeasonData, episodeNo)).orElse(new UserEpisodeData(userSeasonData, episodeNo));
		userEpisodeData.setWatched(!userEpisodeData.getWatched());
		userEpisodeDataRepository.save(userEpisodeData);
	}

	public void toggleMultipleEpisodeWatch(@NonNull Integer userId, Integer tmdbId, Integer seasonNo, Integer fromEpisodeNumber, Integer toEpisodeNumber, boolean watchValue)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		Servie servie = servieRepository.findById(new ServieKey("tv", tmdbId)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey("tv", tmdbId).toString()));
		UserServieData userServieData = userServieDataRepository.findById(new UserServieDataKey(user, servie)).orElseGet(() -> userServieDataRepository.save(new UserServieData(user, servie)));
		UserSeasonData userSeasonData = userSeasonDataRepository.findById(new UserSeasonDataKey(userServieData, seasonNo)).orElseGet(() -> userSeasonDataRepository.save(new UserSeasonData(userServieData, seasonNo)));
		List<UserEpisodeData> userEpisodeDatas = new ArrayList<>();
		for(int episodeNo = fromEpisodeNumber; episodeNo<=toEpisodeNumber; episodeNo++)
		{
			UserEpisodeData userEpisodeData = userEpisodeDataRepository.findById(new UserEpisodeDataKey(userSeasonData, episodeNo)).orElse(new UserEpisodeData(userSeasonData, episodeNo));
			userEpisodeData.setWatched(watchValue);
			userEpisodeDatas.add(userEpisodeData);
		}
		userEpisodeDataRepository.saveAll(userEpisodeDatas);
	}

	public List<Image> getEpisodeImages(Integer tmdbId, Integer seasonNo, Integer episodeNo)
	{
		HttpMethod httpMethod = Objects.requireNonNull(HttpMethod.GET);
		ResponseEntity<EpisodeStillsDto> response = restTemplate.exchange("https://api.themoviedb.org/3/tv/"+tmdbId+"/season/"+seasonNo+"/episode/"+episodeNo+"/images?api_key="+apiKey, httpMethod, httpEntity, EpisodeStillsDto.class);
		EpisodeStillsDto res = response.getBody();
		List<Image> images = new ArrayList<>();
		if(res!=null)
			images = res.getStills();
		return images;
	}

	public void changeImage(Integer tmdbId, Integer seasonNo, Integer episodeNo, String filePath)
	{
		Episode episode = episodeRepository.findByTmdbIdAndSeasonNoAndEpisodeNo(tmdbId, seasonNo, episodeNo);
		episode.setStillPath(filePath);
		episodeRepository.save(episode);
	}
}