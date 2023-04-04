package servie.track_servie.dto.operationsSearch.SearchPageDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
// import java.util.List;
// import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SeriesDtoSearchPage
{
    private Integer id;
    private String name;
    // @JsonProperty("backdrop_path")
    // private String backdropPath;
    @JsonProperty("poster_path")
    private String posterPath;
    // private List<SeasonDtoSearchPage> seasons;
    // private String overview;
    private Integer tmdbId;
}
