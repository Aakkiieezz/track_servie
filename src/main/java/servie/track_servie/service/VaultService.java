package servie.track_servie.service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import servie.track_servie.entity.Episode;
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
import servie.track_servie.payload.dtos.ServieGenreMapping;
import servie.track_servie.payload.primaryKeys.ServieKey;
import servie.track_servie.payload.primaryKeys.UserSeasonDataKey;
import servie.track_servie.payload.primaryKeys.UserServieDataKey;
import servie.track_servie.repository.EpisodeRepository;
import servie.track_servie.repository.GenreRepository;
import servie.track_servie.repository.MovieCollectionRepository;
import servie.track_servie.repository.SeasonRepository;
import servie.track_servie.repository.ServieRepository;
import servie.track_servie.repository.UserEpisodeDataRepository;
import servie.track_servie.repository.UserRepository;
import servie.track_servie.repository.UserSeasonDataRepository;
import servie.track_servie.repository.UserServieDataRepository;

@Slf4j
@Service
public class VaultService
{
    @Autowired
    private UserServieDataRepository userServieDataRepository;
    @Autowired
    private UserSeasonDataRepository userSeasonDataRepository;
    @Autowired
    private UserEpisodeDataRepository userEpisodeDataRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ServieRepository servieRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private SeasonRepository seasonRepository;
    @Autowired
    private EpisodeRepository episodeRepository;
    @Autowired
    private MovieCollectionRepository movieCollectionRepository;

    // @Scheduled(fixedRate = 10*60*1000)
    public void exportMasterData() throws IOException
    {
        exportServies();
        exportSeasons();
        exportEpisodes();
        exportMovieCollections();
        exportServieGenreMappings();
    }

    public void exportServies() throws IOException
    {
        log.info("Exporting servie data");
        List<Servie> servies = servieRepository.findAll();
        String moviesFilePath = "/home/aakkiieezz/Coding/track_servie/backups/movies.csv";
        String seriesFilePath = "/home/aakkiieezz/Coding/track_servie/backups/series.csv";
        try(CSVWriter MovieWriter = new CSVWriter(new FileWriter(moviesFilePath));CSVWriter SeriesWriter = new CSVWriter(new FileWriter(seriesFilePath)))
        {
            String[] movieHeader = {"TmdbId", "Servie Type", "ImdbId", "Backdrop Path", "Poster Path", "Title", "Overview", "Status", "Tagline", "Language", "Popularity", "Collection Id", "Collection Name", "Collection Poster Path", "Collection Backdrop Path", "Release Date", "Runtime"};
            MovieWriter.writeNext(movieHeader);
            String[] seriesHeader = {"TmdbId", "Servie Type", "ImdbId", "Backdrop Path", "Poster Path", "Title", "Overview", "Status", "Tagline", "Language", "Popularity", "Number Of Seasons", "Number Of Episodes", "First Air Date", "Last Air Date"};
            SeriesWriter.writeNext(seriesHeader);
            for(Servie servie : servies)
            {
                String tmdbId = servie.getTmdbId().toString();
                String childtype = servie.getChildtype();
                if(servie instanceof Movie)
                {
                    Movie movie = (Movie) servie;
                    String collectionId = null;
                    String collectionName = null;
                    String collectionPosterPath = null;
                    String collectionBackdropPath = null;
                    if(movie.getBelongsToCollection()!=null)
                    {
                        collectionId = movie.getBelongsToCollection().getCollectionId().toString();
                        collectionName = movie.getBelongsToCollection().getCollectionName();
                        collectionPosterPath = movie.getBelongsToCollection().getPosterPath();
                        collectionBackdropPath = movie.getBelongsToCollection().getCollectionBackdropPath();
                    }
                    String releaseDate = (movie.getReleaseDate()==null)? null : movie.getReleaseDate().toString();
                    String[] movieRow = {
                            tmdbId,
                            childtype,
                            servie.getImdbId(),
                            servie.getBackdropPath(),
                            servie.getPosterPath(),
                            servie.getTitle(),
                            servie.getOverview(),
                            servie.getStatus(),
                            servie.getTagline(),
                            servie.getOriginalLanguage(),
                            servie.getPopularity().toString(),
                            collectionId,
                            collectionName,
                            collectionPosterPath,
                            collectionBackdropPath,
                            releaseDate,
                            movie.getRuntime().toString()};
                    MovieWriter.writeNext(movieRow);
                }
                else
                {
                    Series series = (Series) servie;
                    String[] seriesRow = {
                            tmdbId,
                            childtype,
                            servie.getImdbId(),
                            servie.getBackdropPath(),
                            servie.getPosterPath(),
                            servie.getTitle(),
                            servie.getOverview(),
                            servie.getStatus(),
                            servie.getTagline(),
                            servie.getOriginalLanguage(),
                            servie.getPopularity().toString(),
                            series.getTotalSeasons().toString(),
                            series.getTotalEpisodes().toString(),
                            series.getFirstAirDate().toString(),
                            series.getLastAirDate().toString()};
                    SeriesWriter.writeNext(seriesRow);
                }
            }
        }
        log.info("    Finished exporting servie data");
    }

