package servie.track_servie.payload.dtos.operationsSeasonPageDtos;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class SeasonDtoSeasonPage
{
	private String id;
	private String name;
	private Integer tmdbId;
	private Integer episodeCount;
	private Integer episodesWatched;
	private Boolean watched;
	private String overview;
	private String posterPath;
	private Integer seasonNo;
	private LocalDateTime lastModified;
	private List<EpisodeDtoSeasonPage> episodes;
}
