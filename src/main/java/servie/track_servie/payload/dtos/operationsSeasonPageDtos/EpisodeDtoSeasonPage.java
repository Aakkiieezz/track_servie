package servie.track_servie.payload.dtos.operationsSeasonPageDtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EpisodeDtoSeasonPage
{
    private String name;
    private Integer episodeNumber;
    private String stillPath;
    private String overview;
    private Boolean watched;

    public EpisodeDtoSeasonPage(String name, Integer episodeNumber, String stillPath, String overview, Boolean watched)
    {
        this.name = name;
        this.episodeNumber = episodeNumber;
        this.stillPath = stillPath;
        this.overview = overview;
        this.watched = watched;
    }
}
