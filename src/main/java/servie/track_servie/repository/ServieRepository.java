package servie.track_servie.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import servie.track_servie.entity.Servie;
import servie.track_servie.entity.User;
import servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage;
import servie.track_servie.payload.dtos.operationsServiePageDtos.ServieDtoServiePage;
import servie.track_servie.payload.primaryKeys.ServieKey;

@Repository
public interface ServieRepository extends JpaRepository<Servie, ServieKey>
{
	@Query(value = "SELECT new servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage(s.imdbId, s.tmdbId, s.childtype, s.title,"
			+" CASE WHEN usd.posterPath IS NULL THEN s.posterPath ELSE usd.posterPath END,"
			+" m.releaseDate, t.totalEpisodes, t.firstAirDate, t.lastAirDate, usd.episodesWatched, usd.completed)"
			+" FROM Servie AS s"
			+" LEFT JOIN Movie AS m ON m.childtype = s.childtype AND s.tmdbId = m.tmdbId"
			+" LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId"
			+" JOIN UserServieData AS usd ON s = usd.servie"
			+" WHERE usd.user = :user"
			+" AND (:childtype = '' OR s.childtype = :childtype)"
			+" AND (:watched IS NULL OR usd.completed = :watched)"
			+" AND (:languages IS NULL OR s.originalLanguage = :languages)"
			// +" AND (COALESCE(:languages, NULL) IS NULL OR s.originalLanguage IN :languages)"
			// +" AND (:languages IS NULL OR s.originalLanguage IN :languages OR s.originalLanguage IS NULL)"
			// +" AND (:languages IS NULL OR CONCAT(',', s.originalLanguage, ',') LIKE CONCAT('%,', :languages, ',%'))"
			+" AND (:statuses IS NULL OR s.status IN :statuses)"
			+" AND (     (:startYear IS NULL AND :endYear IS NULL)"
			+"        OR (:startYear IS NOT NULL AND :endYear IS NOT NULL AND (     (s.childtype = 'movie' AND m.releaseDate IS NOT NULL AND YEAR(m.releaseDate) BETWEEN :startYear AND :endYear)"
			+"                                                                   OR (s.childtype = 'tv' AND YEAR(t.firstAirDate) >= :startYear AND YEAR(t.lastAirDate) <= :endYear)"
			+"                                                                )"
			+"          )"
			+"        OR (:startYear IS NOT NULL AND :endYear IS NULL AND (     (s.childtype = 'movie' AND (m.releaseDate IS NULL OR YEAR(m.releaseDate) >= :startYear))"
			+"                                                               OR (s.childtype = 'tv' AND YEAR(t.firstAirDate) >= :startYear)"
			+"                                                            )"
			+"           )"
			+"        OR (:startYear IS NULL AND :endYear IS NOT NULL AND (     (s.childtype = 'movie' AND m.releaseDate IS NOT NULL AND YEAR(m.releaseDate) <= :endYear)"
			+"                                                               OR (s.childtype = 'tv' AND YEAR(t.lastAirDate) <= :endYear)"
			+"                                                            )"
			+"           )"
			+"     )")
	Page<ServieDtoHomePage> getServiesByHomePageFilter(@Param("user") User user, @Param("childtype") String childtype, @Param("watched") Boolean watched, @Param("languages") List<String> languages, @Param("statuses") List<String> statuses, @Param("startYear") Integer startYear, @Param("endYear") Integer endYear, Pageable pageable);
	// @Query(	nativeQuery = true,
	// 		value = "SELECT s.imdb_id AS imdbId, s.tmdb_id AS tmdbId, s.childtype AS childtype, s.title AS title, m.release_date AS releaseDate, t.total_episodes AS totalEpisodes, t.first_air_date AS firstAirDate, t.last_air_date AS lastAirDate"
	// 				//        +" (CASE WHEN usd.poster_path IS NULL THEN s.poster_path ELSE usd.poster_path END) AS poster_path,"
	// 				//        +" (SELECT COUNT(*) FROM track_servie.user_episode_data AS ued"
	// 				//        +"   WHERE ued.user_id = 1 AND ued.tmdb_id = s.tmdb_id AND ued.watched = 1) AS episodes_watched,"
	// 				//        +" (CASE WHEN usd.childtype LIKE 'movie' THEN usd.movie_watched"
	// 				//        +"      ELSE (SELECT CASE WHEN episodes_watched = (SELECT s.total_episodes FROM track_servie.series AS s"
	// 				//        +"                                                  WHERE s.tmdb_id = usd.tmdb_id)"
	// 				//        +"      THEN true ELSE false END)"
	// 				//        +"  END) AS completed"
	// 				+" FROM track_servie.servie AS s"
	// 				+" LEFT JOIN track_servie.movie AS m ON m.childtype = s.childtype AND s.tmdb_id = m.tmdb_id"
	// 				+" LEFT JOIN track_servie.series AS t ON s.childtype = t.childtype AND t.tmdb_id = s.tmdb_id"
	// 				+" JOIN track_servie.user_servie_data usd ON s.tmdb_id = usd.tmdb_id AND s.childtype = usd.childtype"
	// 				+" WHERE usd.user_id = 1")
	// Page<ServieDtoHomePage2> getServiesByHomePageFilterNATIVE(/*
	// 															 * @Param("user") User user, @Param("childtype") String
	// 															 * childtype, @Param("watched") Boolean watched, @Param("languages")
	// 															 * List<String> languages, @Param("statuses") List<String>
	// 															 * statuses, @Param("startYear") Integer startYear, @Param("endYear") Integer
	// 															 * endYear,
	// 															 */Pageable pageable);

