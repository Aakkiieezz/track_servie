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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import servie.track_servie.payload.dtos.operationsHomePageDtos.ResponseDtoHomePage;
import servie.track_servie.payload.dtos.operationsImage.Image;
import servie.track_servie.payload.dtos.operationsImage.SeriesPageDtos.SeriesBackdropsDto;
import servie.track_servie.payload.dtos.operationsImage.SeriesPageDtos.SeriesPostersDto;
import servie.track_servie.payload.dtos.operationsSearch.SearchPageDtos.SearchResultDtoSearchPage;
import servie.track_servie.payload.primaryKeys.ServieKey;
import servie.track_servie.entities.Episode;
import servie.track_servie.entities.Genre;
import servie.track_servie.entities.Movie;
import servie.track_servie.entities.MovieCollection;
import servie.track_servie.entities.Season;
import servie.track_servie.entities.Series;
import servie.track_servie.entities.Servie;
import servie.track_servie.entities.UserEpisodeData;
import servie.track_servie.entities.UserServieData;
import servie.track_servie.exceptions.ResourceNotFoundException;
import servie.track_servie.payload.dtos.ExternalIdsDto;
import servie.track_servie.repository.GenreRepository;
import servie.track_servie.repository.MovieCollectionRepository;
import servie.track_servie.repository.MovieRepository;
import servie.track_servie.repository.SeriesRepository;
import servie.track_servie.repository.ServieRepository;
import servie.track_servie.repository.UserEpisodeDataRepository;
import servie.track_servie.repository.UserRepository;
import servie.track_servie.repository.UserServieDataRepository;
import servie.track_servie.utils.GenreUtils;

@Service
public class ServieService
{
    @Autowired
    private ServieRepository servieRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SeriesRepository seriesRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieCollectionRepository movieCollectionRepository;
    @Autowired
    private UserServieDataRepository userServieDataRepository;
    @Autowired
    private UserEpisodeDataRepository userEpisodeDataRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GenreUtils genreUtils;
    @Value("${tmdb.api.key}")
    private String apiKey;

