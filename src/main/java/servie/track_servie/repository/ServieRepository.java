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
import servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage2;
import servie.track_servie.payload.dtos.operationsServiePageDtos.ServieDtoServiePage;
import servie.track_servie.payload.primaryKeys.ServieKey;

@Repository
public interface ServieRepository extends JpaRepository<Servie, ServieKey>
{
	// @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage(s.imdbId, s.tmdbId, s.childtype, s.title, s.posterPath, m.releaseDate, t.totalEpisodes, t.firstAirDate, t.lastAirDate, usd.episodesWatched, usd.completed) FROM Servie AS s "+"LEFT JOIN Movie AS m ON s.childtype = m.childtype AND s.tmdbId = m.tmdbId "+"LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId "+"JOIN UserServieData AS usd ON s = usd.servie "+"WHERE usd.userId = :userId")
	// Page<ServieDtoHomePage> findAllServiesByUserId(@Param("userId") Integer userId, Pageable pageable);
	// @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage(s.imdbId, s.tmdbId, s.childtype, s.title, s.posterPath, m.releaseDate, t.totalEpisodes, t.firstAirDate, t.lastAirDate, usd.episodesWatched, usd.completed) FROM Servie AS s "+"LEFT JOIN Movie AS m ON s.childtype = m.childtype AND s.tmdbId = m.tmdbId "+"LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId "+"JOIN UserServieData AS usd ON s = usd.servie "+"WHERE usd.userId = :userId AND s.childtype = :childtype")
	// Page<ServieDtoHomePage> findByType(@Param("userId") Integer userId, @Param("type") String type, Pageable pageable);
	// @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage(s.imdbId, s.tmdbId, s.childtype, s.title, s.posterPath, m.releaseDate, t.totalEpisodes, t.firstAirDate, t.lastAirDate, usd.episodesWatched, usd.completed) FROM Servie AS s "+"LEFT JOIN Movie AS m ON s.childtype = m.childtype AND s.tmdbId = m.tmdbId "+"LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId "+"JOIN UserServieData AS usd ON s = usd.servie "+"WHERE usd.userId = :userId AND usd.completed = :watched")
	// Page<ServieDtoHomePage> findByCompleted(@Param("userId") Integer userId, @Param("watched") Boolean watched, Pageable pageable);
	// @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage(s.imdbId, s.tmdbId, s.childtype, s.title, s.posterPath, m.releaseDate, t.totalEpisodes, t.firstAirDate, t.lastAirDate, usd.episodesWatched, usd.completed) FROM Servie AS s "+"LEFT JOIN Movie AS m ON s.childtype = m.childtype AND s.tmdbId = m.tmdbId "+"LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId "+"JOIN UserServieData AS usd ON s = usd.servie "+"JOIN s.genres g "+"WHERE g IN :genreList AND usd.userId = :userId "+"GROUP BY s HAVING COUNT(DISTINCT g) = :genreListSize")
	// Page<ServieDtoHomePage> findByGenres(@Param("userId") Integer userId, @Param("genreList") List<Genre> genres, @Param("genreListSize") long genreListSize, Pageable pageable);
	// @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage(s.imdbId, s.tmdbId, s.childtype, s.title, s.posterPath, m.releaseDate, t.totalEpisodes, t.firstAirDate, t.lastAirDate, usd.episodesWatched, usd.completed) FROM Servie AS s "+"LEFT JOIN Movie AS m ON s.childtype = m.childtype AND s.tmdbId = m.tmdbId "+"LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId "+"JOIN UserServieData AS usd ON s = usd.servie "+"WHERE s.originalLanguage IN :languages AND usd.userId = :userId")
	// Page<ServieDtoHomePage> findByLanguages(@Param("userId") Integer userId, @Param("languages") List<String> languages, Pageable pageable);
	// @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage(s.imdbId, s.tmdbId, s.childtype, s.title, s.posterPath, m.releaseDate, t.totalEpisodes, t.firstAirDate, t.lastAirDate, usd.episodesWatched, usd.completed) FROM Servie AS s "+"LEFT JOIN Movie AS m ON s.childtype = m.childtype AND s.tmdbId = m.tmdbId "+"LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId "+"JOIN UserServieData AS usd ON s = usd.servie "+"WHERE s.status IN :statuses AND usd.userId = :userId")
	// Page<ServieDtoHomePage> findByStatus(Integer userId, @Param("statuses") List<String> status, Pageable pageable);
	// @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage(s.imdbId, s.tmdbId, s.childtype, s.title, s.posterPath, m.releaseDate, t.totalEpisodes, t.firstAirDate, t.lastAirDate, usd.episodesWatched, usd.completed) FROM Servie AS s "+"LEFT JOIN Movie AS m ON s.childtype = m.childtype AND s.tmdbId = m.tmdbId "+"LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId "+"JOIN UserServieData AS usd ON s = usd.servie "+"WHERE usd.userId = :userId AND YEAR(m.releaseDate) >= :startYear OR m.releaseDate IS NULL")
	// Page<ServieDtoHomePage> findServiesAfterYear(@Param("userId") Integer userId, @Param("startYear") Integer startYear, Pageable pageable);
	// @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage(s.imdbId, s.tmdbId, s.childtype, s.title, s.posterPath, m.releaseDate, t.totalEpisodes, t.firstAirDate, t.lastAirDate, usd.episodesWatched, usd.completed) FROM Servie AS s "+"LEFT JOIN Movie AS m ON s.childtype = m.childtype AND s.tmdbId = m.tmdbId "+"LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId "+"JOIN UserServieData AS usd ON s = usd.servie "+"WHERE usd.userId = :userId AND YEAR(m.releaseDate) <= :endYear")
	// Page<ServieDtoHomePage> findServiesBeforeYear(@Param("userId") Integer userId, @Param("endYear") Integer endYear, Pageable pageable);
	// @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage(s.imdbId, s.tmdbId, s.childtype, s.title, s.posterPath, m.releaseDate, t.totalEpisodes, t.firstAirDate, t.lastAirDate, usd.episodesWatched, usd.completed) FROM Servie AS s "+"LEFT JOIN Movie AS m ON s.childtype = m.childtype AND s.tmdbId = m.tmdbId "+"LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId "+"JOIN UserServieData AS usd ON s = usd.servie "+"WHERE usd.userId = :userId AND YEAR(m.releaseDate) BETWEEN :startYear AND :endYear")
	// Page<ServieDtoHomePage> findServiesBetweenStartYearAndEndYear(@Param("userId") Integer userId, @Param("startYear") Integer startYear, @Param("endYear") Integer endYear, Pageable pageable);
	// 
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
			+" AND (:languages IS NULL OR s.originalLanguage IN :languages)"
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

