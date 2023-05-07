package servie.track_servie.payload.dtos.operationsHomePageDtos;

import lombok.Data;

@Data
public class ServieDtoHomePage
{
    private String imdbId;
    private Integer tmdbId;
    private String childtype;
    private String title;
    private String posterPath;
    // 
    // movie fields
    private String releaseDate;
    // 
    // series fields
    private Integer numberOfEpisodes;
    private String firstAirDate;
    private String lastAirDate;
    // 
    // userServieData fields
    private Boolean movieWatched;
    private Integer episodesWatched;
    private Boolean completed;

    public ServieDtoHomePage(String imdbId, Integer tmdbId, String childtype, String title, String posterPath, String releaseDate, Integer numberOfEpisodes, String firstAirDate, String lastAirDate, Boolean movieWatched, Integer episodesWatched, Boolean completed)
    {
        this.imdbId = imdbId;
        this.tmdbId = tmdbId;
        this.childtype = childtype;
        this.title = title;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.numberOfEpisodes = numberOfEpisodes;
        this.firstAirDate = firstAirDate;
        this.lastAirDate = lastAirDate;
        this.movieWatched = movieWatched;
        this.episodesWatched = episodesWatched;
        this.completed = completed;
    }
}