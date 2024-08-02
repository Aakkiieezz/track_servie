package servie.track_servie.payload.dtos.operationsHomePageDtos;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ServieDtoHomePage
{
	// servie fields
	private String imdbId;
	private Integer tmdbId;
	private String childtype;
	private String title;
	private String posterPath;
	// movie fields
	private LocalDate releaseDate;
	// series fields
	private Integer totalEpisodes;
	private LocalDate firstAirDate;
	private LocalDate lastAirDate;
	// userServieData fields
	private Integer episodesWatched;
	private Boolean completed;

	public ServieDtoHomePage(String imdbId, Integer tmdbId, String childtype, String title, String posterPath, LocalDate releaseDate, Integer totalEpisodes, LocalDate firstAirDate, LocalDate lastAirDate, Integer episodesWatched, Boolean completed)
	{
		this.imdbId = imdbId;
		this.tmdbId = tmdbId;
		this.childtype = childtype;
		this.title = title;
		this.posterPath = posterPath;
		this.releaseDate = releaseDate;
		this.totalEpisodes = totalEpisodes;
		this.firstAirDate = firstAirDate;
		this.lastAirDate = lastAirDate;
		this.episodesWatched = episodesWatched;
		this.completed = completed;
	}
}