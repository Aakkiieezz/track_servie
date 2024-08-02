package servie.track_servie.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import servie.track_servie.payload.dtos.operationsHomePageDtos.ResponseDtoHomePage;
import servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage;
import servie.track_servie.payload.dtos.operationsImage.Image;
import servie.track_servie.payload.dtos.operationsImage.SeriesPageDtos.SeriesBackdropsDto;
import servie.track_servie.payload.dtos.operationsImage.SeriesPageDtos.SeriesPostersDto;
import servie.track_servie.payload.dtos.operationsMovieCollectionPageDtos.MovieDtoMovieCollectionPageDto;
import servie.track_servie.payload.dtos.operationsSearch.SearchPageDtos.SearchResultDtoSearchPage;
import servie.track_servie.payload.dtos.operationsSearch.SearchPageDtos.SeriesDtoSearchPage;
import servie.track_servie.payload.dtos.operationsServiePageDtos.GenreDtoServiePage;
import servie.track_servie.payload.dtos.operationsServiePageDtos.SeasonDtoServiePage;
import servie.track_servie.payload.dtos.operationsServiePageDtos.ServieDtoServiePage;
import servie.track_servie.payload.primaryKeys.ServieKey;
import servie.track_servie.payload.primaryKeys.UserSeasonDataKey;
import servie.track_servie.payload.primaryKeys.UserServieDataKey;
import servie.track_servie.entity.Audits;
import servie.track_servie.entity.Episode;
import servie.track_servie.entity.Genre;
import servie.track_servie.entity.Movie;
import servie.track_servie.entity.MovieCollection;
import servie.track_servie.entity.ProductionCompany;
import servie.track_servie.entity.ProductionCountry;
import servie.track_servie.entity.Season;
import servie.track_servie.entity.Series;
import servie.track_servie.entity.Servie;
import servie.track_servie.entity.SpokenLanguage;
import servie.track_servie.entity.User;
import servie.track_servie.entity.UserEpisodeData;
import servie.track_servie.entity.UserSeasonData;
import servie.track_servie.entity.UserServieData;
import servie.track_servie.entity.credits.EpisodeCrew;
import servie.track_servie.entity.credits.MovieCast;
import servie.track_servie.entity.credits.EpisodeCast;
import servie.track_servie.entity.credits.MovieCredits;
import servie.track_servie.entity.credits.SeasonCast;
import servie.track_servie.entity.credits.SeasonCredits;
import servie.track_servie.enums.AuditType;
import servie.track_servie.enums.ServieType;
import servie.track_servie.exceptions.ResourceNotFoundException;
import servie.track_servie.repository.AuditsRepository;
import servie.track_servie.repository.CustomRepository;
import servie.track_servie.repository.EpisodeRepository;
import servie.track_servie.repository.GenreRepository;
import servie.track_servie.repository.MovieCollectionRepository;
import servie.track_servie.repository.MovieRepository;
import servie.track_servie.repository.SeriesRepository;
import servie.track_servie.repository.ServieRepository;
import servie.track_servie.repository.UserEpisodeDataRepository;
import servie.track_servie.repository.UserRepository;
import servie.track_servie.repository.UserSeasonDataRepository;
import servie.track_servie.repository.UserServieDataRepository;
import servie.track_servie.utils.GenreUtils;

@Service
@Slf4j
public class ServieService
{
	@Autowired
	private ServieRepository servieRepository;
	@Autowired
	private CustomRepository csri;
	@Autowired
	private GenreRepository genreRepository;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private MovieCollectionRepository movieCollectionRepository;
	@Autowired
	private UserServieDataRepository userServieDataRepository;
	@Autowired
	private UserSeasonDataRepository userSeasonDataRepository;
	@Autowired
	private UserEpisodeDataRepository userEpisodeDataRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SeriesRepository seriesRepository;
	@Autowired
	private AuditsRepository auditsRepository;
	@Autowired
	private EpisodeRepository episodeRepository;
	@Autowired
	private GenreUtils genreUtils;
	@Value("${tmdb.api.key}")
	private String apiKey;
	@Value("${home-page.page.size}")
	private int pageSize;
	private HttpHeaders headers = new HttpHeaders();
	private HttpEntity<?> httpEntity = new HttpEntity<>(headers, (MultiValueMap<String, String>) null);
	private Set<EpisodeCast> allEpGuestStars = null;
	private Set<EpisodeCrew> allEpCrews = null;
	private Set<SeasonCast> allSeasonCasts = null;

	public Servie addServie(String type, Integer tmdbId)
	{
		Servie servie = new Servie();
		if(type.equals("tv"))
			servie = addSeries(tmdbId);
		else
			servie = addMovie(tmdbId);
		return servie;
	}

	private Movie addMovie(Integer tmdbId)
	{
		Audits audits = new Audits(AuditType.INSERTION, "Adding movie", "movie"+tmdbId);
		auditsRepository.save(audits);
		HttpMethod httpMethod = Objects.requireNonNull(HttpMethod.GET);
		ResponseEntity<Movie> movieResponse = restTemplate.exchange(
				"https://api.themoviedb.org/3/"+ServieType.MOVIE.toString()+"/"+tmdbId+"?api_key="+apiKey,
				httpMethod,
				httpEntity,
				Movie.class);
		if(movieResponse.getStatusCode()==HttpStatus.OK)
		{
			Movie movie = movieResponse.getBody();
			if(movie!=null)
			{
				movie.setLastModified(LocalDateTime.now());
				if(movie.getBelongsToCollection()!=null)
				{
					log.info("    Movie {}, found out to be in a collection", tmdbId);
					addMovieCollection(movie.getBelongsToCollection().getCollectionId());
					movie.setChildtype(ServieType.MOVIE.toString());
					return movie;
				}
				ResponseEntity<MovieCredits> movieCreditsResponse = restTemplate.exchange(
						"https://api.themoviedb.org/3/"+"movie"+"/"+tmdbId+"/credits?api_key="+apiKey, httpMethod,
						httpEntity,
						MovieCredits.class);
				MovieCredits movieCredits = movieCreditsResponse.getBody();
				if(movieCredits!=null)
				{
					movie.setCast(movieCredits.getCast());
					movie.setCrew(movieCredits.getCrew());
				}
				movie.setChildtype(ServieType.MOVIE.toString());
				movie.setGenres(genreUtils.convertTmdbGenresToTrackServieGenres(movie.getGenres()));
				audits = new Audits(AuditType.INSERTION, "Movie added successfully", "movie"+tmdbId);
				auditsRepository.save(audits);
				return movieRepository.save(movie);
			}
			audits = new Audits(AuditType.ERROR, "Error while adding movie, tmdb api was OK, but movie object was null", "movie"+tmdbId);
			auditsRepository.save(audits);
		}
		audits = new Audits(AuditType.ERROR, "Error while adding movie, tmdb api was not OK", "movie"+tmdbId);
		auditsRepository.save(audits);
		return null;
	}