    // Adds a specific Servie (along with all its Seasons,Episodes,...) to the database, from the 3rd party api
    // Break it into multiple methods
    public void addServie(Integer userId, String type, Integer tmdbId)
    {
        UserServieData userServieData = new UserServieData();
        UserEpisodeData userEpisodeData = new UserEpisodeData();
        userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        if(type.equals("tv"))
        {
            ResponseEntity<Series> seriesResponse = restTemplate.exchange("https://api.themoviedb.org/3/"+type+"/"+tmdbId+"?api_key="+apiKey, HttpMethod.GET, httpEntity, Series.class);
            Series series = seriesResponse.getBody();
            if(series!=null)
            {
                Optional<Series> seriesCheck = seriesRepository.findByTmdbId(series.getTmdbId());
                if(seriesCheck.isEmpty())
                {
                    List<Season> seasons = new ArrayList<>();
                    int totalSeasons = series.getNumberOfSeasons();
                    int i = 1;
                    int j = series.getSeasons().get(0).getSeasonNumber();
                    while(i<=totalSeasons)
                    {
                        ResponseEntity<Season> response = restTemplate.exchange("https://api.themoviedb.org/3/"+type+"/"+tmdbId+"/season/"+i+"?api_key="+apiKey, HttpMethod.GET, httpEntity, Season.class);
                        Season season = response.getBody();
                        if(season!=null)
                        {
                            season.setEpisodeCount(series.getSeasons().get(i-j).getEpisodeCount());
                            season.setTmdbId(tmdbId);
                            season.setId(series.getTmdbId().toString()+"-s"+String.format("%02d", season.getSeasonNumber()));
                            for(Episode episode : season.getEpisodes())
                            {
                                episode.setId(season.getId()+"-e"+String.format("%02d", episode.getEpisodeNumber()));
                                userEpisodeData.setEpisodeId(episode.getId());
                                userEpisodeData.setEpisodeNumber(episode.getEpisodeNumber());
                                userEpisodeData.setSeasonNumber(episode.getSeasonNumber());
                                userEpisodeData.setTmdbId(tmdbId);
                                userEpisodeData.setUserId(userId);
                                userEpisodeDataRepository.save(userEpisodeData);
                            }
                            seasons.add(season);
                        }
                        i++;
                    }
                    series.setSeasons(seasons);
                    ResponseEntity<ExternalIdsDto> response = restTemplate.exchange("https://api.themoviedb.org/3/"+type+"/"+tmdbId+"/external_ids?api_key="+apiKey, HttpMethod.GET, httpEntity, ExternalIdsDto.class);
                    ExternalIdsDto imdb = response.getBody();
                    if(imdb!=null)
                        series.setImdbId(imdb.getImdb_id());
                    series.setChildtype("tv");
                    series.setGenres(genreUtils.mapMovieGenresFromTmdbDBToAkashDB(series.getGenres()));
                    servieRepository.save(series);
                    userServieData.setTmdbId(series.getTmdbId());
                    userServieData.setChildtype(series.getChildtype());
                    userServieData.setUserId(userId);
                    userServieDataRepository.save(userServieData);
                }
                else
                {
                    for(Season season : seriesCheck.get().getSeasons())
                    {
                        for(Episode episode : season.getEpisodes())
                        {
                            userEpisodeData.setEpisodeId(episode.getId());
                            userEpisodeData.setEpisodeNumber(episode.getEpisodeNumber());
                            userEpisodeData.setSeasonNumber(episode.getSeasonNumber());
                            userEpisodeData.setTmdbId(tmdbId);
                            userEpisodeData.setUserId(userId);
                            userEpisodeDataRepository.save(userEpisodeData);
                        }
                    }
                    userServieData.setTmdbId(seriesCheck.get().getTmdbId());
                    userServieData.setChildtype(seriesCheck.get().getChildtype());
                    userServieData.setUserId(userId);
                    userServieDataRepository.save(userServieData);
                }
            }
        }
        else
        {
            ResponseEntity<Movie> movieResponse = restTemplate.exchange("https://api.themoviedb.org/3/"+type+"/"+tmdbId+"?api_key="+apiKey, HttpMethod.GET, httpEntity, Movie.class);
            Movie movie = movieResponse.getBody();
            if(movie!=null)
            {
                Optional<Movie> movieCheck = movieRepository.findByTmdbId(movie.getTmdbId());
                if(movieCheck.isEmpty())
                {
                    if(movie.getBelongsToCollection()!=null)
                    {
                        int collectionId = movie.getBelongsToCollection().getCollectionId();
                        ResponseEntity<MovieCollection> movieCollectionResponse = restTemplate.exchange("https://api.themoviedb.org/3/collection/"+collectionId+"?api_key="+apiKey, HttpMethod.GET, httpEntity, MovieCollection.class);
                        MovieCollection movieCollection = movieCollectionResponse.getBody();
                        if(movieCollection!=null)
                        {
                            // each movie has no [ImdbId, collection(Id,name,poster,backdrop), runtime, tagline, status] , no genres only genreIds
                            Set<Genre> newGenresFromCollection = new HashSet<>();
                            List<Movie> finalMovies = new ArrayList<Movie>();
                            for(Movie moviePart : movieCollection.getMovies())
                            {
                                ResponseEntity<Movie> movieResponse2 = restTemplate.exchange("https://api.themoviedb.org/3/"+type+"/"+moviePart.getTmdbId()+"?api_key="+apiKey, HttpMethod.GET, httpEntity, Movie.class);
                                moviePart = movieResponse2.getBody();
                                moviePart.setChildtype("movie");
                                if(moviePart.getImdbId()==null)
                                    moviePart.setImdbId("?"+moviePart.getChildtype()+moviePart.getTmdbId());
                                moviePart.setCollection(movieCollection);
                                Set<Genre> finalGenres = new HashSet<>();
                                Set<Genre> updatedGenres = genreUtils.mapMovieGenresFromTmdbDBToAkashDB(moviePart.getGenres());
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
                                moviePart.setGenres(finalGenres);
                                finalMovies.add(moviePart);
                                userServieData.setTmdbId(moviePart.getTmdbId());
                                userServieData.setChildtype(moviePart.getChildtype());
                                userServieData.setUserId(userId);
                                userServieDataRepository.save(userServieData);
                            }
                            movieCollection.setMovies(finalMovies);
                            movieCollectionRepository.save(movieCollection);
                            return;
                        }
                    }
                    movie.setChildtype("movie");
                    movie.setGenres(genreUtils.mapMovieGenresFromTmdbDBToAkashDB(movie.getGenres()));
                    if(movie.getImdbId()==null)
                        movie.setImdbId("?"+movie.getChildtype()+movie.getTmdbId());
                    servieRepository.save(movie);
                    userServieData.setTmdbId(movie.getTmdbId());
                    userServieData.setChildtype(movie.getChildtype());
                    userServieData.setUserId(userId);
                    userServieDataRepository.save(userServieData);
                }
                else
                {
                    userServieData.setTmdbId(movieCheck.get().getTmdbId());
                    userServieData.setChildtype(movieCheck.get().getChildtype());
                    userServieData.setUserId(userId);
                    userServieDataRepository.save(userServieData);
                }
            }
        }
    }

    // Returns specific Series from the database which matches the criteria
    public Servie getServie(String type, Integer tmdbId)
    {
        Servie servie = servieRepository.findById(new ServieKey(tmdbId, type)).orElseThrow(() -> new ResourceNotFoundException("Series", "Id", tmdbId.toString()));
        // SeriesDtoSeriesPage seriesDto = converter.seriesToDto(series);
        return servie;
    }

