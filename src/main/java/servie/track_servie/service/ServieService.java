package servie.track_servie.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
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
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;
import servie.track_servie.payload.dtos.operationsHomePageDtos.ResponseDtoHomePage;
import servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage;
import servie.track_servie.payload.dtos.operationsImage.Image;
import servie.track_servie.payload.dtos.operationsImage.SeriesPageDtos.SeriesBackdropsDto;
import servie.track_servie.payload.dtos.operationsImage.SeriesPageDtos.SeriesPostersDto;
import servie.track_servie.payload.dtos.operationsSearch.SearchPageDtos.SearchResultDtoSearchPage;
import servie.track_servie.payload.dtos.operationsSearch.SearchPageDtos.SeriesDtoSearchPage;
import servie.track_servie.payload.dtos.operationsServiePageDtos.GenreDtoServiePage;
import servie.track_servie.payload.dtos.operationsServiePageDtos.SeasonDtoServiePage;
import servie.track_servie.payload.dtos.operationsServiePageDtos.ServieDtoServiePage;
import servie.track_servie.payload.primaryKeys.ServieKey;
import servie.track_servie.payload.primaryKeys.UserSeasonDataKey;
import servie.track_servie.payload.primaryKeys.UserServieDataKey;
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
import servie.track_servie.entity.credits.EpisodeCast;
import servie.track_servie.entity.credits.MovieCredits;
import servie.track_servie.entity.credits.SeasonCast;
import servie.track_servie.entity.credits.SeasonCredits;
import servie.track_servie.enums.ServieType;
import servie.track_servie.exceptions.ResourceNotFoundException;
import servie.track_servie.repository.CustomServieRepository;
import servie.track_servie.repository.GenreRepository;
import servie.track_servie.repository.MovieCollectionRepository;
import servie.track_servie.repository.MovieRepository;
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
	private CustomServieRepository csri;
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
	private GenreUtils genreUtils;
	@Value("${tmdb.api.key}")
	private String apiKey;
	@Value("${home-page.page.size}")
	private int pageSize;
	private HttpHeaders headers = new HttpHeaders();
	private HttpEntity<?> httpEntity = new HttpEntity<>(headers);

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
		log.info("Adding movie {}", tmdbId);
		Movie movie = new Movie();
		ResponseEntity<Movie> movieResponse = restTemplate.exchange("https://api.themoviedb.org/3/"+ServieType.MOVIE.toString()+"/"+tmdbId+"?api_key="+apiKey, HttpMethod.GET, httpEntity, Movie.class);
		if(movieResponse.getStatusCode()==HttpStatus.OK)
		{
			movie = movieResponse.getBody();
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
				ResponseEntity<MovieCredits> movieCreditsResponse = restTemplate.exchange("https://api.themoviedb.org/3/"+"movie"+"/"+tmdbId+"/credits?api_key="+apiKey, HttpMethod.GET, httpEntity, MovieCredits.class);
				MovieCredits movieCredits = movieCreditsResponse.getBody();
				if(movieCredits!=null)
				{
					movie.setCast(movieCredits.getCast());
					movie.setCrew(movieCredits.getCrew());
				}
				movie.setChildtype(ServieType.MOVIE.toString());
				movie.setGenres(genreUtils.convertTmdbGenresToTrackServieGenres(movie.getGenres()));
			}
		}
		log.info("    Movie {}, added successfully !!", tmdbId);
		return movieRepository.save(movie);
	}

	private void addMovieCollection(Integer collectionId)
	{
		log.info("Adding movie collection {}", collectionId);
		MovieCollection movieCollection = new MovieCollection();
		ResponseEntity<MovieCollection> movieCollectionResponse = restTemplate.exchange("https://api.themoviedb.org/3/collection/"+collectionId+"?api_key="+apiKey, HttpMethod.GET, httpEntity, MovieCollection.class);
		if(movieCollectionResponse.getStatusCode()==HttpStatus.OK)
		{
			movieCollection = movieCollectionResponse.getBody();
			if(movieCollection!=null)
			{
				List<Genre> allTrackServieGenres = genreRepository.findAll();
				List<Movie> finalMovies = new ArrayList<Movie>();
				Set<ProductionCompany> allProductionCompanies = new HashSet<>();
				Set<ProductionCountry> allProductionCountries = new HashSet<>();
				Set<SpokenLanguage> allSpokenLanguages = new HashSet<>();
				for(Movie movie : movieCollection.getMovies())
				{
					boolean exist = servieRepository.existsById(new ServieKey("movie", movie.getTmdbId()));
					if(exist)
					{
						log.error("{} {}, already exists", "movie", movie.getTmdbId());
						throw new RuntimeException("The movie already exists before saving the movie collection !!!");
					}
					log.info("    Adding movie {} as a part of collection", movie.getTmdbId());
					ResponseEntity<Movie> movieResponse = restTemplate.exchange("https://api.themoviedb.org/3/"+ServieType.MOVIE.toString()+"/"+movie.getTmdbId()+"?api_key="+apiKey, HttpMethod.GET, httpEntity, Movie.class);
					if(movieResponse.getStatusCode()==HttpStatus.OK)
					{
						movie = movieResponse.getBody();
						if(movie!=null)
						{
							ResponseEntity<MovieCredits> movieCreditsResponse = restTemplate.exchange("https://api.themoviedb.org/3/"+"movie"+"/"+movie.getTmdbId()+"/credits?api_key="+apiKey, HttpMethod.GET, httpEntity, MovieCredits.class);
							MovieCredits movieCredits = movieCreditsResponse.getBody();
							if(movieCredits!=null)
							{
								movie.setCast(movieCredits.getCast());
								movie.setCrew(movieCredits.getCrew());
							}
							movie.setLastModified(LocalDateTime.now());
							movie.setChildtype(ServieType.MOVIE.toString());
							movie.setCollection(movieCollection);
							// THIS APPROACH IS ALSO GOOD, BUT I THINK WE SHOULD MAINTAIN A SET WITH NEW COMMERS GENRES AND USE FROM IT
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
							// 
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
							//
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
							finalMovies.add(movie);
						}
					}
				}
				movieCollection.setMovies(finalMovies);
				movieCollectionRepository.save(movieCollection);
				log.info("    Added {} movies successfully !!", finalMovies.size());
			}
		}
	}

	private Series addSeries(Integer tmdbId)
	{
		log.info("Adding series {}", tmdbId);
		Series series = new Series();
		// DTO can be created just to extract the necessary parts
		ResponseEntity<Series> seriesResponse = restTemplate.exchange("https://api.themoviedb.org/3/"+"tv"+"/"+tmdbId+"?api_key="+apiKey, HttpMethod.GET, httpEntity, Series.class);
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
					ResponseEntity<Season> response = restTemplate.exchange("https://api.themoviedb.org/3/"+"tv"+"/"+tmdbId+"/season/"+seasonBrief.getSeasonNo()+"?api_key="+apiKey, HttpMethod.GET, httpEntity, Season.class);
					Season season = response.getBody();
					if(season!=null)
					{
						ResponseEntity<SeasonCredits> seasonCreditsResponse = restTemplate.exchange("https://api.themoviedb.org/3/"+"tv"+"/"+tmdbId+"/season/"+seasonBrief.getSeasonNo()+"/credits?api_key="+apiKey, HttpMethod.GET, httpEntity, SeasonCredits.class);
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
				Set<EpisodeCast> allEpGuestStars = new HashSet<>();
				Set<EpisodeCrew> allEpCrews = new HashSet<>();
				Set<SeasonCast> allSeasonCasts = new HashSet<>();
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
				series.setSeasons(seasons);
				series.setChildtype("tv");
				series.setGenres(genreUtils.convertTmdbGenresToTrackServieGenres(series.getGenres()));
				servieRepository.save(series);
				log.info("    Series {}, added successfully !!", tmdbId);
			}
		}
		return series;
	}

	public ServieDtoServiePage getServie(Integer userId, String type, Integer tmdbId)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		// wrong -> its trying to add servie again just because its not present in user's database
		ServieDtoServiePage servie = servieRepository.getServieForServiePage(user, type, tmdbId).orElseGet(() -> searchServie(type, tmdbId));
		if(type.equals("tv"))
		{
			List<SeasonDtoServiePage> seasons = userSeasonDataRepository.getSeasons(user, type, tmdbId).stream().sorted(Comparator.comparing(SeasonDtoServiePage::getSeasonNo)).collect(Collectors.toList());
			servie.setSeasons(seasons);
		}
		Set<GenreDtoServiePage> genreDtos = genreRepository.getGenresForServiePage(tmdbId, type);
		servie.setGenres(genreDtos);
		return servie;
	}

	private ServieDtoServiePage searchServie(String childtype, Integer tmdbId)
	{
		// ToDo - think which approach is most suitable :
		// *1) search servie, and save it into TrackServie database and then return it
		// 2) just return searched servie
		Servie servie = addServie(childtype, tmdbId);
		if(childtype.equals("tv"))
		{
			Series series = (Series) servie;
			ServieDtoServiePage ser = new ServieDtoServiePage(tmdbId, childtype, servie.getTitle(), servie.getStatus(), servie.getTagline(), servie.getOverview(), servie.getBackdropPath(), null, null, series.getTotalSeasons(), series.getTotalEpisodes(), 0, false);
			return ser;
		}
		else
		{
			Movie movie = (Movie) servie;
			ServieDtoServiePage ser = new ServieDtoServiePage(tmdbId, childtype, servie.getTitle(), servie.getStatus(), servie.getTagline(), servie.getOverview(), servie.getBackdropPath(), movie.getReleaseDate(), movie.getRuntime(), null, null, 0, false);
			return ser;
		}
	}

	public SearchResultDtoSearchPage searchServies(Integer userId, String childtype, String servieName, Integer year, int pageNumber)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		ResponseEntity<SearchResultDtoSearchPage> response = restTemplate.exchange("https://api.themoviedb.org/3/search/"+childtype+"?api_key="+apiKey+"&query="+servieName+"&year="+year+"&page="+pageNumber, HttpMethod.GET, httpEntity, SearchResultDtoSearchPage.class);
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

	public ResponseDtoHomePage getServiesByFilter(Integer userId, String childtype, Boolean watched, List<Integer> genreIds, List<String> languages, List<String> statuses, Integer startYear, Integer endYear, int pageNumber, String sortBy, String sortDir)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		Sort sort = null;
		if(sortDir.equals("asc"))
			sort = Sort.by(sortBy).ascending();
		else if(sortDir.equals("desc"))
			sort = Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		// JPQL/HQL APPROACH (NOT WORKED FOR MULTI SELECTIONS FILTERS)
		List<Genre> genres = new ArrayList<>();
		if(genreIds!=null)
			for(Integer genreId : genreIds)
			{
				Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new ResourceNotFoundException("Genre", "Id", genreId.toString()));
				genres.add(genre);
			}
		// Page<ServieDtoHomePage> page = servieRepository.getServiesByHomePageFilter(user, childtype, watched, languages, statuses, startYear, endYear, pageable);
		Page<ServieDtoHomePage> page = csri.getTempDtosCB(user, childtype, languages, genres, statuses, pageable);
		List<ServieDtoHomePage> servies = page.getContent();
		ResponseDtoHomePage responseDto = new ResponseDtoHomePage();
		responseDto.setServies(servies);
		responseDto.setPageNumber(page.getNumber()); // Returns the number of the current page. Is always non-negative.
		responseDto.setPageSize(page.getSize()); // Returns the size of current page
		responseDto.setTotalElements(page.getNumberOfElements()); // Returns the number of elements currently on this page
		responseDto.setTotalPages(page.getTotalPages()); // Returns the number of total pages (Showing total of entire Servie)
		responseDto.setFirstPage(page.isFirst()); // checks if first page
		responseDto.setLastPage(page.isLast()); // checks if last page
		responseDto.setTotalObjects(page.getTotalElements()); // Returns the number of elements (SHOWING total of entire Servie)
		responseDto.setHasPrevious(page.hasPrevious());// Returns if there is a previous page
		responseDto.setHasNext(page.hasNext()); //Returns if there is a next page
		return responseDto;
	}
	// @Scheduled(fixedRate = Integer.MAX_VALUE)
	// public void nativeApproach()
	// {
	// 	int pageNumber = 0;
	// 	String sortBy = "title";
	// 	String sortDir = "asc";
	// 	Sort sort = null;
	// 	if(sortDir.equals("asc"))
	// 		sort = Sort.by(sortBy).ascending();
	// 	else if(sortDir.equals("desc"))
	// 		sort = Sort.by(sortBy).descending();
	// 	Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
	// 	List<String> languagesfk = new ArrayList<>();
	// 	// languagesfk.add("ko");
	// 	// languagesfk.add("de");
	// 	String[] langs = new String[] {"ko", "de"};
	// 	// Page<Object[]> rs = servieRepository.getTempDtosFK(languagesfk, pageable);
	// 	Page<ServieDtoHomePage> rs = csri.getTempDtosCB(languagesfk, statuses, pageable);
	// 	System.out.println("lol");
	// }
	// private List<ServieDtoHomePage> mapper(Page<Object[]> resultSet)
	// {
	// 	List<Object[]> dtos = resultSet.getContent();
	// 	List<ServieDtoHomePage> servieDtoList = dtos.stream()
	// 			.map(row ->
	// 			{
	// 				String imdbId = (String) row[0];
	// 				Integer tmdbId = (Integer) row[1];
	// 				String childtype = (String) row[2];
	// 				String title = (String) row[3];
	// 				String posterPath = (String) row[4];
	// 				java.sql.Date sqlReleaseDate = (java.sql.Date) row[5];
	// 				LocalDate releaseDate = (sqlReleaseDate!=null? sqlReleaseDate.toLocalDate() : null);
	// 				Integer totalEpisodes = (Integer) row[6];
	// 				java.sql.Date sqlFirstAirDate = (java.sql.Date) row[7];
	// 				LocalDate firstAirDate = (sqlFirstAirDate!=null? sqlFirstAirDate.toLocalDate() : null);
	// 				java.sql.Date sqlLastAirDate = (java.sql.Date) row[8];
	// 				LocalDate lastAirDate = (sqlLastAirDate!=null? sqlLastAirDate.toLocalDate() : null);
	// 				// Long l = (Long) row[9];
	// 				// Integer episodesWatched = (l!=null? l.intValue() : null);
	// 				// BigDecimal bdCompleted = (BigDecimal) row[10];
	// 				// Boolean completed = bdCompleted!=null? bdCompleted.intValue()==1 : null;
	// 				return new ServieDtoHomePage(imdbId, tmdbId, childtype, title, posterPath, releaseDate, totalEpisodes, firstAirDate, lastAirDate, 2, true);
	// 			})
	// 			.collect(Collectors.toList());
	// 	return servieDtoList;
	// }

	public void removeServie(Integer tmdbId, String childtype)
	{
		Servie servie = servieRepository.findById(new ServieKey(childtype, tmdbId)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey(childtype, tmdbId).toString()));
		servieRepository.delete(servie);
	}

	public List<Image> getServieBackdrops(String type, Integer tmdbId)
	{
		ResponseEntity<SeriesBackdropsDto> response = restTemplate.exchange("https://api.themoviedb.org/3/"+type+"/"+tmdbId+"/images?api_key="+apiKey, HttpMethod.GET, httpEntity, SeriesBackdropsDto.class);
		SeriesBackdropsDto imageSearchSeriesDto = response.getBody();
		List<Image> images = new ArrayList<>();
		if(imageSearchSeriesDto!=null)
			images = imageSearchSeriesDto.getBackdrops();
		return images;
	}

	public void changeBackdrop(Integer userId, String childtype, Integer tmdbId, String filePath)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		Servie servie = servieRepository.findById(new ServieKey(childtype, tmdbId)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey(childtype, tmdbId).toString()));
		UserServieData userServieData = userServieDataRepository.findById(new UserServieDataKey(user, servie)).get();
		userServieData.setBackdropPath(filePath);
		userServieDataRepository.save(userServieData);
	}

	public List<Image> getServiePosters(String childtype, Integer tmdbId)
	{
		ResponseEntity<SeriesPostersDto> response = restTemplate.exchange("https://api.themoviedb.org/3/"+childtype+"/"+tmdbId+"/images?api_key="+apiKey, HttpMethod.GET, httpEntity, SeriesPostersDto.class);
		SeriesPostersDto imageSearchSeriesDto = response.getBody();
		List<Image> images = new ArrayList<>();
		if(imageSearchSeriesDto!=null)
			images = imageSearchSeriesDto.getPosters();
		return images;
	}

	public void changePoster(Integer userId, String childtype, Integer tmdbId, String filePath)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		Servie servie = servieRepository.findById(new ServieKey(childtype, tmdbId)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey(childtype, tmdbId).toString()));
		UserServieData userServieData = userServieDataRepository.findById(new UserServieDataKey(user, servie)).get();
		userServieData.setPosterPath(filePath);
		userServieDataRepository.save(userServieData);
	}

	public void toggleServieWatch(Integer userId, String childtype, Integer tmdbId)
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
	public void toggleSeasonWatch(Integer userId, Integer tmdbId, Integer seasonNo)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		Servie servie = servieRepository.findById(new ServieKey("tv", tmdbId)).orElseGet(() -> addServie("tv", tmdbId));
		UserServieData userServieData = userServieDataRepository.findById(new UserServieDataKey(user, servie)).orElseGet(() -> userServieDataRepository.save(new UserServieData(user, servie)));
		UserSeasonData userSeasonData = userSeasonDataRepository.findById(new UserSeasonDataKey(userServieData, seasonNo)).orElseGet(() -> userSeasonDataRepository.save(new UserSeasonData(userServieData, seasonNo)));
		List<UserEpisodeData> userEpisodeDatas = userEpisodeDataRepository.getToggledEpisodes(tmdbId, seasonNo, !userSeasonData.getWatched());
		for(UserEpisodeData userEpisodeData : userEpisodeDatas)
			userEpisodeData.setUserSeasonData(userSeasonData);
		userEpisodeDataRepository.saveAll(userEpisodeDatas);
	}
}
