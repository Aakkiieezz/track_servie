package servie.track_servie.dto.operationsSeriesPageDtos;

import java.util.List;
import java.util.Set;
import lombok.Data;

@Data
public class SeriesDtoSeriesPage
{
    private Integer tmdbId;
    private String name;
    // private String originalName;
    private Integer numberOfSeasons;
    private Integer numberOfEpisodes;
    private Integer episodesWatched;
    private Boolean watched;
    // private String firstAirDate;
    // private String lastAirDate;
    // private String status;
    // private String tagline;
    private String overview;
    // private String posterPath;
    private String backdropPath;
    private List<SeasonDtoSeriesPage> seasons;
    private Set<GenreDtoSeriesPage> genres;
}