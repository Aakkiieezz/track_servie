package servie.track_servie.service;

import java.util.ArrayList;
import java.util.List;
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
import servie.track_servie.dto.ExternalIdsDto;
import servie.track_servie.dto.operationsHomePageDtos.ResponseDtoHomePage;
import servie.track_servie.dto.operationsImage.Image;
import servie.track_servie.dto.operationsImage.SeriesPageDtos.SeriesBackdropsDto;
import servie.track_servie.dto.operationsImage.SeriesPageDtos.SeriesPostersDto;
import servie.track_servie.dto.operationsSearch.SearchPageDtos.SearchResultDtoSearchPage;
import servie.track_servie.entity.Episode;
import servie.track_servie.entity.Genre;
import servie.track_servie.entity.Key;
import servie.track_servie.entity.Movie;
import servie.track_servie.entity.Season;
import servie.track_servie.entity.Series;
import servie.track_servie.entity.Servie;
import servie.track_servie.exception.GenreNotFoundException;
import servie.track_servie.exception.TmdbIdNotFoundException;
import servie.track_servie.repository.EpisodeRepository;
import servie.track_servie.repository.GenreRepository;
import servie.track_servie.repository.MovieRepository;
import servie.track_servie.repository.SeriesRepository;
import servie.track_servie.repository.ServieRepository;

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
    private EpisodeRepository episodeRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Value("${tmdb.api.key}")
    private String apiKey;

    // Adds a specific Servie (along with all its Seasons,Episodes,...) to the database, from the 3rd party api
    public void addServie(String type, Integer tmdbId)
    {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        if(type.equals("tv"))
        {
            ResponseEntity<Series> seriesResponse = restTemplate.exchange("https://api.themoviedb.org/3/tv/"+tmdbId+"?api_key="+apiKey, HttpMethod.GET, httpEntity, Series.class);
            Series series = seriesResponse.getBody();
            if(series!=null)
            {
                List<Season> seasons = new ArrayList<>();
                int totalSeasons = series.getNumberOfSeasons();
                int i = 1;
                int j = series.getSeasons().get(0).getSeasonNumber();
                while(i<=totalSeasons)
                {
                    ResponseEntity<Season> response = restTemplate.exchange("https://api.themoviedb.org/3/tv/"+tmdbId+"/season/"+i+"?api_key="+apiKey, HttpMethod.GET, httpEntity, Season.class);
                    Season season = response.getBody();
                    if(season!=null)
                    {
                        season.setEpisodeCount(series.getSeasons().get(i-j).getEpisodeCount());
                        season.setTmdbId(tmdbId);
                        season.setId(series.getTmdbId().toString()+"-s"+String.format("%02d", season.getSeasonNumber()));
                        for(Episode episode : season.getEpisodes())
                            episode.setId(season.getId()+"-e"+String.format("%02d", episode.getEpisodeNumber()));
                        seasons.add(season);
                    }
                    i++;
                }
                series.setSeasons(seasons);
                ResponseEntity<ExternalIdsDto> response = restTemplate.exchange("https://api.themoviedb.org/3/tv/"+tmdbId+"/external_ids?api_key="+apiKey, HttpMethod.GET, httpEntity, ExternalIdsDto.class);
                ExternalIdsDto imdb = response.getBody();
                if(imdb!=null)
                    series.setImdbId(imdb.getImdb_id());
                series.setChildtype("tv");
                servieRepository.save(series);
            }
        }
        else
        {
            ResponseEntity<Movie> movieResponse = restTemplate.exchange("https://api.themoviedb.org/3/movie/"+tmdbId+"?api_key="+apiKey, HttpMethod.GET, httpEntity, Movie.class);
            Movie movie = movieResponse.getBody();
            if(movie!=null)
                movie.setChildtype("movie");
            servieRepository.save(movie);
        }
    }

    // Returns specific Series from the database which matches the criteria
    public Servie getServie(String type, Integer tmdbId)
    {
        Servie servie = servieRepository.findById(new Key(tmdbId, type)).orElseThrow(() -> new TmdbIdNotFoundException("Series", "Id", tmdbId));
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
    public ResponseDtoHomePage getServiesByFilter(int pageNumber, int pageSize, String sortBy, String sortDir, List<Integer> genreIds, Boolean watched)
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
            Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new GenreNotFoundException("Genre", "Id", genreId));
            genres.add(genre);
        }
        Page<Servie> page;
        // ??? Need a way to manage both parameters (regardless empty or not) in a single query
        // if((genreIds!=null && !genreIds.isEmpty()) && watched!=null) // genreIds != null, watched != null
        //     page = servieRepository.findByCompletedAndGenres(watched, genres, genres.size(), pageable);
        // else if((genreIds!=null && !genreIds.isEmpty()) && watched==null) // genreIds != null, watched == null
        //     page = servieRepository.findByGenres(genres, genres.size(), pageable);
        // else if((genreIds==null || genreIds.isEmpty()) && watched!=null) // genreIds == null, watched != null
        //     page = servieRepository.findByCompleted(watched, pageable);
        // else // genreIds == null, watched == null
        page = servieRepository.findAll(pageable);
        List<Servie> servies = page.getContent();//.stream().map((servie) -> converter.servieToDtoHomePage(servie)).collect(Collectors.toList());
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
        Servie series = servieRepository.findById(new Key(tmdbId, type)).orElseThrow(() -> new TmdbIdNotFoundException("Series", "Id", tmdbId));
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
        Servie series = servieRepository.findById(new Key(tmdbId, type)).orElseThrow(() -> new TmdbIdNotFoundException("Series", "Id", tmdbId));
        series.setPosterPath(filePath);
        servieRepository.save(series);
    }

    // Toggles the watch value of a Series (after toggling watch value of all related Episodes)
    public void toggleSeriesWatch(String type, Integer tmdbId)
    {
        if(type.equals("tv"))
        {
            Series series = seriesRepository.findById(new Key(tmdbId, type)).orElseThrow(() -> new TmdbIdNotFoundException("Series", "Id", tmdbId));
            List<Episode> episodes = episodeRepository.findByTmdbIdAndWatched(tmdbId, series.getWatched());
            for(Episode episode : episodes)
                episode.setWatched(!series.getWatched());
            series.setWatched(!series.getWatched());
            seriesRepository.save(series);
        }
        else
        {
            Movie movie = movieRepository.findById(new Key(tmdbId, type)).orElseThrow(() -> new TmdbIdNotFoundException("Series", "Id", tmdbId));
            movie.setWatched(!movie.getWatched());
            movieRepository.save(movie);
        }
    }
}
