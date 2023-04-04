package servie.track_servie.dto.operationsSearch.SeriesPageDtos;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SeriesDtoSearchSeriesPage
{
    private Integer tmdbId;
    private String name;
    @JsonProperty("backdrop_path")
    private String backdropPath;
    private List<SeasonDtoSearchSeriesPage> seasons;
}