	private void addMovieCollection(Integer collectionId)
	{
		Audits audits = new Audits(AuditType.INSERTION, "Adding movie collection", "movieCollection"+collectionId);
		auditsRepository.save(audits);
		MovieCollection movieCollection = new MovieCollection();
		HttpMethod httpMethod = Objects.requireNonNull(HttpMethod.GET);
		ResponseEntity<MovieCollection> movieCollectionResponse = restTemplate.exchange("https://api.themoviedb.org/3/collection/"+collectionId+"?api_key="+apiKey, httpMethod, httpEntity, MovieCollection.class);
		if(movieCollectionResponse.getStatusCode()==HttpStatus.OK)
		{
			movieCollection = movieCollectionResponse.getBody();
			if(movieCollection!=null)
			{
				List<Genre> allTrackServieGenres = genreRepository.findAll();
				List<Movie> allMovies = new ArrayList<Movie>();
				// Issue - MultipleRepresentations
				Set<ProductionCompany> allProductionCompanies = new HashSet<>();
				Set<ProductionCountry> allProductionCountries = new HashSet<>();
				Set<SpokenLanguage> allSpokenLanguages = new HashSet<>();
				for(Movie movie : movieCollection.getMovies())
				{
					if(servieRepository.existsById(new ServieKey("movie", movie.getTmdbId())))
						throw new RuntimeException("The movie "+movie.getTmdbId()+" already exists before saving the movie collection !!!");
					log.info("    Adding movie {} as a part of collection", movie.getTmdbId());
					ResponseEntity<Movie> movieResponse = restTemplate.exchange("https://api.themoviedb.org/3/"+ServieType.MOVIE.toString()+"/"+movie.getTmdbId()+"?api_key="+apiKey, httpMethod, httpEntity, Movie.class);
					if(movieResponse.getStatusCode()==HttpStatus.OK)
					{
						movie = movieResponse.getBody();
						if(movie!=null)
						{
							ResponseEntity<MovieCredits> movieCreditsResponse = restTemplate.exchange("https://api.themoviedb.org/3/"+"movie"+"/"+movie.getTmdbId()+"/credits?api_key="+apiKey, httpMethod, httpEntity, MovieCredits.class);
							MovieCredits movieCredits = movieCreditsResponse.getBody();
							if(movieCredits!=null)
							{
								movie.setCast(movieCredits.getCast());
								movie.setCrew(movieCredits.getCrew());
							}
							movie.setLastModified(LocalDateTime.now());
							movie.setChildtype(ServieType.MOVIE.toString());
							movie.setCollection(movieCollection);
							Set<Integer> tmdbGenreIds = movie.getGenres().stream()
									.map(Genre::getId)
									.collect(Collectors.toSet());
							Set<Integer> trackServieGenreIds = genreUtils.genreMapper(tmdbGenreIds);
							Set<Genre> finalGenres = allTrackServieGenres.stream()
									.filter(genre -> trackServieGenreIds.contains(genre.getId()))
									.collect(Collectors.toSet());
							movie.setGenres(finalGenres);
							Set<ProductionCompany> prodCompanies = movie.getProductionCompanies().stream()
									.map(productionCompany ->
									{
										if(allProductionCompanies.contains(productionCompany))
											return allProductionCompanies.stream()
													.filter(pc -> pc.equals(productionCompany))
													.findFirst()
													.orElse(null);
										else
										{
											allProductionCompanies.add(productionCompany);
											return productionCompany;
										}
									})
									.collect(Collectors.toSet());
							movie.setProductionCompanies(prodCompanies);
							Set<ProductionCountry> prodCountries = movie.getProductionCountries().stream()
									.map(prodCountry ->
									{
										if(allProductionCountries.contains(prodCountry))
											return allProductionCountries.stream()
													.filter(pc -> pc.equals(prodCountry))
													.findFirst()
													.orElse(null);
										else
										{
											allProductionCountries.add(prodCountry);
											return prodCountry;
										}
									})
									.collect(Collectors.toSet());
							movie.setProductionCountries(prodCountries);
							Set<SpokenLanguage> spokenLanguages = movie.getSpokenLanguages().stream()
									.map(spokenLanguage ->
									{
										if(allSpokenLanguages.contains(spokenLanguage))
											return allSpokenLanguages.stream()
													.filter(pc -> pc.equals(spokenLanguage))
													.findFirst()
													.orElse(null);
										else
										{
											allSpokenLanguages.add(spokenLanguage);
											return spokenLanguage;
										}
									})
									.collect(Collectors.toSet());
							movie.setSpokenLanguages(spokenLanguages);
							allMovies.add(movie);
						}
					}
				}
				movieCollection.setMovies(allMovies);
				movieCollectionRepository.save(movieCollection);
				log.info("    Added {} movies successfully !!", allMovies.size());
			}
		}
	}

	// @Scheduled(fixedRate = Integer.MAX_VALUE)
	// @Scheduled(cron = "0 0 6 * * ?") // Runs every morning at 6 AM
	@Transactional
	public void updateAiringSeries()
	{
		// Series (Airing) -> everyday once
		List<Series> serieses = seriesRepository.findByLastModifiedBeforeAndStatus(LocalDateTime.now(), "Returning Series");
		serieses.clear();
		serieses.add(seriesRepository.findById(new ServieKey("tv", 76479)).get());
		log.info("Updating {} series which are currently airing", serieses.size());
		for(Series series : serieses)
			updateSeries(series.getTmdbId());
		log.info("Finished Updating {} series which are currently airing", serieses.size());
	}

	// @Scheduled(fixedRate = Integer.MAX_VALUE)
	// @Scheduled(cron = "0 0 6 * * ?") // Runs every morning at 6 AM
	@Transactional
	public void updateNonAiringSeries()
	{
		List<Series> serieses = seriesRepository.findByLastModifiedBefore(LocalDateTime.now().minusDays(5));
		log.info("Updating {} series which are not updated within last 5 days", serieses.size());
		for(Series series : serieses)
			updateSeries(series.getTmdbId());
		log.info("Finished Updating {} series which are not updated within last 5 days", serieses.size());
		// Movies -> once in a week
		// movies = moviesRepo.
	}