	@Query(value = "SELECT new servie.track_servie.payload.dtos.operationsServiePageDtos.ServieDtoServiePage(s.tmdbId, s.childtype, s.title, s.status, s.tagline, s.overview,"
			+" CASE WHEN usd.backdropPath IS NULL THEN s.backdropPath ELSE usd.backdropPath END,"
			+" s.lastModified, movie.releaseDate, movie.runtime, t.totalSeasons, t.totalEpisodes, usd.episodesWatched, usd.completed)"
			+" FROM Servie AS s"
			+" LEFT JOIN Movie AS movie"
			+"   ON movie.childtype = s.childtype"
			+"     AND s.tmdbId = movie.tmdbId"
			+" LEFT JOIN Series AS t"
			+"   ON s.childtype = t.childtype"
			+"     AND s.tmdbId = t.tmdbId"
			+" LEFT JOIN UserServieData AS usd"
			+"   ON usd.servie = s"
			+"     AND usd.user = :user"
			+" WHERE s.childtype = :childtype"
			+"   AND s.tmdbId = :tmdbId")
	Optional<ServieDtoServiePage> getServieForServiePage(@Param("user") User user, @Param("childtype") String childtype, @Param("tmdbId") Integer tmdbId);

	@Query(	nativeQuery = true,
			value = "SELECT s.imdb_id, s.tmdb_id, s.title"
					+" FROM track_servie.servie s"
					+" WHERE ((:languages) IS NULL OR s.original_language IN (:languages))")
	Page<Object[]> getTempDtosFK(@Param("languages") List<String> languages, Pageable pageable);
	// 
	// 
	// DID NOT WANTED TO USE LEFT-JOIN UserEpisodeData, but had to because the sub-queries are not working as intended in native queries, but they worked in MySQL workbench
	// @Query(	nativeQuery = true,
	// 		value = "SELECT s.imdb_id, s.tmdb_id, s.childtype, s.title, COALESCE(usd.poster_path, s.poster_path), m.release_date, tv.total_episodes, tv.first_air_date, tv.last_air_date"
	// 				// +" CASE WHEN s.childtype = 'movie' THEN NULL"
	// 				// +"      ELSE COUNT(*)"
	// 				// +" END,"
	// 				// +"    ,CASE WHEN s.childtype = 'movie' THEN NULL ELSE (SELECT COUNT(*) FROM track_servie.user_episode_data ued WHERE ued.user_id = 1 AND ued.tmdb_id = s.tmdb_id AND ued.childtype = s.childtype AND ued.season_no > 0 AND ued.watched = 1) END"
	// 				// +" CASE WHEN s.childtype = 'movie' THEN usd.movie_watched"
	// 				// +"      ELSE (CASE WHEN COUNT(*) = tv.total_episodes THEN TRUE"
	// 				// +"                 ELSE FALSE"
	// 				// +"            END)"
	// 				// +" END"
	// 				// +"	    CASE"
	// 				// +"          WHEN s.childtype = 'movie' THEN usd.movie_watched"
	// 				// +"          ELSE (SELECT"
	// 				// +"                CASE"
	// 				// +"                    WHEN (SELECT COUNT(*) FROM track_servie.user_episode_data AS ued"
	// 				// +"                          WHERE ued.user_id = :userId"tv.total_episodes
	// 				// +"                              AND ued.tmdb_id = s.tmdb_id"
	// 				// +"                              AND ued.childtype = s.childtype"
	// 				// +"                              AND ued.season_no > 0"
	// 				// +"                              AND ued.watched = 1) = tv.total_episodes THEN TRUE"
	// 				// +"                    ELSE FALSE"
	// 				// +"                END"
	// 				// +"               )"
	// 				// +"      END"
	// 				+" FROM track_servie.servie s"
	// 				+" INNER JOIN track_servie.user_servie_data usd"
	// 				+"	  ON usd.tmdb_id = s.tmdb_id"
	// 				+"     AND usd.childtype = s.childtype"
	// 				+"     AND usd.user_id = :userId"
	// 				+" LEFT JOIN track_servie.movie m"
	// 				+"	  ON m.childtype = s.childtype"
	// 				+"     AND m.tmdb_id = s.tmdb_id"
	// 				+" LEFT JOIN track_servie.series tv"
	// 				+"     ON tv.childtype = s.childtype"
	// 				+"     AND tv.tmdb_id = s.tmdb_id"
	// 				// +" LEFT JOIN track_servie.user_episode_data ued"
	// 				// +"     ON ued.user_id = :userId"
	// 				// +"     AND ued.tmdb_id = s.tmdb_id"
	// 				// +"     AND ued.childtype = s.childtype"
	// 				// +"     AND ued.season_no > 0"
	// 				// +"     AND ued.watched = 1"
	// 				+" WHERE (:languages IS null OR s.original_language IN :languages)")
	// // +" WHERE (:languages IS NULL OR s.original_language IN :languages)"
	// // +" GROUP BY s.tmdb_id, s.childtype")
	// Page<Object[]> getTempDtos(@Param("userId") Integer userId, @Param("languages") List<String> languages, /*
	// *,
	// @Param("childtype")
	// String childtype,
	// @Param("watched")
	// Boolean watched,*,
	// @Param("statuses")
	// List<String> statuses,,@Param("startYear") Integer
	// 																										 * startYear, @Param("endYear") Integer endYear*/
	// Pageable pageable);

	Servie findByImdbId(String servieId);

	Servie findByTmdbIdAndChildtype(Integer tmdbId, String childtype);

	@Query(value = "SELECT new servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage(s.imdbId, s.tmdbId, s.childtype, s.title,"
			+" s.posterPath,"
			+" m.releaseDate, t.totalEpisodes, t.firstAirDate, t.lastAirDate, 0, true)"
			+" FROM Servie AS s"
			+" LEFT JOIN Movie AS m ON m.childtype = s.childtype AND s.tmdbId = m.tmdbId"
			+" LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId"
			+" JOIN WatchList AS w ON w.servie = s")
	Page<ServieDtoHomePage> getServiesForWatchList(Pageable pageable);
}