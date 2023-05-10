package servie.track_servie.payload.dtos.operationsSeriesPageDtos;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.Data;
import servie.track_servie.entities.MovieCollection;
import servie.track_servie.entities.MovieCollectionInfo;
import servie.track_servie.entities.Season;

@Data
public class SeriesDtoSeriesPage
{
    private String imdbId;
    private Integer tmdbId;
    private String childtype;
    private String title;
    private String tagline;
    private String overview;
    private String backdropPath;
    private Set<GenreDtoSeriesPage> genres;
    // 
    // movie fields
    private LocalDate releaseDate;
    private MovieCollectionInfo belongsToCollection;
    private MovieCollection collection;
    // 
    // series fields
    private Integer numberOfSeasons;
    private Integer numberOfEpisodes;
    private List<Season> seasons;
    // 
    // userServieData fields
    private Integer episodesWatched;
    private Boolean completed;

    public SeriesDtoSeriesPage(String imdbId, Integer tmdbId, String childtype, String title, String tagline, String overview, String backdropPath, LocalDate releaseDate, Integer numberOfEpisodes, Integer episodesWatched, Boolean completed)
    {
        this.imdbId = imdbId;
        this.tmdbId = tmdbId;
        this.childtype = childtype;
        this.title = title;
        this.tagline = tagline;
        this.overview = overview;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
        // this.genres = genres;
        // this.belongsToCollection = belongsToCollection;
        // this.collection = collection;
        this.numberOfEpisodes = numberOfEpisodes;
        // this.seasons = seasons;
        this.episodesWatched = episodesWatched;
        this.completed = completed;
    }
}