	private void updateSeries(Integer tmdbId)
	{
		Series tempSeries = fetchSeriesFromTmdb(tmdbId);
		if(tempSeries==null)
		{
			log.info("No such series found, probably deleted by TMDB");
			return;
		}
		log.info("Updating series {} name {}", tmdbId, tempSeries.getTitle());
		Series oldSeries = (Series) servieRepository.findById(new ServieKey("tv", tmdbId)).get();
		if(!oldSeries.isAdult()==tempSeries.isAdult())
		{
			log.info("  Series 'Adult' mismatched");
			oldSeries.setAdult(tempSeries.isAdult());
		}
		if(oldSeries.getBackdropPath()==null || !oldSeries.getBackdropPath().equals(tempSeries.getBackdropPath()))
		{
			log.info("  Series 'BackgroundPath' mismatched");
			oldSeries.setBackdropPath(tempSeries.getBackdropPath());
		}
		if(!oldSeries.getCreatedBy().equals(tempSeries.getCreatedBy()))
		{
			log.info("  Series 'CreatedBy' mismatched");
			oldSeries.setCreatedBy(tempSeries.getCreatedBy());
		}
		if(oldSeries.getFirstAirDate()==null || !oldSeries.getFirstAirDate().equals(tempSeries.getFirstAirDate()))
		{
			log.info("  Series 'FirstAirDate' mismatched");
			oldSeries.setFirstAirDate(tempSeries.getFirstAirDate());
		}
		if(!oldSeries.getGenres().equals(tempSeries.getGenres()))
		{
			log.info("  Series 'Genres' mismatched");
			oldSeries.setGenres(tempSeries.getGenres());
		}
		if(!oldSeries.getHomepage().equals(tempSeries.getHomepage()))
		{
			log.info("  Series 'HomePage' mismatched");
			oldSeries.setHomepage(tempSeries.getHomepage());
		}
		if(!Objects.equals(oldSeries.getImdbId(), tempSeries.getImdbId()))
		{
			log.info("  Series 'ImdbId' mismatched");
			oldSeries.setImdbId(tempSeries.getImdbId());
		}
		if(!oldSeries.isInProduction()==tempSeries.isInProduction())
		{
			log.info("  Series 'InProduction' mismatched");
			oldSeries.setInProduction(tempSeries.isInProduction());
		}
		if(!Objects.equals(oldSeries.getLastAirDate(), tempSeries.getLastAirDate()))
		{
			log.info("  Series 'LastAirDate' mismatched");
			oldSeries.setLastAirDate(tempSeries.getLastAirDate());
		}
		if(!oldSeries.getNetworks().equals(tempSeries.getNetworks()))
		{
			log.info("  Series 'Networks' mismatched");
			oldSeries.setNetworks(tempSeries.getNetworks());
		}
		if(!oldSeries.getOriginCountry().equals(tempSeries.getOriginCountry()))
		{
			log.info("  Series 'OriginalCountry' mismatched");
			oldSeries.setOriginCountry(tempSeries.getOriginCountry());
		}
		if(!oldSeries.getOriginalTitle().equals(tempSeries.getOriginalTitle()))
		{
			log.info("  Series 'OriginalTitle' mismatched");
			oldSeries.setOriginalTitle(tempSeries.getOriginalTitle());
		}
		if(!oldSeries.getOverview().equals(tempSeries.getOverview()))
		{
			log.info("  Series 'Overview' mismatched");
			oldSeries.setOverview(tempSeries.getOverview());
		}
		if(!oldSeries.getPopularity().equals(tempSeries.getPopularity()))
		{
			log.info("  Series 'Popularity' mismatched");
			oldSeries.setPopularity(tempSeries.getPopularity());
		}
		if(oldSeries.getPosterPath()==null || !oldSeries.getPosterPath().equals(tempSeries.getPosterPath()))
		{
			log.info("  Series 'PosterPath' mismatched");
			oldSeries.setPosterPath(tempSeries.getPosterPath());
		}
		if(!oldSeries.getProductionCompanies().equals(tempSeries.getProductionCompanies()))
		{
			log.info("  Series 'ProductionCompanies' mismatched");
			oldSeries.setProductionCompanies(tempSeries.getProductionCompanies());
		}
		if(!oldSeries.getProductionCountries().equals(tempSeries.getProductionCountries()))
		{
			log.info("  Series 'ProductionCountries' mismatched");
			oldSeries.setProductionCountries(tempSeries.getProductionCountries());
		}
		List<Season> updatedSeasons = getDeepSeasonsComparison(oldSeries.getSeasons(), tempSeries.getSeasons());
		oldSeries.setSeasons(updatedSeasons);
		if(!oldSeries.getSpokenLanguages().equals(tempSeries.getSpokenLanguages()))
		{
			log.info("  Series 'SpokenLanguages' mismatched");
			oldSeries.setSpokenLanguages(tempSeries.getSpokenLanguages());
		}
		if(!oldSeries.getStatus().equals(tempSeries.getStatus()))
		{
			log.info("  Series 'Status' mismatched");
			oldSeries.setStatus(tempSeries.getStatus());
		}
		if(!oldSeries.getTagline().equals(tempSeries.getTagline()))
		{
			log.info("  Series 'Tagline' mismatched");
			oldSeries.setTagline(tempSeries.getTagline());
		}
		if(!oldSeries.getTitle().equals(tempSeries.getTitle()))
		{
			log.info("  Series 'Title' mismatched");
			oldSeries.setTitle(tempSeries.getTitle());
		}
		// no check need for 'tmdbId'
		if(!oldSeries.getTotalEpisodes().equals(tempSeries.getTotalEpisodes()))
		{
			log.info("  Series 'TotalEpisodes' mismatched");
			oldSeries.setTotalEpisodes(tempSeries.getTotalEpisodes());
		}
		if(!oldSeries.getTotalSeasons().equals(tempSeries.getTotalSeasons()))
		{
			log.info("  Series 'TotalSeasons' mismatched");
			oldSeries.setTotalSeasons(tempSeries.getTotalSeasons());
		}
		if(!oldSeries.getType().equals(tempSeries.getType()))
		{
			log.info("  Series 'Type' mismatched");
			oldSeries.setType(tempSeries.getType());
		}
		if(!oldSeries.getVoteAverage().equals(tempSeries.getVoteAverage()))
		{
			log.info("  Series 'VoteAverage' mismatched");
			oldSeries.setVoteAverage(tempSeries.getVoteAverage());
		}
		if(!oldSeries.getVoteCount().equals(tempSeries.getVoteCount()))
		{
			log.info("  Series 'VoteCount' mismatched");
			oldSeries.setVoteCount(tempSeries.getVoteCount());
		}
		oldSeries.setLastModified(LocalDateTime.now());
		servieRepository.save(oldSeries);
		log.info("Series {}, updated successfully !!", tmdbId);
	}

