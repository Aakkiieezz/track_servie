package servie.track_servie.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import servie.track_servie.entities.Movie;
import servie.track_servie.payload.primaryKeys.ServieKey;

@Repository
public interface MovieRepository extends JpaRepository<Movie, ServieKey>
{
    Optional<Movie> findByTmdbId(Integer tmdbId);
}
