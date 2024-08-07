package servie.track_servie.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import servie.track_servie.entity.User;
import servie.track_servie.entity.UserSeasonData;
import servie.track_servie.payload.dtos.operationsServiePageDtos.SeasonDtoServiePage;
import servie.track_servie.payload.primaryKeys.UserSeasonDataKey;

@Repository
public interface UserSeasonDataRepository extends JpaRepository<UserSeasonData, UserSeasonDataKey>
{
	@Query(value = "SELECT new servie.track_servie.payload.dtos.operationsServiePageDtos.SeasonDtoServiePage(season.id, season.name, season.seasonNo,"
			+" COALESCE(userSeasonData.posterPath, season.posterPath) AS posterPath,"
			+" season.episodeCount, userSeasonData.episodesWatched, userSeasonData.watched)"
			+" FROM Season AS season"
			+" LEFT JOIN UserSeasonData AS userSeasonData"
			+"   ON userSeasonData.seasonNo = season.seasonNo"
			+"     AND userSeasonData.userServieData.servie.tmdbId = season.series.tmdbId"
			+"     AND userSeasonData.userServieData.user = :user"
			+" WHERE season.series.childtype = :childtype"
			+"   AND season.series.tmdbId = :tmdbId")
	List<SeasonDtoServiePage> getSeasons(@Param("user") User user, @Param("childtype") String childtype, @Param("tmdbId") Integer tmdbId);

	@Query(value = "SELECT userSeasonData.seasonNo"
			+" FROM UserSeasonData AS userSeasonData"
			+" WHERE (:user IS NULL OR userSeasonData.userServieData.user = :user)"
			+"   AND userSeasonData.userServieData.servie.childtype = :childtype"
			+"   AND userSeasonData.userServieData.servie.tmdbId = :tmdbId"
			+"   AND userSeasonData.seasonNo > 0"
			+"   AND userSeasonData.watched IS NOT NULL")
	List<Integer> getAllNonNullSeasons(User user, String childtype, Integer tmdbId);

	@Query(value = "SELECT sea.seasonNo"
			+" FROM Season AS sea"
			+" LEFT JOIN (SELECT usd.userServieData.servie.tmdbId AS tmdbId, usd.seasonNo AS seasonNo"
			+" 			  FROM UserSeasonData AS usd"
			+"            WHERE usd.userServieData.user = :user"
			+"              AND usd.userServieData.servie.tmdbId = :tmdbId"
			+"              AND usd.watched = TRUE) AS usd"
			+" ON usd.tmdbId = sea.series.tmdbId"
			+"   AND usd.seasonNo = sea.seasonNo"
			+" WHERE sea.series.tmdbId = :tmdbId"
			+"   AND sea.seasonNo > 0"
			+"   AND usd.seasonNo IS NULL"
			+" ORDER BY sea.seasonNo")
	List<Integer> getAllIncompleteSeasons(@Param("user") User user, @Param("tmdbId") Integer tmdbId);
}