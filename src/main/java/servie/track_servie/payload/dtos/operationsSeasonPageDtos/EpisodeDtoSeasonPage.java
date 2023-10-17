package servie.track_servie.payload.dtos.operationsSeasonPageDtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EpisodeDtoSeasonPage
{
    private String name;
    private Integer episodeNo;
    private String stillPath;
    private String overview;
    private Integer runtime;
    private Boolean watched;

    public EpisodeDtoSeasonPage(String name, Integer episodeNo, String stillPath, String overview, Integer runtime, Boolean watched)
    {
        this.name = name;
        this.episodeNo = episodeNo;
        this.stillPath = stillPath;
        this.overview = overview;
        this.runtime = runtime;
        this.watched = watched;
    }
}