	private List<Season> getDeepSeasonsComparison(List<Season> oldSeriesSeasons, List<Season> tempSeriesSeasons)
	{
		List<Season> finalSeasons = new ArrayList<>();
		Set<Season> oldSeriesSeasonsSet = new HashSet<>(oldSeriesSeasons);
		Set<Season> tempSeriesSeasonsSet = new HashSet<>(tempSeriesSeasons);
		// ??? RARE = what if there are few old seasons not present in latest payload ?
		// Issue - MultipleRepresentations
		allSeasonCasts = new HashSet<>();
		allEpCrews = new HashSet<>();
		for(Season tempSeriesSeason : tempSeriesSeasonsSet)
		{
			log.info("    Running check for season id {} S{}", tempSeriesSeason.getId(), tempSeriesSeason.getSeasonNo());
			Season oldSeriesSeason = findSeasonObject(oldSeriesSeasonsSet, tempSeriesSeason);
			if(oldSeriesSeason==null)
			{
				log.info("      No such season available !!!");
				Set<SeasonCast> seasonCasts = new HashSet<>();
				for(SeasonCast cast : tempSeriesSeason.getSeasonCast())
					if(allSeasonCasts.contains(cast))
					{
						for(SeasonCast allSeasonUniqueCast : allSeasonCasts)
							if(allSeasonUniqueCast.equals(cast))
							{
								seasonCasts.add(allSeasonUniqueCast);
								break;
							}
					}
					else
					{
						allSeasonCasts.add(cast);
						seasonCasts.add(cast);
					}
				tempSeriesSeason.setSeasonCast(seasonCasts);
				tempSeriesSeason.setLastModified(LocalDateTime.now());
				finalSeasons.add(tempSeriesSeason);
				log.info("      Therefore added !!!");
				continue;
			}
			if(!oldSeriesSeason.getName().equals(tempSeriesSeason.getName()))
			{
				log.info("    Season 'name' mismatched");
				oldSeriesSeason.setName(tempSeriesSeason.getName());
			}
			if(!oldSeriesSeason.getEpisodeCount().equals(tempSeriesSeason.getEpisodeCount()))
			{
				log.info("    Season 'EpisodeCount' mismatched");
				oldSeriesSeason.setEpisodeCount(tempSeriesSeason.getEpisodeCount());
			}
			if(!oldSeriesSeason.getOverview().equals(tempSeriesSeason.getOverview()))
			{
				log.info("    Season 'Overview' mismatched");
				oldSeriesSeason.setOverview(tempSeriesSeason.getOverview());
			}
			if(!Objects.equals(oldSeriesSeason.getPosterPath(), tempSeriesSeason.getPosterPath()))
			{
				log.info("    Season 'PosterPath' mismatched");
				oldSeriesSeason.setPosterPath(tempSeriesSeason.getPosterPath());
			}
			// no check need for 'seasonNo'
			List<Episode> updatedEpisodes = getDeepEpisodesComparison(oldSeriesSeason.getEpisodes(), tempSeriesSeason.getEpisodes());
			oldSeriesSeason.setEpisodes(updatedEpisodes);
			// no check need for 'id2'
			if(!Objects.equals(oldSeriesSeason.getAirDate(), tempSeriesSeason.getAirDate()))
			{
				log.info("    Season 'AirDate' mismatched");
				oldSeriesSeason.setAirDate(tempSeriesSeason.getAirDate());
			}
			if(!oldSeriesSeason.getVoteAverage().equals(tempSeriesSeason.getVoteAverage()))
			{
				log.info("  Season 'VoteAverage' mismatched");
				oldSeriesSeason.setVoteAverage(tempSeriesSeason.getVoteAverage());
			}
			log.info("  Season 'SeasonCast' Syncing");
			Set<SeasonCast> seasonCasts = new HashSet<>();
			for(SeasonCast cast : tempSeriesSeason.getSeasonCast())
				if(allSeasonCasts.contains(cast))
				{
					for(SeasonCast allSeasonUniqueCast : allSeasonCasts)
						if(allSeasonUniqueCast.equals(cast))
						{
							seasonCasts.add(allSeasonUniqueCast);
							break;
						}
				}
				else
				{
					allSeasonCasts.add(cast);
					seasonCasts.add(cast);
				}
			oldSeriesSeason.setSeasonCast(seasonCasts);
			oldSeriesSeason.setLastModified(LocalDateTime.now());
			finalSeasons.add(oldSeriesSeason);
		}
		allSeasonCasts = null;
		allEpCrews = null;
		return finalSeasons;
	}

