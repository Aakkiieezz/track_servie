package servie.track_servie.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import servie.track_servie.entity.User;
import servie.track_servie.entity.UserEpisodeData;
import servie.track_servie.entity.UserSeasonData;
import servie.track_servie.entity.UserServieData;
import servie.track_servie.payload.dtos.operationsSeasonPageDtos.EpisodeDtoSeasonPage;
import servie.track_servie.payload.primaryKeys.UserEpisodeDataKey;

@Repository
public interface UserEpisodeDataRepository extends JpaRepository<UserEpisodeData, UserEpisodeDataKey>
{
	List<UserEpisodeData> findByUserSeasonDataAndWatched(UserSeasonData userSeasonData, Boolean watched);

	// ??? what if did like this - SELECT userEpisode FROM UserEpisodeData userEpisode WHERE userEpisode.userSeasonData.userServieData = :servie AND userEpisode.watched = :watched
	@Query(value = "SELECT userEpisode FROM UserEpisodeData userEpisode"
			+" JOIN userEpisode.userSeasonData userSeason"
			+" JOIN userSeason.userServieData userServie"
			+" WHERE userServie = :servie"
			+" AND userEpisode.watched = :watched")
	List<UserEpisodeData> findByUserServieDataAndWatched(@Param("servie") UserServieData userServieData, @Param("watched") Boolean watched);

	@Query(value = "SELECT new servie.track_servie.entity.UserEpisodeData(episode.episodeNo, :watched, ued.notes)"
			+" FROM Episode AS episode"
			+" LEFT JOIN UserEpisodeData AS ued"
			+"   ON ued.episodeNo = episode.episodeNo"
			+"     AND ued.userSeasonData.seasonNo = episode.seasonNo"
			+"     AND ued.userSeasonData.userServieData.servie.tmdbId = episode.season.series.tmdbId"
			// +"   AND (:user IS NULL OR ued.userSeasonData.userServieData.user = :user)"
			+" WHERE episode.season.series.tmdbId = :tmdbId"
			+"   AND episode.seasonNo = :seasonNo"
			// +"   AND ued.userSeasonData.userServieData.user = :user"
			+"   AND (ued.watched IS NULL OR ued.watched <> :watched)")
	List<UserEpisodeData> getToggledEpisodes(Integer tmdbId, Integer seasonNo, Boolean watched);

	@Query(value = "SELECT new servie.track_servie.payload.dtos.operationsSeasonPageDtos.EpisodeDtoSeasonPage(episode.name, episode.episodeNo, episode.stillPath, episode.overview, episode.runtime, episode.lastModified, episode.airDate, ued.watched)"
			+" FROM Episode AS episode"
			+" LEFT JOIN UserEpisodeData AS ued"
			+"   ON ued.episodeNo = episode.episodeNo"
			+"     AND ued.userSeasonData.seasonNo = episode.seasonNo"
			+"     AND ued.userSeasonData.userServieData.servie.tmdbId = episode.season.series.tmdbId"
			+"     AND (:user IS NULL OR ued.userSeasonData.userServieData.user = :user)"
			+" WHERE episode.season.series.tmdbId = :tmdbId"
			+"   AND episode.seasonNo = :seasonNo"
			// +"   AND ued.userSeasonData.userServieData.user = :user"
			+"")
	List<EpisodeDtoSeasonPage> getEpisodesForSeasonPage(User user, Integer tmdbId, Integer seasonNo);
}