package servie.track_servie.payload.dtos.operationsMovieCollectionPageDtos;

import java.util.List;
import lombok.Data;

@Data
public class MovieCollectionPageDto
{
	private Integer id;
	private String name;
	private String overview;
	private String backdropPath;
	private List<MovieDtoMovieCollectionPageDto> movies;
}
