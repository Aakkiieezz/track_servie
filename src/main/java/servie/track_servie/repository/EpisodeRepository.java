package servie.track_servie.repository;

// import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import servie.track_servie.entity.Episode;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, String>
{
    // Returns specific Episode from the database which matches the criteria
    Episode findByTmdbIdAndSeasonNoAndEpisodeNo(Integer tmdbId, Integer seasonNo, Integer episodeNo);
    // 
    // Returns list of Episodes of a specific Season, from the database, which matches the criteria
    // List<Episode> findByTmdbIdAndSeasonNoAndWatched(Integer tmdbId, Integer seasonNo, boolean watched);
    // 
    // Returns list of Episodes of a specific Series, from the database, which matches the criteria
    // List<Episode> findByTmdbIdAndWatched(Integer tmdbId, Boolean watched);
}