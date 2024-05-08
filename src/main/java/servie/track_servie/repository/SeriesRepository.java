package servie.track_servie.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import servie.track_servie.entity.Series;
import servie.track_servie.payload.primaryKeys.ServieKey;

@Repository
public interface SeriesRepository extends JpaRepository<Series, ServieKey>
{
	Optional<Series> findByTmdbId(Integer tmdbId);

	List<Series> findByLastModifiedBefore(LocalDateTime of);

	List<Series> findByLastModifiedBeforeAndStatus(LocalDateTime of, String string);
}
