package servie.track_servie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import servie.track_servie.entity.Season;

@Repository
public interface SeasonRepository extends JpaRepository<Season, String>
{
    // Returns specific Season from the database which matches the criteria
    Season findByTmdbIdAndSeasonNumber(Integer tmdbId, Integer seasonNumber);
}
