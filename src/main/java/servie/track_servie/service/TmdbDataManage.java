package servie.track_servie.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import servie.track_servie.entity.tmdbExports.TmdbMovieExports;
import servie.track_servie.repository.tmdbExports.TmdbMovieExportsRepository;
import servie.track_servie.utils.FileManager;

@Service
@Slf4j
public class TmdbDataManage
{
    @Autowired
    private FileManager fileDownloader;
    @Autowired
    private TmdbMovieExportsRepository tmdbMovieExportsRepository;

    // @Scheduled(fixedRate = 6000000)
    // @Scheduled(cron = "0 46 14 ? * SUN", zone = "UTC")
    public void manageExports()
    {
        // downloadExports();
        // extractExports();
        readJsonFile();
    }

    public void downloadExports()
    {
        try
        {
            fileDownloader.downloadFile("http://files.tmdb.org/p/exports/movie_ids_05_25_2023.json.gz", "/home/aakkiieezz/Coding/track_servie/tmdb exports/movie_ids.json.gz");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void extractExports()
    {
        fileDownloader.extractGzFile("/home/aakkiieezz/Coding/track_servie/tmdb exports/movie_ids.json.gz", "/home/aakkiieezz/Coding/track_servie/tmdb exports/movie_ids.json");
    }

    public void readJsonFile()
    {
        String filePath = "/home/aakkiieezz/Coding/track_servie/tmdb exports/movie_ids.json";
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();
            String line;
            List<TmdbMovieExports> tmdbMovieExportsList = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            int totalCounter = 0;
            while((line = reader.readLine())!=null)
            {
                TmdbMovieExports object = objectMapper.readValue(line, TmdbMovieExports.class);
                tmdbMovieExportsList.add(object);
                totalCounter++;
                if(totalCounter%1000==0)
                {
                    tmdbMovieExportsRepository.saveAll(tmdbMovieExportsList);
                    log.info("{} records done", totalCounter);
                    tmdbMovieExportsList.clear();
                }
                // if(totalCounter==100000)
                //     break;
            }
            reader.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
