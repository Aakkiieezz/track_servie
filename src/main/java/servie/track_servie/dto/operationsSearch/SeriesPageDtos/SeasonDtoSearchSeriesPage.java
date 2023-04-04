package servie.track_servie.dto.operationsSearch.SeriesPageDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SeasonDtoSearchSeriesPage
{
    private String id;
    private String name;
    private Integer tmdbId;
    @JsonProperty("season_number")
    private Integer seasonNumber;
    @JsonProperty("poster_path")
    private String posterPath;
}
