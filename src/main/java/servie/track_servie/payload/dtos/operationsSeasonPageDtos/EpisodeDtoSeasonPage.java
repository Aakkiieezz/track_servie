package servie.track_servie.payload.dtos.operationsSeasonPageDtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
	private LocalDateTime LastModified;
	private LocalDate airDate;
	private Boolean watched;

	public EpisodeDtoSeasonPage(String name, Integer episodeNo, String stillPath, String overview, Integer runtime, LocalDateTime LastModified, LocalDate airDate, Boolean watched)
	{
		this.name = name;
		this.episodeNo = episodeNo;
		this.stillPath = stillPath;
		this.overview = overview;
		this.runtime = runtime;
		this.LastModified = LastModified;
		this.airDate = airDate;
		this.watched = watched;
	}
}
