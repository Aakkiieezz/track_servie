package servie.track_servie.dto.operationsHomePageDtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SeriesDtoHomePage extends ServieDtoHomePage
{
    private Integer numberOfEpisodes;
    private Integer episodesWatched;
}
