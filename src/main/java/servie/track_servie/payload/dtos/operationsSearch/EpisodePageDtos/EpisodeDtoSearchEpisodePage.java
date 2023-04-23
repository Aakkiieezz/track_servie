package servie.track_servie.payload.dtos.operationsSearch.EpisodePageDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EpisodeDtoSearchEpisodePage
{
    private String id;
    private String name;
    private String overview;
    @JsonProperty("still_path")
    private String stillPath;
    private Integer tmdbId;
}
