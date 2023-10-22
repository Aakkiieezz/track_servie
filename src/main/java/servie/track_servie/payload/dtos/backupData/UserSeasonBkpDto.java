package servie.track_servie.payload.dtos.backupData;

import lombok.Data;

@Data
public class UserSeasonBkpDto
{
	private Integer tmdbId;
	private String childtype;
	private String title;
	private Integer SeasonNo;
	private String posterPath;
	private String notes;

	public UserSeasonBkpDto(Integer tmdbId, String childtype, String title, Integer seasonNo, String posterPath, String notes)
	{
		this.tmdbId = tmdbId;
		this.childtype = childtype;
		this.title = title;
		SeasonNo = seasonNo;
		this.posterPath = posterPath;
		this.notes = notes;
	}
}