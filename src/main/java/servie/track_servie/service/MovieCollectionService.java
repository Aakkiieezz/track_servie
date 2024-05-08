package servie.track_servie.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import servie.track_servie.payload.dtos.operationsMovieCollectionPageDtos.MovieCollectionPageDto;
import servie.track_servie.payload.dtos.operationsMovieCollectionPageDtos.MovieDtoMovieCollectionPageDto;
import servie.track_servie.entity.MovieCollection;
import servie.track_servie.repository.MovieCollectionRepository;

@Service
public class MovieCollectionService
{
	@Autowired
	private MovieCollectionRepository movieCollectionRepository;
	@Autowired
	private ServieService servieService;

	public MovieCollectionPageDto getMovieCollection(Integer id)
	{
		MovieCollection movieCollection = movieCollectionRepository.getReferenceById(id);
		MovieCollectionPageDto movieCollectionPageDto = new MovieCollectionPageDto();
		movieCollectionPageDto.setId(movieCollection.getId());
		movieCollectionPageDto.setOverview(movieCollection.getOverview());
		movieCollectionPageDto.setBackdropPath(movieCollection.getBackdropPath());
		List<MovieDtoMovieCollectionPageDto> movieDtoMovieCollectionPageDtos = servieService.getMovieDtoMovieCollectionPageDtos(id);
		movieCollectionPageDto.setMovies(movieDtoMovieCollectionPageDtos);
		return movieCollectionPageDto;
	}
}