    private void exportSeasons() throws IOException
    {
        log.info("Exporting seasons data");
        List<Season> seasons = seasonRepository.findAll();
        String seasonsFilePath = "/home/aakkiieezz/Coding/track_servie/backups/seasons.csv";
        try(CSVWriter SeasonsWriter = new CSVWriter(new FileWriter(seasonsFilePath)))
        {
            String[] seasonHeader = {"TmdbId", "Servie Type", "Season Number", "Id", "Overview", "Name", "Episode Count", "Poster Path"};
            SeasonsWriter.writeNext(seasonHeader);
            for(Season season : seasons)
            {
                String[] seasonRow = {
                        season.getSeries().getTmdbId().toString(),
                        season.getSeries().getChildtype(),
                        season.getSeasonNo().toString(),
                        season.getId(),
                        season.getOverview(),
                        season.getName(),
                        season.getEpisodeCount().toString(),
                        season.getPosterPath()
                };
                SeasonsWriter.writeNext(seasonRow);
            }
        }
        log.info("    Finished exporting seasons data");
    }

    private void exportEpisodes() throws IOException
    {
        log.info("Exporting episodes data");
        List<Episode> episodes = episodeRepository.findAll();
        String episodesFilePath = "/home/aakkiieezz/Coding/track_servie/backups/episodes.csv";
        try(CSVWriter episodesWriter = new CSVWriter(new FileWriter(episodesFilePath)))
        {
            String[] episodesHeader = {"TmdbId", "Id", "Season Id", "Season Number", "Episode Number", "Overview", "Runtime", "Still Path", "Name"};
            episodesWriter.writeNext(episodesHeader);
            for(Episode episode : episodes)
            {
                String runtime = (episode.getRuntime()==null)? null : episode.getRuntime().toString();
                String[] episodeRow = {
                        episode.getTmdbId().toString(),
                        episode.getId(),
                        episode.getSeason().getId(),
                        episode.getSeasonNo().toString(),
                        episode.getEpisodeNo().toString(),
                        episode.getOverview(),
                        runtime,
                        episode.getStillPath(),
                        episode.getName()
                };
                episodesWriter.writeNext(episodeRow);
            }
        }
        log.info("    Finished exporting episodes data");
    }

    private void exportMovieCollections() throws IOException
    {
        log.info("Exporting collections data");
        List<MovieCollection> movieCollections = movieCollectionRepository.findAll();
        String collectionsFilePath = "/home/aakkiieezz/Coding/track_servie/backups/movie_collections.csv";
        try(CSVWriter collectionsWriter = new CSVWriter(new FileWriter(collectionsFilePath)))
        {
            String[] collectionsHeader = {"Id", "Name", "Overview", "Backdrop Path", "Poster Path"};
            collectionsWriter.writeNext(collectionsHeader);
            for(MovieCollection movieCollection : movieCollections)
            {
                String[] collectionRow = {
                        movieCollection.getId().toString(),
                        movieCollection.getName(),
                        movieCollection.getOverview(),
                        movieCollection.getBackdropPath(),
                        movieCollection.getPosterPath()
                };
                collectionsWriter.writeNext(collectionRow);
            }
        }
        log.info("    Finished exporting collections data");
    }

    private void exportServieGenreMappings() throws IOException
    {
        log.info("Exporting servie_genre mappings");
        List<ServieGenreMapping> servieGenreMappings = genreRepository.getGenreMappings();
        String servieGenresFilePath = "/home/aakkiieezz/Coding/track_servie/backups/servie_genres.csv";
        try(CSVWriter ServieGenresWriter = new CSVWriter(new FileWriter(servieGenresFilePath)))
        {
            String[] servieGenreHeader = {"Genre Id", "Servie Tmdb Id", "Servie Type"};
            ServieGenresWriter.writeNext(servieGenreHeader);
            for(ServieGenreMapping servieGenre : servieGenreMappings)
            {
                String[] servieGenreRow = {
                        servieGenre.getGenreId().toString(),
                        servieGenre.getTmdbId().toString(),
                        servieGenre.getChildtype()
                };
                ServieGenresWriter.writeNext(servieGenreRow);
            }
        }
        log.info("    Finished exporting servie_genre mappings");
    }

