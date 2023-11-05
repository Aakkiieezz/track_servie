package servie.track_servie.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TempDto
{
	public String imdbId;
	public Integer tmdbId;

	public TempDto(String imdbId, Integer tmdbId)
	{
		this.imdbId = imdbId;
		this.tmdbId = tmdbId;
	}

	public String getImdbId()
	{
		return imdbId;
	}

	public Integer getTmdbId()
	{
		return tmdbId;
	}
}
