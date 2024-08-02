package servie.track_servie.payload.dtos.operationsMovieCollectionPageDtos;

import java.time.LocalDate;
import lombok.Data;

@Data
public class MovieDtoMovieCollectionPageDto
{
	// servie db
	private Integer tmdbId;
	private String title;
	private String posterPath;
	// movie db
	private LocalDate releaseDate;
	// userServieData db
	private boolean movieWatched = false;

	public MovieDtoMovieCollectionPageDto(Integer tmdbId, String title, String posterPath, LocalDate releaseDate, boolean movieWatched)
	{
		this.tmdbId = tmdbId;
		this.title = title;
		this.posterPath = posterPath;
		this.releaseDate = releaseDate;
		this.movieWatched = movieWatched;
	}
}