package servie.track_servie.payload.dtos.operationsSeriesPageDtos;

import lombok.Data;

@Data
public class SeasonDtoSeriesPage
{
    private String id;
    private String name;
    private Integer tmdbId;
    private Integer seasonNumber;
    private Boolean watched;
    private String posterPath;
    private Integer episodeCount;
    private Integer episodesWatched;
}