    // @Scheduled(fixedRate = 10*60*1000)
    public void exportUserRawData() throws IOException
    {
        Integer userId = 1;
        log.info("Exporting data of user id {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
        List<UserServieData> userServieDatas = userServieDataRepository.findAllByUser(user);
        String serviesFilePath = "/home/aakkiieezz/Coding/track_servie/user_backups/watched_servies.csv";
        String SeasonsFilePath = "/home/aakkiieezz/Coding/track_servie/user_backups/watched_seasons.csv";
        String episodesFilePath = "/home/aakkiieezz/Coding/track_servie/user_backups/watched_episodes.csv";
        try(CSVWriter ServieWriter = new CSVWriter(new FileWriter(serviesFilePath));
                CSVWriter SeasonWriter = new CSVWriter(new FileWriter(SeasonsFilePath));
                CSVWriter EpisodeWriter = new CSVWriter(new FileWriter(episodesFilePath)))
        {
            String[] servieHeader = {"TmdbId", "Servie Type", "Movie Watched", "Backdrop Path", "Poster Path", "Notes"};
            ServieWriter.writeNext(servieHeader);
            String[] seasonHeader = {"TmdbId", "Servie Type", "Season Number", "Poster Path", "Notes"};
            SeasonWriter.writeNext(seasonHeader);
            String[] episodeHeader = {"TmdbId", "Servie Type", "Season Number", "Episode Number", "Watched", "Notes"};
            EpisodeWriter.writeNext(episodeHeader);
            for(UserServieData userServieData : userServieDatas)
            {
                String tmdbId = userServieData.getServie().getTmdbId().toString();
                String childtype = userServieData.getServie().getChildtype();
                if(userServieData.getServie() instanceof Series)
                {
                    List<UserSeasonData> userSeasonDatas = userServieData.getSeasons();
                    for(UserSeasonData userSeasonData : userSeasonDatas)
                    {
                        String seasonNo = userSeasonData.getSeasonNo().toString();
                        List<UserEpisodeData> userEpisodeDatas = userSeasonData.getEpisodes();
                        for(UserEpisodeData userEpisodeData : userEpisodeDatas)
                        {
                            String[] episodeRow = {tmdbId, childtype, seasonNo, userEpisodeData.getEpisodeNo().toString(), Boolean.toString(userEpisodeData.getWatched()), userEpisodeData.getNotes()};
                            EpisodeWriter.writeNext(episodeRow);
                        }
                        String[] seasonRow = {tmdbId, childtype, seasonNo, userSeasonData.getPosterPath(), userSeasonData.getNotes()};
                        SeasonWriter.writeNext(seasonRow);
                    }
                }
                String[] servieRow = {tmdbId, childtype, Boolean.toString(userServieData.getMovieWatched()), userServieData.getBackdropPath(), userServieData.getPosterPath(), userServieData.getNotes()};
                ServieWriter.writeNext(servieRow);
            }
        }
        log.info("    Finished exporting data of user id {}", userId);
    }
    // APPROACH - save the entities seperately
    // public void importer() throws IOException, CsvException
    // {
    //     Integer userId = 1;
    //     User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
    //     String serviesFilePath = "/home/aakkiieezz/Coding/track_servie/user_backups/watched_movies.csv";
    //     CSVReader ServieCsvReader = new CSVReader(new FileReader(serviesFilePath));
    //     ServieCsvReader.skip(1);
    //     List<String[]> ServieCsvRows = ServieCsvReader.readAll();
    //     String SeasonsFilePath = "/home/aakkiieezz/Coding/track_servie/user_backups/watched_seasons.csv";
    //     CSVReader SeasonCsvReader = new CSVReader(new FileReader(SeasonsFilePath));
    //     SeasonCsvReader.skip(1);
    //     List<String[]> SeasonCsvRows = SeasonCsvReader.readAll();
    //     String EpisodesFilePath = "/home/aakkiieezz/Coding/track_servie/user_backups/watched_episodes.csv";
    //     CSVReader EpisodeCsvReader = new CSVReader(new FileReader(EpisodesFilePath));
    //     EpisodeCsvReader.skip(1);
    //     List<String[]> EpisodeCsvRows = EpisodeCsvReader.readAll();
    //     int serviePointer = 0, seasonPointer = 0, episodePointer = 0;
    //     List<UserServieData> userServieDatas = new ArrayList<>();
    //     for(String[] servieCsvRow : ServieCsvRows)
    //     {
    //         Integer tmdbId = Integer.parseInt(servieCsvRow[0]);
    //         String childtype = servieCsvRow[1];
    //         Servie servie = servieRepository.findById(new ServieKey(childtype, tmdbId)).get();
    //         UserServieData userServieData = new UserServieData();
    //         userServieData.setUser(user);
    //         userServieData.setServie(servie);
    //         userServieDatas.add(userServieData);
    //     }
    //     userServieDataRepository.saveAll(userServieDatas);
    //     // TO DO
    // }

    // @Scheduled(fixedRate = 600000)
    // NOT SURE OF THIS STYLE
    public void importAllUserRawData() throws IOException, CsvException
    {
        Integer userId = 1;
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
        String serviesFilePath = "/home/aakkiieezz/Coding/track_servie/user_backups/watched_movies.csv";
        CSVReader reader = new CSVReader(new FileReader(serviesFilePath));
        reader.skip(1);
        List<String[]> rows = reader.readAll();
        List<UserServieData> userServieDatas = new ArrayList<>();
        for(String[] row : rows)
        {
            Integer tmdbId = Integer.parseInt(row[0]);
            String childtype = row[1];
            Servie servie = servieRepository.findById(new ServieKey(childtype, tmdbId)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey(childtype, tmdbId).toString()));
            UserServieData userServieData = new UserServieData();
            userServieData.setServie(servie);
            userServieData.setUser(user);
            userServieDatas.add(userServieData);
        }
        userServieDataRepository.saveAll(userServieDatas);
        String SeasonsFilePath = "/home/aakkiieezz/Coding/track_servie/user_backups/watched_seasons.csv";
        reader = new CSVReader(new FileReader(SeasonsFilePath));
        reader.skip(1);
        rows = reader.readAll();
        List<UserSeasonData> userSeasonDatas = new ArrayList<>();
        for(String[] row : rows)
        {
            Integer tmdbId = Integer.parseInt(row[0]);
            String childtype = row[1];
            Servie servie = servieRepository.findById(new ServieKey(childtype, tmdbId)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey(childtype, tmdbId).toString()));
            UserServieData userServieData = userServieDataRepository.findById(new UserServieDataKey(user, servie)).orElseThrow(() -> new ResourceNotFoundException("UserServieData", "UserServieDataKey", new UserServieDataKey(user, servie).toString()));
            UserSeasonData userSeasonData = new UserSeasonData();
            userSeasonData.setUserServieData(userServieData);
            userSeasonData.setSeasonNo(Integer.parseInt(row[2]));
            // userSeasonData.setEpisodeCount(Integer.parseInt(row[3]));
            userSeasonDatas.add(userSeasonData);
        }
        userSeasonDataRepository.saveAll(userSeasonDatas);
        String EpisodesFilePath = "/home/aakkiieezz/Coding/track_servie/user_backups/watched_episodes.csv";
        reader = new CSVReader(new FileReader(EpisodesFilePath));
        reader.skip(1);
        rows = reader.readAll();
        List<UserEpisodeData> userEpisodeDatas = new ArrayList<>();
        for(String[] row : rows)
        {
            Integer tmdbId = Integer.parseInt(row[0]);
            String childtype = row[1];
            Servie servie = servieRepository.findById(new ServieKey(childtype, tmdbId)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey(childtype, tmdbId).toString()));
            Integer seasonNo = Integer.parseInt(row[2]);
            Integer episodeNo = Integer.parseInt(row[3]);
            Boolean watched = Boolean.valueOf(row[4]);
            UserServieData userServieData = userServieDataRepository.findById(new UserServieDataKey(user, servie)).orElseThrow(() -> new ResourceNotFoundException("UserServieData", "UserServieDataKey", new UserServieDataKey(user, servie).toString()));
            UserSeasonData userSeasonData = userSeasonDataRepository.findById(new UserSeasonDataKey(userServieData, seasonNo)).orElseThrow(() -> new ResourceNotFoundException("UserServieData", "UserSeasonDataKey", new UserSeasonDataKey(userServieData, seasonNo).toString()));
            UserEpisodeData userEpisodeData = new UserEpisodeData();
            userEpisodeData.setUserSeasonData(userSeasonData);
            userEpisodeData.setEpisodeNo(episodeNo);
            userEpisodeData.setWatched(watched);
            userEpisodeDatas.add(userEpisodeData);
        }
        userEpisodeDataRepository.saveAll(userEpisodeDatas);
    }
    // public void importUserServieData() throws IOException, CsvException
    // {
    //     Integer userId = 1;
    //     User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
    //     String filePath = "/home/aakkiieezz/Coding/track_servie/tmdb exports/watched_movies.csv";
    //     CSVReader reader = new CSVReader(new FileReader(filePath));
    //     reader.skip(1);
    //     List<String[]> rows = reader.readAll();
    //     List<UserServieData> userServieDatas = new ArrayList<>();
    //     for(String[] row : rows)
    //     {
    //         Integer tmdbId = Integer.parseInt(row[0]);
    //         Servie servie = servieRepository.findById(new ServieKey(ServieType.MOVIE.toString(), tmdbId)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey(ServieType.MOVIE.toString(), tmdbId).toString()));
    //         UserServieData userServieData = new UserServieData();
    //         userServieData.setServie(servie);
    //         userServieData.setUser(user);
    //         userServieDatas.add(userServieData);
    //     }
    //     userServieDataRepository.saveAll(userServieDatas);
    // }
}