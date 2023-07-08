package servie.track_servie.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import servie.track_servie.payload.primaryKeys.UserServieDataKey;
import servie.track_servie.entity.Cast;
import servie.track_servie.entity.Episode;
import servie.track_servie.entity.Genre;
import servie.track_servie.entity.Movie;
import servie.track_servie.entity.MovieCollection;
import servie.track_servie.entity.Season;
import servie.track_servie.entity.Series;
import servie.track_servie.entity.Servie;
import servie.track_servie.entity.User;
import servie.track_servie.entity.UserEpisodeData;
import servie.track_servie.entity.UserSeasonData;
import servie.track_servie.entity.UserServieData;
import servie.track_servie.exceptions.ResourceNotFoundException;
import servie.track_servie.repository.GenreRepository;
import servie.track_servie.repository.MovieCollectionRepository;
import servie.track_servie.repository.MovieRepository;
import servie.track_servie.repository.ServieRepository;
import servie.track_servie.repository.UserRepository;
import servie.track_servie.repository.UserSeasonDataRepository;
import servie.track_servie.repository.UserServieDataRepository;
import servie.track_servie.utils.GenreUtils;

@Service
public class ServieService
{
    @Autowired
    private CastService castService;
    @Autowired
    private ServieRepository servieRepository;
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
    private UserRepository userRepository;
    @Autowired
    private GenreUtils genreUtils;
    @Value("${tmdb.api.key}")
    private String apiKey;
    @Value("${home-page.page.size}")
    private int pageSize;
    private HttpHeaders headers = new HttpHeaders();
    private HttpEntity<?> httpEntity = new HttpEntity<>(headers);

    public void collectServie(Integer userId, String type, Integer tmdbId)
    {
        UserServieData userServieData = new UserServieData();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
        Optional<Servie> optionalServie = servieRepository.findById(new ServieKey(tmdbId, type));
        Servie servie = optionalServie.orElseGet(() -> addServie(type, tmdbId));
        userServieData.setUser(user);
        userServieData.setServie(servie);
        if(type.equals("tv"))
        {
            Series series = (Series) servie;
            List<UserSeasonData> userSeasonDatas = new ArrayList<>();
            for(Season season : series.getSeasons())
            {
                UserSeasonData userSeasonData = new UserSeasonData();
                List<UserEpisodeData> userEpisodeDatas = new ArrayList<>();
                for(Episode episode : season.getEpisodes())
                {
                    UserEpisodeData userEpisodeData = new UserEpisodeData();
                    userEpisodeData.setEpisodeNumber(episode.getEpisodeNumber());
                    userEpisodeData.setUserSeasonData(userSeasonData);
                    userEpisodeDatas.add(userEpisodeData);
                }
                userSeasonData.setEpisodes(userEpisodeDatas);
                userSeasonData.setEpisodeCount(season.getEpisodeCount());
                userSeasonData.setSeasonNumber(season.getSeasonNumber());
                userSeasonData.setUserServieData(userServieData);
                userSeasonDatas.add(userSeasonData);
            }
            userServieData.setSeasons(userSeasonDatas);
        }
        userServieDataRepository.save(userServieData);
    }