    // Returns a list of search related Series from the 3rd party api
    public SearchResultDtoSearchPage searchServies(String type, String servieName, int pageNumber)
    {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<SearchResultDtoSearchPage> response = restTemplate.exchange("https://api.themoviedb.org/3/search/"+type+"?api_key="+apiKey+"&query="+servieName+"&page="+pageNumber, HttpMethod.GET, httpEntity, SearchResultDtoSearchPage.class);
        SearchResultDtoSearchPage searchResult = response.getBody();
        return searchResult;
    }

    // Returns all Servies from the database which matches the filter
    public ResponseDtoHomePage getServiesByFilter(Integer userId, String type, int pageNumber, int pageSize, String sortBy, String sortDir, List<Integer> genreIds, Boolean watched)
    {
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
        Page<Servie> page;
        // ??? Need a way to manage both parameters (regardless empty or not) in a single query
        if((genreIds!=null && !genreIds.isEmpty()) && watched!=null) // genreIds != null, watched != null
            page = servieRepository.findByCompletedAndGenres(watched, genres, genres.size(), pageable);
        else if((genreIds!=null && !genreIds.isEmpty()) && watched==null) // genreIds != null, watched == null
            page = servieRepository.findByGenres(genres, genres.size(), pageable);
        else if((genreIds==null || genreIds.isEmpty()) && watched!=null) // genreIds == null, watched != null
            page = servieRepository.findByCompleted(watched, pageable);
        else // genreIds == null, watched == null
             // page = servieRepository.findAll(pageable);
            page = servieRepository.findAllMoviesByUserId(userId, pageable);
        List<Servie> servies = page.getContent();
        ResponseDtoHomePage responseDto = new ResponseDtoHomePage();
        responseDto.setServies(servies);
        responseDto.setPageNumber(page.getNumber());
        responseDto.setPageSize(page.getSize());
        responseDto.setTotalElements(page.getNumberOfElements()); // ??? getting wrong count, since removing common elements later
        responseDto.setTotalPages(page.getTotalPages());
        responseDto.setLastPage(page.isLast());
        return responseDto;
    }

    // Removes a specific Series from the database
    public void removeServie(String imdbId)
    {
        Servie servie = servieRepository.findByImdbId(imdbId);//.orElseThrow(() -> new ImdbIdNotFoundException("Servie", "Id", servieId));
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
    public void changeBackdrop(String type, Integer tmdbId, String filePath)
    {
        Servie series = servieRepository.findById(new ServieKey(tmdbId, type)).orElseThrow(() -> new ResourceNotFoundException("Series", "Id", tmdbId.toString()));
        series.setBackdropPath(filePath);
        servieRepository.save(series);
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
    public void changePoster(String type, Integer tmdbId, String filePath)
    {
        Servie series = servieRepository.findById(new ServieKey(tmdbId, type)).orElseThrow(() -> new ResourceNotFoundException("Series", "Id", tmdbId.toString()));
        series.setPosterPath(filePath);
        servieRepository.save(series);
    }

    // Toggles the watch value of a Series (after toggling watch value of all related Episodes)
    public void toggleServieWatch(Integer userId, String type, Integer tmdbId)
    {
        if(type.equals("tv"))
        {
            Series series = seriesRepository.findById(new ServieKey(tmdbId, type)).orElseThrow(() -> new ResourceNotFoundException("Series", "Id", tmdbId.toString()));
            // without user
            // List<Episode> episodes = episodeRepository.findByTmdbIdAndWatched(tmdbId, series.getWatched());
            // for(Episode episode : episodes)
            //     episode.setWatched(!series.getWatched());
            // 
            // with user
            List<UserEpisodeData> userEpisodeDatas = userEpisodeDataRepository.findByUserIdAndTmdbIdAndWatched(userId, tmdbId, series.getWatched());
            for(UserEpisodeData userEpisodeData : userEpisodeDatas)
                userEpisodeData.setWatched(!series.getWatched());
            // 
            // series.setWatched(!series.getWatched()); Not needed
            seriesRepository.save(series);
        }
        else
        {
            movieRepository.findById(new ServieKey(tmdbId, type)).orElseThrow(() -> new ResourceNotFoundException("Movie", "Id", tmdbId.toString()));
            UserServieData userServieData = userServieDataRepository.findByUserIdAndTmdbIdAndChildtype(userId, tmdbId, type);
            userServieData.setMovieWatched(!userServieData.getMovieWatched());
            userServieDataRepository.save(userServieData);
            // movie.setWatched(!movie.getWatched());
            // movieRepository.save(movie);
        }
    }
}
