package servie.track_servie.payload.dtos.operationsServiePageDtos;

import lombok.Data;

@Data
public class SeasonDtoServiePage
{
    // season
    private String id;
    private String name;
    private Integer seasonNo;
    private String posterPath;
    private Integer episodeCount;
    // UserSeasonData fields
    private Integer episodesWatched;
    private Boolean watched;

    public SeasonDtoServiePage(String id, String name, Integer seasonNo, String posterPath, Integer episodeCount, Integer episodesWatched, Boolean watched)
    {
        this.id = id;
        this.name = name;
        this.seasonNo = seasonNo;
        this.posterPath = posterPath;
        this.episodeCount = episodeCount;
        this.episodesWatched = episodesWatched;
        this.watched = watched;
    }
}