    private Servie addServie(String type, Integer tmdbId)
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
        Movie movie = new Movie();
        ResponseEntity<Movie> movieResponse = restTemplate.exchange("https://api.themoviedb.org/3/"+"movie"+"/"+tmdbId+"?api_key="+apiKey, HttpMethod.GET, httpEntity, Movie.class);
        if(movieResponse.getStatusCode()==HttpStatus.OK)
        {
            movie = movieResponse.getBody();
            if(movie!=null)
            {
                if(movie.getBelongsToCollection()!=null)
                {
                    addMovieCollection(movie.getBelongsToCollection().getCollectionId());
                    movie.setChildtype("movie");
                    return movie;
                }
                List<Cast> casts = castService.saveCastMembers(tmdbId, "movie");
                movie.setCasts(casts);
                movie.setChildtype("movie");
                movie.setGenres(genreUtils.convertTmdbGenresToTrackServieGenres(movie.getGenres()));
            }
        }
        return movieRepository.save(movie);
    }

    private void addMovieCollection(Integer collectionId)
    {
        MovieCollection movieCollection = new MovieCollection();
        ResponseEntity<MovieCollection> movieCollectionResponse = restTemplate.exchange("https://api.themoviedb.org/3/collection/"+collectionId+"?api_key="+apiKey, HttpMethod.GET, httpEntity, MovieCollection.class);
        if(movieCollectionResponse.getStatusCode()==HttpStatus.OK)
        {
            movieCollection = movieCollectionResponse.getBody();
            if(movieCollection!=null)
            {
                Set<Genre> newGenresFromCollection = new HashSet<>();
                List<Movie> finalMovies = new ArrayList<Movie>();
                // each movie has no [ImdbId, collection(Id,name,poster,backdrop), runtime, tagline, status] , no genres only genreIds
                for(Movie movie : movieCollection.getMovies())
                {
                    ResponseEntity<Movie> movieResponse = restTemplate.exchange("https://api.themoviedb.org/3/"+"movie"+"/"+movie.getTmdbId()+"?api_key="+apiKey, HttpMethod.GET, httpEntity, Movie.class);
                    if(movieResponse.getStatusCode()==HttpStatus.OK)
                    {
                        movie = movieResponse.getBody();
                        if(movie!=null)
                        {
                            movie.setChildtype("movie");
                            movie.setCollection(movieCollection);
                            Set<Genre> finalGenres = new HashSet<>();
                            Set<Genre> updatedGenres = genreUtils.convertTmdbGenresToTrackServieGenres(movie.getGenres());
                            for(Genre genre : updatedGenres)
                            {
                                Optional<Genre> repoGenre = genreRepository.findById(genre.getId());
                                if(repoGenre.isPresent())
                                    finalGenres.add(repoGenre.get());
                                else
                                {
                                    Boolean isPresent = false;
                                    for(Genre newGenre : newGenresFromCollection)
                                        if(newGenre.getId().equals(genre.getId()) && newGenre.getName().equals(genre.getName()))
                                        {
                                            isPresent = true;
                                            finalGenres.add(newGenre);
                                            break;
                                        }
                                    if(!isPresent)
                                    {
                                        newGenresFromCollection.add(genre);
                                        finalGenres.add(genre);
                                    }
                                }
                            }
                            movie.setGenres(finalGenres);
                            List<Cast> casts = castService.saveCastMembers(movie.getTmdbId(), "movie");
                            movie.setCasts(casts);
                            finalMovies.add(movie);
                        }
                    }
                }
                movieCollection.setMovies(finalMovies);
                movieCollectionRepository.save(movieCollection);
            }
        }
    }

    private Series addSeries(Integer tmdbId)
    {
        Series series = new Series();
        ResponseEntity<Series> seriesResponse = restTemplate.exchange("https://api.themoviedb.org/3/"+"tv"+"/"+tmdbId+"?api_key="+apiKey, HttpMethod.GET, httpEntity, Series.class);
        if(seriesResponse.getStatusCode()==HttpStatus.OK)
        {
            series = seriesResponse.getBody();
            if(series!=null)
            {
                List<Season> seasons = new ArrayList<>();
                int totalSeasons = series.getNumberOfSeasons();
                int i = 1;
                int j = series.getSeasons().get(0).getSeasonNumber();
                while(i<=totalSeasons)
                {
                    ResponseEntity<Season> response = restTemplate.exchange("https://api.themoviedb.org/3/"+"tv"+"/"+tmdbId+"/season/"+i+"?api_key="+apiKey, HttpMethod.GET, httpEntity, Season.class);
                    Season season = response.getBody();
                    if(season!=null)
                    {
                        season.setSeries(series);
                        season.setEpisodeCount(series.getSeasons().get(i-j).getEpisodeCount());
                        // season.setTmdbId(tmdbId);
                        for(Episode episode : season.getEpisodes())
                            episode.setSeason(season);
                        seasons.add(season);
                    }
                    i++;
                }
                series.setSeasons(seasons);
                series.setChildtype("tv");
                List<Cast> casts = castService.saveCastMembers(tmdbId, "tv");
                series.setCasts(casts);
                series.setGenres(genreUtils.convertTmdbGenresToTrackServieGenres(series.getGenres()));
                servieRepository.save(series);
            }
        }
        return series;
    }

    // Returns specific Series from the database which matches the criteria
    public ServieDtoServiePage getServie(Integer userId, String type, Integer tmdbId)
    {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
        ServieDtoServiePage servie = servieRepository.getServieForServiePage(user, type, tmdbId);
        Set<GenreDtoServiePage> genreDtos = genreRepository.getGenresForServiePage(tmdbId, type);
        // List<SeasonDtoServiePage> seasonDtos = servieRepository.getSeasonsFromServie()
        servie.setGenres(genreDtos);
        return servie;
    }

    // Returns a list of search related Series from the 3rd party api
    public SearchResultDtoSearchPage searchServies(Integer userId, String childtype, String servieName, int pageNumber)
    {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<SearchResultDtoSearchPage> response = restTemplate.exchange("https://api.themoviedb.org/3/search/"+childtype+"?api_key="+apiKey+"&query="+servieName+"&page="+pageNumber, HttpMethod.GET, httpEntity, SearchResultDtoSearchPage.class);
        SearchResultDtoSearchPage searchResult = response.getBody();
        if(searchResult!=null)
        {
            List<SeriesDtoSearchPage> results = searchResult.getResults();
            List<Integer> tmdbIds = new ArrayList<>();
            for(SeriesDtoSearchPage s : results)
            {
                tmdbIds.add(s.getTmdbId());
                s.setCompleted(false);
                s.setChildtype(childtype);
            }
            List<UserServieData> usds = userServieDataRepository.findByUserIdAndChildtypeAndTmdbIdIn(user, childtype, tmdbIds);
            if(!usds.isEmpty())
            {
                for(UserServieData usd : usds)
                {
                    Optional<Servie> servie = servieRepository.findById(new ServieKey(usd.getServie().getTmdbId(), usd.getServie().getChildtype()));
                    if(servie.isPresent())
                    {
                        Servie servieObj = servie.get();
                        Series series = new Series();
                        Integer tmdbId = servieObj.getTmdbId();
                        for(SeriesDtoSearchPage s : results)
                        {
                            if(s.getTmdbId().equals(tmdbId))
                            {
                                s.setFound(true);
                                s.setCompleted(usd.getCompleted());
                                if(servieObj instanceof Series)
                                {
                                    series = (Series) servieObj;
                                    s.setNumberOfEpisodes(series.getNumberOfEpisodes());
                                    s.setEpisodesWatched(usd.getEpisodesWatched());
                                }
                            }
                            else
                            {
                                s.setFound(false);
                            }
                        }
                    }
                }
            }
        }
        return searchResult;
    }

    // Returns all Servies from the database which matches the filter
    public ResponseDtoHomePage getServiesByFilter(Integer userId, String type, Boolean watched, List<Integer> genreIds, List<String> languages, List<String> statuses, Integer startYear, Integer endYear, int pageNumber, String sortBy, String sortDir)
    {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
        Sort sort = null;
        if(sortDir.equals("asc"))
            sort = Sort.by(sortBy).ascending();
        else if(sortDir.equals("desc"))
            sort = Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        List<Genre> genres = new ArrayList<>();
        for(Integer genreId : genreIds)
        {
            Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new ResourceNotFoundException("Genre", "Id", genreId.toString()));
            genres.add(genre);
        }
        // Filter 1 = type      [Null, Movies/Series]
        // Filter 2 = watched   [Null, watched/unwatched]
        // Filter 3 = genre     [Null, list[Action, Drama, ...]->OR/AND]
        // Filter 4 = language  [Null, list[Eng, Jap, Hin, ...]->OR]
        // Filter 5 = status    [Null, list[In Production, Cancelled, Released, ...]->OR]
        // Filter 6 = startDate [Null, After StartYear]
        // Filter 7 = endDate   [Null, Before EndYear]
        // Filter 8 = 6 + 7
        Page<ServieDtoHomePage> page;
        // ??? Need a way to manage both parameters (regardless empty or not) in a single query
        // if((genreIds!=null && !genreIds.isEmpty()) && watched!=null) // genreIds != null, watched != null
        //     page = servieRepository.findByCompletedAndGenres(userId, watched, genres, genres.size(), pageable);
        // else if((genreIds!=null && !genreIds.isEmpty()) && watched==null) // genreIds != null, watched == null
        //     page = servieRepository.findByGenres(userId, genres, genres.size(), pageable);
        // else if((genreIds==null || genreIds.isEmpty()) && watched!=null) // genreIds == null, watched != null
        //     page = servieRepository.findByCompleted(userId, watched, pageable);
        // else // genreIds == null, watched == null
        //     page = servieRepository.findAllServiesByUserId(userId, pageable);
        // 
        // WORKING
        // page = servieRepository.findByType(userId, type, pageable);
        // page = servieRepository.findByLanguages(userId, languages, pageable);
        // page = servieRepository.findByStatus(userId, statuses, pageable);
        // 
        // PARTIALLY WORKING
        // page = servieRepository.findServiesAfterYear(userId, startYear, pageable);
        // page = servieRepository.findServiesBeforeYear(userId, endYear, pageable);
        // page = servieRepository.findServiesBetweenStartYearAndEndYear(userId, startYear, endYear, pageable);
        // 
        // By All filters together
        page = servieRepository.getServiesByHomePageFilter(user, type, watched, languages, statuses, startYear, endYear, pageable);
        List<ServieDtoHomePage> servies = page.getContent();
        ResponseDtoHomePage responseDto = new ResponseDtoHomePage();
        responseDto.setServies(servies);
        responseDto.setPageNumber(page.getNumber());
        responseDto.setPageSize(page.getSize());
        responseDto.setTotalElements(page.getNumberOfElements());
        responseDto.setTotalPages(page.getTotalPages());
        responseDto.setLastPage(page.isLast());
        return responseDto;
    }

    // Removes a specific Series from the database
    public void removeServie(Integer tmdbId, String childtype)
    {
        Servie servie = servieRepository.findById(new ServieKey(tmdbId, childtype)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey(tmdbId, childtype).toString()));
        servieRepository.delete(servie);
    }

    // Returns list of img-Backdrops(for Series) from the 3rd party api
    public List<Image> getServieBackdrops(String type, Integer tmdbId)
    {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<SeriesBackdropsDto> response = restTemplate.exchange("https://api.themoviedb.org/3/"+type+"/"+tmdbId+"/images?api_key="+apiKey, HttpMethod.GET, httpEntity, SeriesBackdropsDto.class);
        SeriesBackdropsDto imageSearchSeriesDto = response.getBody();
        List<Image> images = new ArrayList<>();
        if(imageSearchSeriesDto!=null)
            images = imageSearchSeriesDto.getBackdrops();
        return images;
    }

    // Changes img-Backdrop of a Series
    public void changeBackdrop(String childtype, Integer tmdbId, String filePath)
    {
        Servie servie = servieRepository.findById(new ServieKey(tmdbId, childtype)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey(tmdbId, childtype).toString()));
        servie.setBackdropPath(filePath);
        servieRepository.save(servie);
    }

    // Returns list of img-Posters(for Series) from the 3rd party api
    public List<Image> getServiePosters(String type, Integer tmdbId)
    {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<SeriesPostersDto> response = restTemplate.exchange("https://api.themoviedb.org/3/"+type+"/"+tmdbId+"/images?api_key="+apiKey, HttpMethod.GET, httpEntity, SeriesPostersDto.class);
        SeriesPostersDto imageSearchSeriesDto = response.getBody();
        List<Image> images = new ArrayList<>();
        if(imageSearchSeriesDto!=null)
            images = imageSearchSeriesDto.getPosters();
        return images;
    }

    // Changes img-Poster of a Series
    public void changePoster(String childtype, Integer tmdbId, String filePath)
    {
        Servie servie = servieRepository.findById(new ServieKey(tmdbId, childtype)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey(tmdbId, childtype).toString()));
        servie.setPosterPath(filePath);
        servieRepository.save(servie);
    }

    // Toggles the watch value of a Series (after toggling watch value of all related Episodes)
    public void toggleServieWatch(Integer userId, String type, Integer tmdbId)
    {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
        Servie servie = servieRepository.findById(new ServieKey(tmdbId, type)).orElseGet(() -> addServie(type, tmdbId));
        UserServieData userServieData = userServieDataRepository.findById(new UserServieDataKey(user, servie)).orElse(new UserServieData(user, servie));
        if(type.equals("tv"))
        {
            Series series = (Series) servie;
            List<UserSeasonData> userSeasonDatas = new ArrayList<>();
            for(Season season : series.getSeasons())
            {
                UserSeasonData userSeasonData = new UserSeasonData();
                List<UserEpisodeData> userEpisodeDatas = new ArrayList<>();
                for(Episode episode : season.getEpisodes())
                {
                    UserEpisodeData userEpisodeData = new UserEpisodeData();
                    userEpisodeData.setEpisodeNumber(episode.getEpisodeNumber());
                    userEpisodeData.setUserSeasonData(userSeasonData);
                    userEpisodeData.setWatched(!userServieData.getCompleted());
                    userEpisodeDatas.add(userEpisodeData);
                }
                userSeasonData.setEpisodes(userEpisodeDatas);
                userSeasonData.setEpisodeCount(season.getEpisodeCount());
                userSeasonData.setSeasonNumber(season.getSeasonNumber());
                userSeasonData.setUserServieData(userServieData);
                userSeasonDatas.add(userSeasonData);
            }
            userServieData.setSeasons(userSeasonDatas);
            userServieDataRepository.save(userServieData);
        }
        else
        {
            userServieData.setMovieWatched(!userServieData.getCompleted());
            userServieDataRepository.save(userServieData);
        }
    }

    public List<SeasonDtoServiePage> getSeasonsForSerivePage(Integer userId, String type, Integer tmdbId)
    {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
        return userSeasonDataRepository.getSeasons(user, type, tmdbId);
    }
}
