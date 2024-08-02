package servie.track_servie.payload.dtos.personPageDtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class PersonPageResponseDto
{
	private List<PersonCastDto> servies;
	private Integer id;
	// ??? Person has a imdbId ???
	private String imdbId;
	private String name;
	private String knownForDepartment;
	private Integer gender;
	private Boolean adult;
	private Double popularity;
	private String profilePath;
	private LocalDate birthday;
	private LocalDate deathday;
	private String biography;
	private String birthPlace;
	private String homepage;
	private LocalDateTime lastModified;
}
