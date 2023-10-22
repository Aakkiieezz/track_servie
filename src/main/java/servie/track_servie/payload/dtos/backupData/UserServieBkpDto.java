package servie.track_servie.payload.dtos.backupData;

import lombok.Data;

@Data
public class UserServieBkpDto
{
	private Integer tmdbId;
	private String childtype;
	private String title;
	private Boolean movieWatched;
	private String backdropPath;
	private String posterPath;
	private String notes;

	public UserServieBkpDto(Integer tmdbId, String childtype, String title, Boolean movieWatched, String backdropPath, String posterPath, String notes)
	{
		this.tmdbId = tmdbId;
		this.childtype = childtype;
		this.title = title;
		this.movieWatched = movieWatched;
		this.backdropPath = backdropPath;
		this.posterPath = posterPath;
		this.notes = notes;
	}
}
