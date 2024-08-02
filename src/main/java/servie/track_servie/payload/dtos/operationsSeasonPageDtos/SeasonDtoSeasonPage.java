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
	private int episodeCount;
	private int episodesWatched;
	private boolean watched;
	private String overview;
	private String posterPath;
	private int seasonNo;
	private int totalRuntime;
	private int watchedRuntime;
	private LocalDateTime lastModified;
	private List<EpisodeDtoSeasonPage> episodes;
}
