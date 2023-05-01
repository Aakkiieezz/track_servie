package servie.track_servie.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import servie.track_servie.entities.Series;
import servie.track_servie.payload.primaryKeys.ServieKey;

@Repository
public interface SeriesRepository extends JpaRepository<Series, ServieKey>
{
    Optional<Series> findByTmdbId(Integer tmdbId);
}
