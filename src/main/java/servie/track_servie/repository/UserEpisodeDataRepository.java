package servie.track_servie.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import servie.track_servie.entities.UserEpisodeData;
import servie.track_servie.payload.primaryKeys.UserEpisodeKey;

@Repository
public interface UserEpisodeDataRepository extends JpaRepository<UserEpisodeData, UserEpisodeKey>
{
    UserEpisodeData findByUserIdAndTmdbIdAndSeasonNumberAndEpisodeId(Integer userId, Integer tmdbId, Integer seasonNumber, String episodeId);

    // Returns specific Episode from the database which matches the criteria
    UserEpisodeData findByUserIdAndTmdbIdAndSeasonNumberAndEpisodeNumber(Integer userId, Integer tmdbId, Integer seasonNumber, Integer episodeNumber);

    // Returns list of Episodes of a specific Season, from the database, which matches the criteria
    List<UserEpisodeData> findByUserIdAndTmdbIdAndSeasonNumberAndWatched(Integer userId, Integer tmdbId, Integer seasonNumber, boolean watched);

    // Returns list of Episodes of a specific Series, from the database, which matches the criteria
    List<UserEpisodeData> findByUserIdAndTmdbIdAndWatched(Integer userId, Integer tmdbId, Boolean watched);
}
