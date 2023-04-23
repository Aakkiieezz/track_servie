package servie.track_servie.payload.dtos.operationsHomePageDtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SeriesDtoHomePage extends ServieDtoHomePage
{
    private Integer numberOfEpisodes;
    private Integer episodesWatched;
}
