package servie.track_servie.payload.dtos.operationsSearch.SearchPageDtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SeriesDtoSearchPage
{
    @JsonProperty("id")
    private Integer tmdbId;
    // ---------------------------------------------------------------
    @JsonAlias({"title", "name"})
    private String title;
    // ---------------------------------------------------------------
    @JsonProperty("poster_path")
    private String posterPath;
    // ---------------------------------------------------------------
    // extra
    private Boolean found;
    private String childtype;
    private Integer numberOfEpisodes;
    private Integer episodesWatched;
    private Boolean completed;
    // public SeriesDtoSearchPage(Integer tmdbId, String title, String posterPath, String childtype, Integer numberOfEpisodes, Integer episodesWatched, Boolean completed)
    // {
    //     this.tmdbId = tmdbId;
    //     this.title = title;
    //     this.posterPath = posterPath;
    //     this.childtype = childtype;
    //     this.numberOfEpisodes = numberOfEpisodes;
    //     this.episodesWatched = episodesWatched;
    //     this.completed = completed;
    // }
}
//   "first_air_date": "2011-04-17",