	@Query(	nativeQuery = true,
			value = "SELECT s.imdb_id AS imdbId, s.tmdb_id AS tmdbId, s.childtype AS childtype, s.title AS title, m.release_date AS releaseDate, t.total_episodes AS totalEpisodes, t.first_air_date AS firstAirDate, t.last_air_date AS lastAirDate"
					//        +" (CASE WHEN usd.poster_path IS NULL THEN s.poster_path ELSE usd.poster_path END) AS poster_path,"
					//        +" (SELECT COUNT(*) FROM track_servie_stg.user_episode_data AS ued"
					//        +"   WHERE ued.user_id = 1 AND ued.tmdb_id = s.tmdb_id AND ued.watched = 1) AS episodes_watched,"
					//        +" (CASE WHEN usd.childtype LIKE 'movie' THEN usd.movie_watched"
					//        +"      ELSE (SELECT CASE WHEN episodes_watched = (SELECT s.total_episodes FROM track_servie_stg.series AS s"
					//        +"                                                  WHERE s.tmdb_id = usd.tmdb_id)"
					//        +"      THEN true ELSE false END)"
					//        +"  END) AS completed"
					+" FROM track_servie_stg.servie AS s"
					+" LEFT JOIN track_servie_stg.movie AS m ON m.childtype = s.childtype AND s.tmdb_id = m.tmdb_id"
					+" LEFT JOIN track_servie_stg.series AS t ON s.childtype = t.childtype AND t.tmdb_id = s.tmdb_id"
					+" JOIN track_servie_stg.user_servie_data usd ON s.tmdb_id = usd.tmdb_id AND s.childtype = usd.childtype"
					+" WHERE usd.user_id = 1")
	Page<ServieDtoHomePage2> getServiesByHomePageFilterNATIVE(/*
																 * @Param("user") User user, @Param("childtype") String
																 * childtype, @Param("watched") Boolean watched, @Param("languages")
																 * List<String> languages, @Param("statuses") List<String>
																 * statuses, @Param("startYear") Integer startYear, @Param("endYear") Integer
																 * endYear,
																 */Pageable pageable);

	@Query(value = "SELECT new servie.track_servie.payload.dtos.operationsServiePageDtos.ServieDtoServiePage(s.tmdbId, s.childtype, s.title, s.status, s.tagline, s.overview,"
			+" CASE WHEN usd.backdropPath IS NULL THEN s.backdropPath ELSE usd.backdropPath END,"
			+" m.releaseDate, m.runtime, t.totalSeasons, t.totalEpisodes, usd.episodesWatched, usd.completed)"
			+" FROM Servie AS s"
			+" LEFT JOIN Movie AS m ON s.childtype = m.childtype AND s.tmdbId = m.tmdbId"
			+" LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId"
			+" LEFT JOIN UserServieData AS usd ON usd.servie = s AND usd.user = :user"
			+" WHERE s.childtype = :childtype AND s.tmdbId = :tmdbId")
	Optional<ServieDtoServiePage> getServieForServiePage(@Param("user") User user, @Param("childtype") String childtype, @Param("tmdbId") Integer tmdbId);

	Servie findByImdbId(String servieId);

	Servie findByTmdbIdAndChildtype(Integer tmdbId, String childtype);
}