	private List<Episode> getDeepEpisodesComparison(List<Episode> oldSeriesSeasonsEpisodes, List<Episode> tempSeriesSeasonEpisodes)
	{
		List<Episode> finalEpisodes = new ArrayList<>();
		Set<Episode> oldSeriesSeasonEpisodesSet = new HashSet<>(oldSeriesSeasonsEpisodes);
		Set<Episode> tempSeriesSeasonEpisodesSet = new HashSet<>(tempSeriesSeasonEpisodes);
		Iterator<Episode> iterator = oldSeriesSeasonEpisodesSet.iterator();
		log.info("      Checking for such episodes which are removed from tmdb");
		while(iterator.hasNext())
		{
			Episode episode = iterator.next();
			log.info("      Running check for episode id {} S{}E{} name {}", episode.getId(), episode.getSeasonNo(), episode.getEpisodeNo(), episode.getName());
			if(!tempSeriesSeasonEpisodesSet.contains(episode))
			{
				log.info("Removing");
				iterator.remove();
				episodeRepository.delete(episode);
			}
		}
		// ??? RARE = what if there are few old episodes not present in latest payload ?
		// Issue - MultipleRepresentations
		allEpGuestStars = new HashSet<>();
		for(Episode tempSeriesSeasonEpisode : tempSeriesSeasonEpisodesSet)
		{
			log.info("      Running check for episode id {} S{}E{}", tempSeriesSeasonEpisode.getId(), tempSeriesSeasonEpisode.getSeasonNo(), tempSeriesSeasonEpisode.getEpisodeNo());
			Episode oldSeriesSeasonEpisode = findEpisodeObject(oldSeriesSeasonEpisodesSet, tempSeriesSeasonEpisode);
			if(oldSeriesSeasonEpisode==null)
			{
				log.info("      No such episode available !!!");
				Set<EpisodeCast> epCasts = new HashSet<>();
				for(EpisodeCast cast : tempSeriesSeasonEpisode.getGuestStars())
					if(allEpGuestStars.contains(cast))
					{
						for(EpisodeCast allEpUniqueCast : allEpGuestStars)
							if(allEpUniqueCast.equals(cast))
							{
								epCasts.add(allEpUniqueCast);
								break;
							}
					}
					else
					{
						allEpGuestStars.add(cast);
						epCasts.add(cast);
					}
				tempSeriesSeasonEpisode.setGuestStars(epCasts);
				Set<EpisodeCrew> epCrews = new HashSet<>();
				for(EpisodeCrew epCrew : tempSeriesSeasonEpisode.getCrew())
					if(allEpCrews.contains(epCrew))
					{
						for(EpisodeCrew allEpUniqueCrew : allEpCrews)
							if(allEpUniqueCrew.equals(epCrew))
							{
								epCrews.add(allEpUniqueCrew);
								break;
							}
					}
					else
					{
						allEpCrews.add(epCrew);
						epCrews.add(epCrew);
					}
				tempSeriesSeasonEpisode.setCrew(epCrews);
				tempSeriesSeasonEpisode.setLastModified(LocalDateTime.now());
				finalEpisodes.add(tempSeriesSeasonEpisode);
				log.info("      Therefore added !!!");
				continue;
			}
			if(!oldSeriesSeasonEpisode.getName().equals(tempSeriesSeasonEpisode.getName()))
			{
				log.info("        Episode 'Name' mismatched");
				oldSeriesSeasonEpisode.setName(tempSeriesSeasonEpisode.getName());
			}
			// no check need for 'episodeNo'
			// no check need for 'seasonNo'
			if(!Objects.equals(oldSeriesSeasonEpisode.getRuntime(), tempSeriesSeasonEpisode.getRuntime()))
			{
				log.info("        Episode 'Runtime' mismatched", oldSeriesSeasonEpisode.getRuntime(), oldSeriesSeasonEpisode.getRuntime());
				oldSeriesSeasonEpisode.setRuntime(tempSeriesSeasonEpisode.getRuntime());
			}
			if(!Objects.equals(oldSeriesSeasonEpisode.getOverview(), tempSeriesSeasonEpisode.getOverview()))
			{
				log.info("        Episode 'Overview' mismatched", oldSeriesSeasonEpisode.getOverview(), oldSeriesSeasonEpisode.getOverview());
				oldSeriesSeasonEpisode.setOverview(tempSeriesSeasonEpisode.getOverview());
			}
			if(!Objects.equals(oldSeriesSeasonEpisode.getStillPath(), tempSeriesSeasonEpisode.getStillPath()))
			{
				log.info("        Episode 'StillPath' mismatched", oldSeriesSeasonEpisode.getSeasonNo(), oldSeriesSeasonEpisode.getEpisodeNo());
				oldSeriesSeasonEpisode.setStillPath(tempSeriesSeasonEpisode.getStillPath());
			}
			// no check need for 'tmdbId'
			// no check need for 'season'
			if(!Objects.equals(oldSeriesSeasonEpisode.getAirDate(), tempSeriesSeasonEpisode.getAirDate()))
			{
				log.info("        Episode 'AirDate' mismatched");
				oldSeriesSeasonEpisode.setAirDate(tempSeriesSeasonEpisode.getAirDate());
			}
			if(!oldSeriesSeasonEpisode.getType().equals(tempSeriesSeasonEpisode.getType()))
			{
				log.info("        Episode 'Type' mismatched");
				oldSeriesSeasonEpisode.setType(tempSeriesSeasonEpisode.getType());
			}
			if(!oldSeriesSeasonEpisode.getVoteAverage().equals(tempSeriesSeasonEpisode.getVoteAverage()))
			{
				log.info("        Episode 'VoteAverage' mismatched");
				oldSeriesSeasonEpisode.setVoteAverage(tempSeriesSeasonEpisode.getVoteAverage());
			}
			if(!oldSeriesSeasonEpisode.getVoteCount().equals(tempSeriesSeasonEpisode.getVoteCount()))
			{
				log.info("        Episode 'VoteCount' mismatched");
				oldSeriesSeasonEpisode.setVoteCount(tempSeriesSeasonEpisode.getVoteCount());
			}
			if(!oldSeriesSeasonEpisode.getProductionCode().equals(tempSeriesSeasonEpisode.getProductionCode()))
			{
				log.info("        Episode 'ProductionCode' mismatched");
				oldSeriesSeasonEpisode.setProductionCode(tempSeriesSeasonEpisode.getProductionCode());
			}
			log.info("        Episode 'EpisodeCast' Syncing");
			Set<EpisodeCast> epCasts = new HashSet<>();
			for(EpisodeCast epCast : tempSeriesSeasonEpisode.getGuestStars())
				if(allEpGuestStars.contains(epCast))
				{
					for(EpisodeCast allEpUniqueCast : allEpGuestStars)
						if(allEpUniqueCast.equals(epCast))
						{
							epCasts.add(allEpUniqueCast);
							break;
						}
				}
				else
				{
					allEpGuestStars.add(epCast);
					epCasts.add(epCast);
				}
			oldSeriesSeasonEpisode.setGuestStars(epCasts);
			log.info("        Episode 'EpisodeCrew' Syncing");
			Set<EpisodeCrew> epCrews = new HashSet<>();
			for(EpisodeCrew cast : tempSeriesSeasonEpisode.getCrew())
				if(allEpCrews.contains(cast))
				{
					for(EpisodeCrew allEpUniqueCrew : allEpCrews)
						if(allEpUniqueCrew.equals(cast))
						{
							epCrews.add(allEpUniqueCrew);
							break;
						}
				}
				else
				{
					allEpCrews.add(cast);
					epCrews.add(cast);
				}
			oldSeriesSeasonEpisode.setCrew(epCrews);
			oldSeriesSeasonEpisode.setLastModified(LocalDateTime.now());
			finalEpisodes.add(oldSeriesSeasonEpisode);
		}
		allEpGuestStars = null;
		return finalEpisodes;
	}

