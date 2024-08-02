package servie.track_servie.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import servie.track_servie.entity.Movie;
import servie.track_servie.payload.dtos.operationsMovieCollectionPageDtos.MovieDtoMovieCollectionPageDto;
import servie.track_servie.payload.primaryKeys.ServieKey;

@Repository
public interface MovieRepository extends JpaRepository<Movie, ServieKey>
{
	Optional<Movie> findByTmdbId(Integer tmdbId);

	@Query(value = "SELECT new servie.track_servie.payload.dtos.operationsMovieCollectionPageDtos.MovieDtoMovieCollectionPageDto(ss.tmdbId, ss.title, ss.posterPath, mm.releaseDate, COALESCE(usd.movieWatched, false))"
			+" FROM Servie ss"
			+" JOIN Movie mm"
			+"    ON ss.tmdbId = mm.tmdbId AND ss.childtype = mm.childtype"
			+" LEFT JOIN UserServieData usd"
			+"    ON ss = usd.servie AND usd.user.id = 1"
			+" WHERE mm.collection.id = :id")
	List<MovieDtoMovieCollectionPageDto> getMovieDtoMovieCollectionPageDtos(Integer id);
}
