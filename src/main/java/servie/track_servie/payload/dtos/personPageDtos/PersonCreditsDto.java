package servie.track_servie.payload.dtos.personPageDtos;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

// person-combined-credits
@Data
public class PersonCreditsDto
{
	@JsonProperty("id")
	private Integer id;
	// ---------------------------------------------------------------
	@JsonProperty("cast")
	private List<PersonCastDto> cast;
	// ---------------------------------------------------------------
	// @JsonProperty("crew")
	// private Set<PersonCrewDto> crew;
}