	private Episode findEpisodeObject(Set<Episode> set, Episode targetObject)
	{
		for(Episode obj : set)
			if(obj.equals(targetObject))
				return obj;
		return null;
	}

	private static Season findSeasonObject(Set<Season> seasons, Season targetObject)
	{
		for(Season obj : seasons)
			if(obj.equals(targetObject))
				return obj;
		return null;
	}

	private Series addSeries(Integer tmdbId)
	{
		Series series = fetchSeriesFromTmdb(tmdbId);
		if(series==null)
			throw new RuntimeException("Series cannot be null !!! Require urgent fix !!!");
		servieRepository.save(series);
		log.info("Series {}, added successfully !!", tmdbId);
		return series;
	}

	private Series fetchSeriesFromTmdb(Integer tmdbId)
	{
		log.info("Fetching series {}", tmdbId);
		Series series = new Series();
		// DTO can be created just to extract the necessary parts
		HttpMethod httpMethod = Objects.requireNonNull(HttpMethod.GET);
		try
		{
			ResponseEntity<Series> seriesResponse = restTemplate.exchange("https://api.themoviedb.org/3/"+"tv"+"/"+tmdbId+"?api_key="+apiKey, httpMethod, httpEntity, Series.class);
			if(seriesResponse.getStatusCode()==HttpStatus.OK)
			{
				series = seriesResponse.getBody();
				if(series!=null)
				{
					LocalDateTime localDateTime = LocalDateTime.now();
					series.setLastModified(localDateTime);
					List<Season> seasons = new ArrayList<>();
					// the seasons payload got from above servie api call does not have episodes data, therefore making api call for each
					for(Season seasonBrief : series.getSeasons())
					{
						ResponseEntity<Season> response = restTemplate.exchange("https://api.themoviedb.org/3/"+"tv"+"/"+tmdbId+"/season/"+seasonBrief.getSeasonNo()+"?api_key="+apiKey, httpMethod, httpEntity, Season.class);
						Season season = response.getBody();
						if(season!=null)
						{
							ResponseEntity<SeasonCredits> seasonCreditsResponse = restTemplate.exchange("https://api.themoviedb.org/3/"+"tv"+"/"+tmdbId+"/season/"+seasonBrief.getSeasonNo()+"/credits?api_key="+apiKey, httpMethod, httpEntity, SeasonCredits.class);
							SeasonCredits seasonCredits = seasonCreditsResponse.getBody();
							if(seasonCredits!=null)
								season.setSeasonCast(seasonCredits.getCast());
							season.setLastModified(localDateTime);
							season.setSeries(series);
							season.setEpisodeCount(seasonBrief.getEpisodeCount());
							for(Episode episode : season.getEpisodes())
							{
								episode.setLastModified(localDateTime);
								episode.setSeason(season);
							}
							seasons.add(season);
						}
					}
					// Issue - MultipleRepresentations
					allEpGuestStars = new HashSet<>();
					allEpCrews = new HashSet<>();
					allSeasonCasts = new HashSet<>();
					for(Season season : seasons)
					{
						Set<SeasonCast> seasonCasts = new HashSet<>();
						for(SeasonCast cast : season.getSeasonCast())
							if(allSeasonCasts.contains(cast))
							{
								for(SeasonCast allSeasonUniqueCast : allSeasonCasts)
									if(allSeasonUniqueCast.equals(cast))
									{
										seasonCasts.add(allSeasonUniqueCast);
										break;
									}
							}
							else
							{
								allSeasonCasts.add(cast);
								seasonCasts.add(cast);
							}
						season.setSeasonCast(seasonCasts);
						for(Episode episode : season.getEpisodes())
						{
							Set<EpisodeCrew> epCrews = new HashSet<>();
							for(EpisodeCrew cast : episode.getCrew())
								if(allEpCrews.contains(cast))
								{
									for(EpisodeCrew allEpUniqueCast : allEpCrews)
										if(allEpUniqueCast.equals(cast))
										{
											epCrews.add(allEpUniqueCast);
											break;
										}
								}
								else
								{
									allEpCrews.add(cast);
									epCrews.add(cast);
								}
							episode.setCrew(epCrews);
							Set<EpisodeCast> epCasts = new HashSet<>();
							for(EpisodeCast cast : episode.getGuestStars())
								if(allEpGuestStars.contains(cast))
								{
									for(EpisodeCast allEpUniqueCast : allEpGuestStars)
										if(allEpUniqueCast.equals(cast))
										{
											epCasts.add(allEpUniqueCast);
											break;
										}
								}
								else
								{
									allEpGuestStars.add(cast);
									epCasts.add(cast);
								}
							episode.setGuestStars(epCasts);
						}
					}
					allEpGuestStars = null;
					allEpCrews = null;
					allSeasonCasts = null;
					series.setSeasons(seasons);
					series.setChildtype("tv");
					series.setGenres(genreUtils.convertTmdbGenresToTrackServieGenres(series.getGenres()));
					series.setLastModified(LocalDateTime.now());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		log.info("Fetched series {}", tmdbId);
		return series;
	}

	public ServieDtoServiePage getServie(@NonNull Integer userId, String childtype, Integer tmdbId)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		// wrong -> its trying to add servie again just because its not present in user's database
		ServieDtoServiePage servieDtoServiePage = servieRepository.getServieForServiePage(user, childtype, tmdbId).orElseGet(() -> searchServie(childtype, tmdbId));
		if(childtype.equals("tv"))
		{
			List<SeasonDtoServiePage> seasons = userSeasonDataRepository.getSeasons(user, childtype, tmdbId).stream().sorted(Comparator.comparing(SeasonDtoServiePage::getSeasonNo)).collect(Collectors.toList());
			servieDtoServiePage.setSeasons(seasons);
		}
		Set<GenreDtoServiePage> genreDtos = genreRepository.getGenresForServiePage(tmdbId, childtype);
		servieDtoServiePage.setGenres(genreDtos);
		if(childtype.equals("movie"))
		{
			Servie servie = servieRepository.findById(new ServieKey(childtype, tmdbId)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey(childtype, tmdbId).toString()));
			Movie mm = (Movie) servie;
			Set<MovieCast> movieCasts = mm.getCast();
			List<MovieCast> movieCastsList = new ArrayList<>(movieCasts);
			movieCastsList.sort(Comparator.comparingInt(MovieCast::getOrder));
			servieDtoServiePage.setCast(movieCastsList);
			if(mm.getBelongsToCollection()!=null)
			{
				servieDtoServiePage.setCollectionId(mm.getCollection().getId());
				servieDtoServiePage.setCollectionName(mm.getCollection().getName());
				servieDtoServiePage.setColleactionPosterPath(mm.getCollection().getPosterPath());
			}
		}
		return servieDtoServiePage;
	}

	private ServieDtoServiePage searchServie(String childtype, Integer tmdbId)
	{
		Servie servie = addServie(childtype, tmdbId);
		if(childtype.equals("tv"))
		{
			Series series = (Series) servie;
			ServieDtoServiePage ser = new ServieDtoServiePage(tmdbId, childtype, servie.getTitle(), servie.getStatus(), servie.getTagline(), servie.getOverview(), servie.getBackdropPath(), servie.getLastModified(), null, null, series.getTotalSeasons(), series.getTotalEpisodes(), 0, false);
			return ser;
		}
		else
		{
			Movie movie = (Movie) servie;
			ServieDtoServiePage ser = new ServieDtoServiePage(tmdbId, childtype, servie.getTitle(), servie.getStatus(), servie.getTagline(), servie.getOverview(), servie.getBackdropPath(), servie.getLastModified(), movie.getReleaseDate(), movie.getRuntime(), null, null, 0, false);
			return ser;
		}
	}

	public SearchResultDtoSearchPage searchServies(@NonNull Integer userId, String childtype, String servieName, Integer year, int pageNumber)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		HttpMethod httpMethod = Objects.requireNonNull(HttpMethod.GET);
		ResponseEntity<SearchResultDtoSearchPage> response = restTemplate.exchange("https://api.themoviedb.org/3/search/"+childtype+"?api_key="+apiKey+"&query="+servieName+"&year="+year+"&page="+pageNumber, httpMethod, httpEntity, SearchResultDtoSearchPage.class);
		SearchResultDtoSearchPage searchResult = response.getBody();
		if(searchResult!=null)
		{
			List<SeriesDtoSearchPage> results = searchResult.getResults();
			// .stream()
			// .filter(yourObject -> "hi".equals(yourObject.getLanguage())
			//         || "kn".equals(yourObject.getLanguage())
			//         || "ta".equals(yourObject.getLanguage())
			//         || "mr".equals(yourObject.getLanguage()))
			// .collect(Collectors.toList());
			searchResult.setResults(results);
			List<Integer> tmdbIds = new ArrayList<>();
			for(SeriesDtoSearchPage s : results)
			{
				tmdbIds.add(s.getTmdbId());
				s.setCompleted(false);
				s.setChildtype(childtype);
			}
			List<UserServieData> usds = userServieDataRepository.findByUserIdAndChildtypeAndTmdbIdIn(user, childtype, tmdbIds);
			if(!usds.isEmpty())
				for(UserServieData usd : usds)
				{
					Optional<Servie> servie = servieRepository.findById(new ServieKey(usd.getServie().getChildtype(), usd.getServie().getTmdbId()));
					if(servie.isPresent())
					{
						Servie servieObj = servie.get();
						Series series = new Series();
						Integer tmdbId = servieObj.getTmdbId();
						for(SeriesDtoSearchPage s : results)
							if(s.getTmdbId().equals(tmdbId))
							{
								s.setFound(true);
								s.setCompleted(usd.getCompleted());
								if(servieObj instanceof Series)
								{
									series = (Series) servieObj;
									s.setTotalEpisodes(series.getTotalEpisodes());
									s.setEpisodesWatched(usd.getEpisodesWatched());
								}
							}
							else
								s.setFound(false);
					}
				}
		}
		return searchResult;
	}

