package servie.track_servie.payload.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LanguageDto
{
	@JsonProperty("iso_639_1")
	private String iso;
	// ---------------------------------------------------------------
	@JsonProperty("english_name")
	private String name;
}
