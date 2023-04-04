package servie.track_servie.dto.operationsSeasonPageDtos;

import lombok.Data;

@Data
public class EpisodeDtoSeasonPage
{
    private String id;
    private String name;
    private Integer tmdbId;
    private Integer seasonNumber;
    private Integer episodeNumber;
    private String stillPath;
    private String overview;
    private Boolean watched;
}
