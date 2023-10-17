package servie.track_servie.payload.dtos.operationsEpisodePageDtos;

import lombok.Data;

@Data
public class EpisodeDtoEpisodePage
{
    private String id;
    private String name;
    private Boolean watched;
    private Integer episodeNo;
    private Integer seasonNo;
    private String overview;
    private String stillPath;
    private Integer tmdbId;
}
