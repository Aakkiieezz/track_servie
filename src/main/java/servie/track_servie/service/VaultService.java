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
import servie.track_servie.entity.Series;
import servie.track_servie.entity.Servie;
import servie.track_servie.entity.User;
import servie.track_servie.entity.UserEpisodeData;
import servie.track_servie.entity.UserSeasonData;
import servie.track_servie.entity.UserServieData;
import servie.track_servie.entity.vault.VaultUserServie;
import servie.track_servie.enums.ServieType;
import servie.track_servie.exceptions.ResourceNotFoundException;
import servie.track_servie.payload.primaryKeys.ServieKey;
import servie.track_servie.payload.primaryKeys.UserSeasonDataKey;
import servie.track_servie.payload.primaryKeys.UserServieDataKey;
import servie.track_servie.repository.ServieRepository;
import servie.track_servie.repository.UserEpisodeDataRepository;
import servie.track_servie.repository.UserRepository;
import servie.track_servie.repository.UserSeasonDataRepository;
import servie.track_servie.repository.UserServieDataRepository;

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

    // @Scheduled(fixedRate = 600000)
    public void exportAllUserRawData() throws IOException
    {
        Integer userId = 1;
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
        List<UserServieData> userServieDatas = userServieDataRepository.findAllByUser(user);
        // List<VaultUserServie> vaultUserServies = userServieDataRepository.getUserMovies(user);
        String serviesFilePath = "/home/aakkiieezz/Coding/track_servie/tmdb exports/watched_movies.csv";
        // String SeriesFilePath = "/home/aakkiieezz/Coding/track_servie/tmdb exports/watched_series.csv";
        String SeasonsFilePath = "/home/aakkiieezz/Coding/track_servie/tmdb exports/watched_seasons.csv";
        String EpisodesFilePath = "/home/aakkiieezz/Coding/track_servie/tmdb exports/watched_episodes.csv";
        try(CSVWriter ServieWriter = new CSVWriter(new FileWriter(serviesFilePath));
                // CSVWriter SeriesWriter = new CSVWriter(new FileWriter(serviesFilePath));
                CSVWriter SeasonWriter = new CSVWriter(new FileWriter(SeasonsFilePath));
                CSVWriter EpisodeWriter = new CSVWriter(new FileWriter(EpisodesFilePath)))
        {
            String[] servieHeader = {"TmdbId", "ChildType", "Movie Watched", "Backdrop Path", "Poster Path"};
            ServieWriter.writeNext(servieHeader);
            String[] seasonHeader = {"TmdbId", "ChildType", "Season Number"};
            SeasonWriter.writeNext(seasonHeader);
            String[] episodeHeader = {"TmdbId", "ChildType", "Season Number", "Episode Number", "Watched"};
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
                        String seasonNumber = userSeasonData.getSeasonNumber().toString();
                        List<UserEpisodeData> userEpisodeDatas = userSeasonData.getEpisodes();
                        for(UserEpisodeData userEpisodeData : userEpisodeDatas)
                        {
                            String[] episodeRow = {tmdbId, childtype, seasonNumber, userEpisodeData.getEpisodeNumber().toString(), Boolean.toString(userEpisodeData.getWatched())};
                            EpisodeWriter.writeNext(episodeRow);
                        }
                        String[] seasonRow = {tmdbId, childtype, seasonNumber};
                        SeasonWriter.writeNext(seasonRow);
                    }
                }
                String[] servieRow = {tmdbId, childtype, Boolean.toString(userServieData.getMovieWatched()), userServieData.getBackdropPath(), userServieData.getPosterPath()};
                ServieWriter.writeNext(servieRow);
            }
        }
    }

    // @Scheduled(fixedRate = 600000)
    public void exportUserMovieData() throws IOException
    {
        Integer userId = 1;
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
        List<VaultUserServie> vaultUserServies = userServieDataRepository.getUserMovies(user);
        String filePath = "/home/aakkiieezz/Coding/track_servie/tmdb exports/watched_movies.csv";
        try(CSVWriter writer = new CSVWriter(new FileWriter(filePath)))
        {
            String[] header = {"Id", "Name", "Release Date"};
            writer.writeNext(header);
            for(VaultUserServie vaultUserServie : vaultUserServies)
            {
                String[] row = {vaultUserServie.getTmdbId().toString(), vaultUserServie.getTitle()};
                writer.writeNext(row);
            }
        }
    }
    // public void exportUserSeriesData() throws IOException
    // {
    //     Integer userId = 1;
    //     User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
    //     List<UserServieData> 
    //     List<VaultUserServie> vaultUserServies = userServieDataRepository.getUserSeries(user);
    //     String filePath = "/home/aakkiieezz/Coding/track_servie/tmdb exports/watched_movies.csv";
    //     try(CSVWriter writer = new CSVWriter(new FileWriter(filePath)))
    //     {
    //         String[] header = {"Id", "Name", "Release Date"};
    //         writer.writeNext(header);
    //         for(VaultUserServie vaultUserServie : vaultUserServies)
    //         {
    //             String[] row = {vaultUserServie.getTmdbId().toString(), vaultUserServie.getTitle()};
    //             writer.writeNext(row);
    //         }
    //     }
    // }

    public void importUserServieData() throws IOException, CsvException
    {
        Integer userId = 1;
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
        String filePath = "/home/aakkiieezz/Coding/track_servie/tmdb exports/watched_movies.csv";
        CSVReader reader = new CSVReader(new FileReader(filePath));
        reader.skip(1);
        List<String[]> rows = reader.readAll();
        List<UserServieData> userServieDatas = new ArrayList<>();
        for(String[] row : rows)
        {
            Integer tmdbId = Integer.parseInt(row[0]);
            Servie servie = servieRepository.findById(new ServieKey(ServieType.MOVIE.toString(), tmdbId)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey(ServieType.MOVIE.toString(), tmdbId).toString()));
            UserServieData userServieData = new UserServieData();
            userServieData.setServie(servie);
            userServieData.setUser(user);
            userServieDatas.add(userServieData);
        }
        userServieDataRepository.saveAll(userServieDatas);
    }

    // @Scheduled(fixedRate = 600000)
    public void importAllUserRawData() throws IOException, CsvException
    {
        Integer userId = 1;
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
        // String SeriesFilePath = "/home/aakkiieezz/Coding/track_servie/tmdb exports/watched_series.csv";
        String serviesFilePath = "/home/aakkiieezz/Coding/track_servie/tmdb exports/watched_movies.csv";
        CSVReader reader = new CSVReader(new FileReader(serviesFilePath));
        reader.skip(1);
        List<String[]> rows = reader.readAll();
        List<UserServieData> userServieDatas = new ArrayList<>();
        for(String[] row : rows)
        {
            Integer tmdbId = Integer.parseInt(row[0]);
            String childtype = row[1];
            Servie servie = servieRepository.findById(new ServieKey(childtype, tmdbId)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey(ServieType.MOVIE.toString(), tmdbId).toString()));
            UserServieData userServieData = new UserServieData();
            userServieData.setServie(servie);
            userServieData.setUser(user);
            userServieDatas.add(userServieData);
        }
        userServieDataRepository.saveAll(userServieDatas);
        String SeasonsFilePath = "/home/aakkiieezz/Coding/track_servie/tmdb exports/watched_seasons.csv";
        reader = new CSVReader(new FileReader(SeasonsFilePath));
        reader.skip(1);
        rows = reader.readAll();
        List<UserSeasonData> userSeasonDatas = new ArrayList<>();
        for(String[] row : rows)
        {
            Integer tmdbId = Integer.parseInt(row[0]);
            String childtype = row[1];
            Servie servie = servieRepository.findById(new ServieKey(childtype, tmdbId)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey(ServieType.MOVIE.toString(), tmdbId).toString()));
            UserServieData userServieData = userServieDataRepository.findById(new UserServieDataKey(user, servie)).orElseThrow(() -> new ResourceNotFoundException("UserServieData", "UserServieDataKey", new UserServieDataKey(user, servie).toString()));
            UserSeasonData userSeasonData = new UserSeasonData();
            userSeasonData.setUserServieData(userServieData);
            userSeasonData.setSeasonNumber(Integer.parseInt(row[2]));
            // userSeasonData.setEpisodeCount(Integer.parseInt(row[3]));
            userSeasonDatas.add(userSeasonData);
        }
        userSeasonDataRepository.saveAll(userSeasonDatas);
        String EpisodesFilePath = "/home/aakkiieezz/Coding/track_servie/tmdb exports/watched_episodes.csv";
        reader = new CSVReader(new FileReader(EpisodesFilePath));
        reader.skip(1);
        rows = reader.readAll();
        List<UserEpisodeData> userEpisodeDatas = new ArrayList<>();
        for(String[] row : rows)
        {
            Integer tmdbId = Integer.parseInt(row[0]);
            String childtype = row[1];
            Integer seasonNumber = Integer.parseInt(row[2]);
            Integer episodeNumber = Integer.parseInt(row[3]);
            Boolean watched = Boolean.valueOf(row[4]);
            Servie servie = servieRepository.findById(new ServieKey(childtype, tmdbId)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey(ServieType.MOVIE.toString(), tmdbId).toString()));
            UserServieData userServieData = userServieDataRepository.findById(new UserServieDataKey(user, servie)).orElseThrow(() -> new ResourceNotFoundException("UserServieData", "UserServieDataKey", new UserServieDataKey(user, servie).toString()));
            UserSeasonData userSeasonData = userSeasonDataRepository.findById(new UserSeasonDataKey(userServieData, seasonNumber)).orElseThrow(() -> new ResourceNotFoundException("UserServieData", "UserSeasonDataKey", new UserSeasonDataKey(userServieData, seasonNumber).toString()));
            UserEpisodeData userEpisodeData = new UserEpisodeData();
            userEpisodeData.setUserSeasonData(userSeasonData);
            userEpisodeData.setEpisodeNumber(episodeNumber);
            userEpisodeData.setWatched(watched);
            userEpisodeDatas.add(userEpisodeData);
        }
        userEpisodeDataRepository.saveAll(userEpisodeDatas);
    }
}