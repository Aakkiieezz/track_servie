package servie.track_servie.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import servie.track_servie.entity.UserEpisodeData;
import servie.track_servie.entity.UserSeasonData;
import servie.track_servie.entity.UserServieData;
import servie.track_servie.payload.primaryKeys.UserEpisodeDataKey;

@Repository
public interface UserEpisodeDataRepository extends JpaRepository<UserEpisodeData, UserEpisodeDataKey>
{
    // UserEpisodeData findByUserIdAndTmdbIdAndSeasonNumberAndEpisodeId(Integer userId, Integer tmdbId, Integer seasonNumber, String episodeId);
    // Returns specific Episode from the database which matches the criteria
    // UserEpisodeData findByUserIdAndTmdbIdAndSeasonNumberAndEpisodeNumber(Integer userId, Integer tmdbId, Integer seasonNumber, Integer episodeNumber);
    // Returns list of Episodes of a specific Season, from the database, which matches the criteria
    // List<UserEpisodeData> findByUserIdAndTmdbIdAndSeasonNumberAndWatched(Integer userId, Integer tmdbId, Integer seasonNumber, boolean watched);
    List<UserEpisodeData> findByUserSeasonDataAndWatched(UserSeasonData userSeasonData, Boolean watched);
    // Returns list of Episodes of a specific Series, from the database, which matches the criteria
    // List<UserEpisodeData> findByUserIdAndTmdbIdAndWatched(Integer userId, Integer tmdbId, Boolean watched);
    // List<UserEpisodeData> findByUserServieDataAndWatched(UserServieData userServieData, Boolean watched);

    @Query(value = "SELECT userEpisode FROM UserEpisodeData userEpisode JOIN userEpisode.userSeasonData userSeason JOIN userSeason.userServieData userServie WHERE userServie = :servie AND userEpisode.watched = :watched")
    List<UserEpisodeData> findByUserServieDataAndWatched(@Param("servie") UserServieData userServieData, @Param("watched") Boolean watched);
}