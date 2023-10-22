package servie.track_servie.payload.dtos.backupData;

import lombok.Data;

@Data
public class UserEpisodeBkpDto
{
	private Integer tmdbId;
	private String title;
	private Integer SeasonNo;
	private Integer episodeNo;
	private Boolean watched;
	private String notes;

	public UserEpisodeBkpDto(Integer tmdbId, String title, Integer seasonNo, Integer episodeNo, Boolean watched, String notes)
	{
		this.tmdbId = tmdbId;
		this.title = title;
		SeasonNo = seasonNo;
		this.episodeNo = episodeNo;
		this.watched = watched;
		this.notes = notes;
	}
}