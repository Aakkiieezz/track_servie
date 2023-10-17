package servie.track_servie.payload.dtos.operationsServiePageDtos;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.Data;

@Data
public class ServieDtoServiePage
{
    // servie fields
    // private String imdbId;
    private Integer tmdbId;
    private String childtype;
    private String title;
    private String status;
    private String tagline;
    private String overview;
    private String backdropPath;
    private Set<GenreDtoServiePage> genres;
    // 
    // movie fields
    private LocalDate releaseDate;
    private Integer runtime;
    // 
    // movie collection
    // private Integer collectionId;
    // private String collectionName;
    // private String posterPath;
    // private String collectionBackdropPath;
    // 
    // series fields
    private Integer totalSeasons;
    private Integer totalEpisodes;
    // private LocalDate firstAirDate;
    // private LocalDate lastAirDate;
    private List<SeasonDtoServiePage> seasons;
    // 
    // userServieData fields
    private Integer episodesWatched;
    private Boolean completed;

    public ServieDtoServiePage(Integer tmdbId, String childtype, String title, String status, String tagline, String overview, String backdropPath, LocalDate releaseDate, Integer runtime, Integer totalSeasons, Integer totalEpisodes, /* LocalDate firstAirDate, LocalDate lastAirDate, */ Integer episodesWatched, Boolean completed)
    {
        this.tmdbId = tmdbId;
        this.childtype = childtype;
        this.title = title;
        this.status = status;
        this.tagline = tagline;
        this.overview = overview;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.totalSeasons = totalSeasons;
        this.totalEpisodes = totalEpisodes;
        // this.firstAirDate = firstAirDate;
        // this.lastAirDate = lastAirDate;
        this.episodesWatched = episodesWatched;
        this.completed = completed;
    }
}