	public ResponseDtoHomePage getServiesByFilter(@NonNull Integer userId, String childtype, Boolean watched, List<Integer> genreIds, List<String> languages, List<String> statuses, Integer startYear, Integer endYear, int pageNumber, String sortBy, String sortDir)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		Sort sort = (sortDir.equals("asc"))? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		// JPQL/HQL APPROACH (NOT WORKED FOR MULTI SELECTIONS FILTERS)
		List<Genre> genres = new ArrayList<>();
		if(genreIds!=null)
			for(Integer genreId : genreIds)
			{
				if(genreId==null)
					throw new RuntimeException("GenreId cannot be null !!! Require urgent fix !!!");
				Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new ResourceNotFoundException("Genre", "Id", genreId.toString()));
				genres.add(genre);
			}
		// Page<ServieDtoHomePage> page = servieRepository.getServiesByHomePageFilter(user, childtype, watched, languages, statuses, startYear, endYear, pageable);
		Page<ServieDtoHomePage> page = csri.getTempDtosCB(user, childtype, languages, genres, statuses, pageable);
		List<ServieDtoHomePage> servies = page.getContent();
		ResponseDtoHomePage responseDto = new ResponseDtoHomePage();
		responseDto.setServies(servies);
		responseDto.setPageNumber(page.getNumber());
		responseDto.setPageSize(page.getSize());
		responseDto.setNumberOfElementsOnPage(page.getNumberOfElements());
		responseDto.setTotalPages(page.getTotalPages());
		responseDto.setFirstPage(page.isFirst());
		responseDto.setLastPage(page.isLast());
		responseDto.setTotalElements(page.getTotalElements());
		responseDto.setHasPrevious(page.hasPrevious());
		responseDto.setHasNext(page.hasNext());
		return responseDto;
	}

	public void removeServie(Integer tmdbId, String childtype)
	{
		Servie servie = servieRepository.findById(new ServieKey(childtype, tmdbId)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey(childtype, tmdbId).toString()));
		if(servie==null)
			throw new RuntimeException("Servie cannot be null !!! Require urgent fix !!!");
		servieRepository.delete(servie);
	}

	public List<Image> getServieBackdrops(String type, Integer tmdbId)
	{
		HttpMethod httpMethod = Objects.requireNonNull(HttpMethod.GET);
		ResponseEntity<SeriesBackdropsDto> response = restTemplate.exchange("https://api.themoviedb.org/3/"+type+"/"+tmdbId+"/images?api_key="+apiKey, httpMethod, httpEntity, SeriesBackdropsDto.class);
		SeriesBackdropsDto imageSearchSeriesDto = response.getBody();
		List<Image> images = new ArrayList<>();
		if(imageSearchSeriesDto!=null)
			images = imageSearchSeriesDto.getBackdrops();
		return images;
	}

	public void changeBackdrop(@NonNull Integer userId, String childtype, Integer tmdbId, String filePath)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		Servie servie = servieRepository.findById(new ServieKey(childtype, tmdbId)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey(childtype, tmdbId).toString()));
		UserServieData userServieData = userServieDataRepository.findById(new UserServieDataKey(user, servie)).get();
		userServieData.setBackdropPath(filePath);
		userServieDataRepository.save(userServieData);
	}

	public List<Image> getServiePosters(String childtype, Integer tmdbId)
	{
		HttpMethod httpMethod = Objects.requireNonNull(HttpMethod.GET);
		ResponseEntity<SeriesPostersDto> response = restTemplate.exchange("https://api.themoviedb.org/3/"+childtype+"/"+tmdbId+"/images?api_key="+apiKey, httpMethod, httpEntity, SeriesPostersDto.class);
		SeriesPostersDto imageSearchSeriesDto = response.getBody();
		List<Image> images = new ArrayList<>();
		if(imageSearchSeriesDto!=null)
			images = imageSearchSeriesDto.getPosters();
		return images;
	}

	public void changePoster(@NonNull Integer userId, String childtype, Integer tmdbId, String filePath)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		Servie servie = servieRepository.findById(new ServieKey(childtype, tmdbId)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey(childtype, tmdbId).toString()));
		UserServieData userServieData = userServieDataRepository.findById(new UserServieDataKey(user, servie)).get();
		userServieData.setPosterPath(filePath);
		userServieDataRepository.save(userServieData);
	}

	public void toggleServieWatch(@NonNull Integer userId, String childtype, Integer tmdbId)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		Servie servie = servieRepository.findById(new ServieKey(childtype, tmdbId)).orElseGet(() -> addServie(childtype, tmdbId));
		UserServieData userServieData = userServieDataRepository.findById(new UserServieDataKey(user, servie)).orElseGet(() -> userServieDataRepository.save(new UserServieData(user, servie)));
		if(childtype.equals("tv"))
		{
			List<Integer> seasonNos = new ArrayList<>();
			// if (Series 1 -> 0)
			//     get seasons with watched True/False
			// else (Series 0 -> 1)
			//     get seasons with watched Null/False
			if(userServieData.getCompleted()) // ToDo CHECK LATER = PROBABLY NOT REQUIRED
				seasonNos = userSeasonDataRepository.getAllNonNullSeasons(user, childtype, tmdbId);
			else
				// ToDo TO THINK ABOUT USER ???
				seasonNos = userSeasonDataRepository.getAllIncompleteSeasons(user, tmdbId);
			for(Integer seasonNo : seasonNos)
				toggleSeasonWatch(userId, tmdbId, seasonNo);
		}
		else
		{
			userServieData.setMovieWatched(!userServieData.getCompleted());
			userServieDataRepository.save(userServieData);
		}
	}

	// ??? FOR AVOIDING CIRCULAR DEPENDENCY
	// ??? CANNOT SAVE ANOTHER SEASON INTO A SERIES ALREADY HAVING A SEASON
	// @Transactional
	// Pattern for entities [UserServieData , UserSeasonData , UserEpisodeData]:
	// * AkashPersistSeparatelyPattern -> Save each entity individually
	// AkashPersistTogetherPattern -> Save the parent entity along with all the modifications in the child entities
	public void toggleSeasonWatch(@NonNull Integer userId, Integer tmdbId, Integer seasonNo)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		Servie servie = servieRepository.findById(new ServieKey("tv", tmdbId)).orElseGet(() -> addServie("tv", tmdbId));
		UserServieData userServieData = userServieDataRepository.findById(new UserServieDataKey(user, servie)).orElseGet(() -> userServieDataRepository.save(new UserServieData(user, servie)));
		UserSeasonData userSeasonData = userSeasonDataRepository.findById(new UserSeasonDataKey(userServieData, seasonNo)).orElseGet(() -> userSeasonDataRepository.save(new UserSeasonData(userServieData, seasonNo)));
		List<UserEpisodeData> userEpisodeDatas = userEpisodeDataRepository.getToggledEpisodes(tmdbId, seasonNo, !userSeasonData.getWatched());
		if(userEpisodeDatas==null)
			throw new RuntimeException("UserEpisodeData list cannot be null !!! Require urgent fix !!!");
		for(UserEpisodeData userEpisodeData : userEpisodeDatas)
			userEpisodeData.setUserSeasonData(userSeasonData);
		userEpisodeDataRepository.saveAll(userEpisodeDatas);
	}

	public ResponseDtoHomePage getServiesForWatchList(@NonNull Integer userId, int pageNumber, String sortBy, String sortDir)
	{
		userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		Sort sort = (sortDir.equals("asc"))? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<ServieDtoHomePage> page = servieRepository.getServiesForWatchList(pageable);
		List<ServieDtoHomePage> servies = page.getContent();
		ResponseDtoHomePage responseDto = new ResponseDtoHomePage();
		responseDto.setServies(servies);
		responseDto.setPageNumber(page.getNumber());
		responseDto.setPageSize(page.getSize());
		responseDto.setNumberOfElementsOnPage(page.getNumberOfElements());
		responseDto.setTotalPages(page.getTotalPages());
		responseDto.setFirstPage(page.isFirst());
		responseDto.setLastPage(page.isLast());
		responseDto.setTotalElements(page.getTotalElements());
		responseDto.setHasPrevious(page.hasPrevious());
		responseDto.setHasNext(page.hasNext());
		return responseDto;
	}

	public List<MovieDtoMovieCollectionPageDto> getMovieDtoMovieCollectionPageDtos(Integer id)
	{
		List<MovieDtoMovieCollectionPageDto> dtos = movieRepository.getMovieDtoMovieCollectionPageDtos(id);
		dtos = dtos.stream()
				.sorted(Comparator.comparing(dto -> dto.getReleaseDate()))
				.collect(Collectors.toList());
		return dtos;
	}
}
