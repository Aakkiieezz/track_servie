package servie.track_servie.payload.dtos.operationsSearch.SeasonPageDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EpisodeDtoSearchSeasonPage
{
    private String id;
    private String name;
    private Integer tmdbId;
    @JsonProperty("season_number")
    private Integer seasonNo;
    @JsonProperty("episode_number")
    private Integer episodeNo;
    @JsonProperty("still_path")
    private String stillPath;
}
