package servie.track_servie.payload.dtos.operationsHomePageDtos;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ServieDtoHomePage2
{
    private String imdbId;
    private Integer tmdbId;
    private String childtype;
    private String title;
    private LocalDate releaseDate;
    private Integer totalEpisodes;
    private LocalDate firstAirDate;
    private LocalDate lastAirDate;

    public ServieDtoHomePage2(String imdbId, Integer tmdbId, String childtype, String title, LocalDate releaseDate, Integer totalEpisodes, LocalDate firstAirDate, LocalDate lastAirDate)
    {
        this.imdbId = imdbId;
        this.tmdbId = tmdbId;
        this.childtype = childtype;
        this.title = title;
        this.releaseDate = releaseDate;
        this.totalEpisodes = totalEpisodes;
        this.firstAirDate = firstAirDate;
        this.lastAirDate = lastAirDate;
    }
}