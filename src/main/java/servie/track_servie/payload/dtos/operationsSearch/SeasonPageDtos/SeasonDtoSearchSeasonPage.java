package servie.track_servie.payload.dtos.operationsSearch.SeasonPageDtos;

import java.util.List;
import lombok.Data;

@Data
public class SeasonDtoSearchSeasonPage
{
    // private String id;
    private String name;
    private Integer tmdbId;
    // private Integer episodeCount;
    // private Integer episodesWatched;
    // private Boolean watched = false;
    // private String overview;
    // private String posterPath;
    // private Integer seasonNumber;
    List<EpisodeDtoSearchSeasonPage> episodes;
}
