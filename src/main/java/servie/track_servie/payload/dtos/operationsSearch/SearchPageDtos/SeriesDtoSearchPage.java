package servie.track_servie.payload.dtos.operationsSearch.SearchPageDtos;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
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
    private Boolean found;
    private String childtype;
    private Integer totalEpisodes;
    private Integer episodesWatched;
    private Boolean completed;
    @JsonAlias({"release_date", "first_air_date"})
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate releaseDate;
    @JsonProperty("original_language")
    private String language;
    // public SeriesDtoSearchPage(Integer tmdbId, String title, String posterPath, String childtype, Integer totalEpisodes, Integer episodesWatched, Boolean completed)
    // {
    //     this.tmdbId = tmdbId;
    //     this.title = title;
    //     this.posterPath = posterPath;
    //     this.childtype = childtype;
    //     this.totalEpisodes = totalEpisodes;
    //     this.episodesWatched = episodesWatched;
    //     this.completed = completed;
    // }
}
//   "first_air_date": "2011-04-17",