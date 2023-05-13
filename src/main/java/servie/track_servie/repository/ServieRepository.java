package servie.track_servie.repository;

import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import servie.track_servie.entities.Genre;
import servie.track_servie.entities.Servie;
import servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage;
import servie.track_servie.payload.dtos.operationsSeriesPageDtos.GenreDtoSeriesPage;
import servie.track_servie.payload.dtos.operationsSeriesPageDtos.SeriesDtoSeriesPage;
import servie.track_servie.payload.primaryKeys.ServieKey;

@Repository
public interface ServieRepository extends JpaRepository<Servie, ServieKey>
{
    @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage(s.imdbId, s.tmdbId, s.childtype, s.title, s.posterPath, m.releaseDate, t.numberOfEpisodes, t.firstAirDate, t.lastAirDate, usd.episodesWatched, usd.completed) FROM Servie AS s "+"LEFT JOIN Movie AS m ON s.childtype = m.childtype AND s.tmdbId = m.tmdbId "+"LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId "+"JOIN UserServieData AS usd ON s.childtype = usd.childtype AND s.tmdbId = usd.tmdbId "+"WHERE usd.userId = :userId")
    Page<ServieDtoHomePage> findAllServiesByUserId(@Param("userId") Integer userId, Pageable pageable);

    @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage(s.imdbId, s.tmdbId, s.childtype, s.title, s.posterPath, m.releaseDate, t.numberOfEpisodes, t.firstAirDate, t.lastAirDate, usd.episodesWatched, usd.completed) FROM Servie AS s "+"LEFT JOIN Movie AS m ON s.childtype = m.childtype AND s.tmdbId = m.tmdbId "+"LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId "+"JOIN UserServieData AS usd ON s.childtype = usd.childtype AND s.tmdbId = usd.tmdbId "+"WHERE usd.userId = :userId AND s.childtype = :type")
    Page<ServieDtoHomePage> findByType(@Param("userId") Integer userId, @Param("type") String type, Pageable pageable);

    @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage(s.imdbId, s.tmdbId, s.childtype, s.title, s.posterPath, m.releaseDate, t.numberOfEpisodes, t.firstAirDate, t.lastAirDate, usd.episodesWatched, usd.completed) FROM Servie AS s "+"LEFT JOIN Movie AS m ON s.childtype = m.childtype AND s.tmdbId = m.tmdbId "+"LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId "+"JOIN UserServieData AS usd ON s.childtype = usd.childtype AND s.tmdbId = usd.tmdbId "+"WHERE usd.userId = :userId AND usd.completed = :watched")
    Page<ServieDtoHomePage> findByCompleted(@Param("userId") Integer userId, @Param("watched") Boolean watched, Pageable pageable);

    @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage(s.imdbId, s.tmdbId, s.childtype, s.title, s.posterPath, m.releaseDate, t.numberOfEpisodes, t.firstAirDate, t.lastAirDate, usd.episodesWatched, usd.completed) FROM Servie AS s "+"LEFT JOIN Movie AS m ON s.childtype = m.childtype AND s.tmdbId = m.tmdbId "+"LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId "+"JOIN UserServieData AS usd ON s.childtype = usd.childtype AND s.tmdbId = usd.tmdbId "+"JOIN s.genres g "+"WHERE g IN :genreList AND usd.userId = :userId "+"GROUP BY s HAVING COUNT(DISTINCT g) = :genreListSize")
    Page<ServieDtoHomePage> findByGenres(@Param("userId") Integer userId, @Param("genreList") List<Genre> genres, @Param("genreListSize") long genreListSize, Pageable pageable);

    @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage(s.imdbId, s.tmdbId, s.childtype, s.title, s.posterPath, m.releaseDate, t.numberOfEpisodes, t.firstAirDate, t.lastAirDate, usd.episodesWatched, usd.completed) FROM Servie AS s "+"LEFT JOIN Movie AS m ON s.childtype = m.childtype AND s.tmdbId = m.tmdbId "+"LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId "+"JOIN UserServieData AS usd ON s.childtype = usd.childtype AND s.tmdbId = usd.tmdbId "+"WHERE s.originalLanguage IN :languages AND usd.userId = :userId")
    Page<ServieDtoHomePage> findByLanguages(@Param("userId") Integer userId, @Param("languages") List<String> languages, Pageable pageable);

    @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage(s.imdbId, s.tmdbId, s.childtype, s.title, s.posterPath, m.releaseDate, t.numberOfEpisodes, t.firstAirDate, t.lastAirDate, usd.episodesWatched, usd.completed) FROM Servie AS s "+"LEFT JOIN Movie AS m ON s.childtype = m.childtype AND s.tmdbId = m.tmdbId "+"LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId "+"JOIN UserServieData AS usd ON s.childtype = usd.childtype AND s.tmdbId = usd.tmdbId "+"WHERE s.status IN :statuses AND usd.userId = :userId")
    Page<ServieDtoHomePage> findByStatus(Integer userId, @Param("statuses") List<String> status, Pageable pageable);

    @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage(s.imdbId, s.tmdbId, s.childtype, s.title, s.posterPath, m.releaseDate, t.numberOfEpisodes, t.firstAirDate, t.lastAirDate, usd.episodesWatched, usd.completed) FROM Servie AS s "+"LEFT JOIN Movie AS m ON s.childtype = m.childtype AND s.tmdbId = m.tmdbId "+"LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId "+"JOIN UserServieData AS usd ON s.childtype = usd.childtype AND s.tmdbId = usd.tmdbId "+"WHERE usd.userId = :userId AND YEAR(m.releaseDate) >= :startYear OR m.releaseDate IS NULL")
    Page<ServieDtoHomePage> findServiesAfterYear(@Param("userId") Integer userId, @Param("startYear") Integer startYear, Pageable pageable);

    @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage(s.imdbId, s.tmdbId, s.childtype, s.title, s.posterPath, m.releaseDate, t.numberOfEpisodes, t.firstAirDate, t.lastAirDate, usd.episodesWatched, usd.completed) FROM Servie AS s "+"LEFT JOIN Movie AS m ON s.childtype = m.childtype AND s.tmdbId = m.tmdbId "+"LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId "+"JOIN UserServieData AS usd ON s.childtype = usd.childtype AND s.tmdbId = usd.tmdbId "+"WHERE usd.userId = :userId AND YEAR(m.releaseDate) <= :endYear")
    Page<ServieDtoHomePage> findServiesBeforeYear(@Param("userId") Integer userId, @Param("endYear") Integer endYear, Pageable pageable);

    @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage(s.imdbId, s.tmdbId, s.childtype, s.title, s.posterPath, m.releaseDate, t.numberOfEpisodes, t.firstAirDate, t.lastAirDate, usd.episodesWatched, usd.completed) FROM Servie AS s "+"LEFT JOIN Movie AS m ON s.childtype = m.childtype AND s.tmdbId = m.tmdbId "+"LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId "+"JOIN UserServieData AS usd ON s.childtype = usd.childtype AND s.tmdbId = usd.tmdbId "+"WHERE usd.userId = :userId AND YEAR(m.releaseDate) BETWEEN :startYear AND :endYear")
    Page<ServieDtoHomePage> findServiesBetweenStartYearAndEndYear(@Param("userId") Integer userId, @Param("startYear") Integer startYear, @Param("endYear") Integer endYear, Pageable pageable);
    // 
    // @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage(s.imdbId, s.tmdbId, s.childtype, s.title, s.posterPath, m.releaseDate, t.numberOfEpisodes, t.firstAirDate, t.lastAirDate, usd.episodesWatched, usd.completed) FROM Servie AS s "
    // +"LEFT JOIN Movie AS m ON s.childtype = m.childtype AND s.tmdbId = m.tmdbId "
    // +"LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId "
    // +"JOIN UserServieData AS usd ON s.childtype = usd.childtype AND s.tmdbId = usd.tmdbId "
    // +"WHERE usd.userId = :userId "+"AND (:type = '' OR s.childtype = :type) "
    // +"AND (:watched IS NULL OR usd.completed = :watched) "
    // +"AND (:languages IS NULL OR s.originalLanguage IN :languages) "
    // +"AND (:statuses IS NULL OR s.status IN :statuses) "
    // +"AND ( "
    // +"      (:startYear IS NULL AND :endYear IS NULL) "
    // +"      OR (:startYear IS NOT NULL AND :endYear IS NOT NULL AND ( "
    // +"                                                               (s.childtype = 'movie' AND m.releaseDate IS NOT NULL AND YEAR(m.releaseDate) BETWEEN :startYear AND :endYear) "
    // +"                                                               OR (s.childtype = 'tv' AND YEAR(t.firstAirDate) >= :startYear AND YEAR(t.lastAirDate) <= :endYear) "
    // +"                                                              ) "
    // +"         ) "
    // +"      OR (:startYear IS NOT NULL AND :endYear IS NULL AND ( "
    // +"                                                               (s.childtype = 'movie' AND (m.releaseDate IS NULL OR YEAR(m.releaseDate) >= :startYear)) "
    // +"                                                               OR (s.childtype = 'tv' AND YEAR(t.firstAirDate) >= :startYear) "
    // +"                                                          ) "
    // +"         ) "
    // +"      OR (:startYear IS NULL AND :endYear IS NOT NULL AND ( "
    // +"                                                               (s.childtype = 'movie' AND m.releaseDate IS NOT NULL AND YEAR(m.releaseDate) <= :endYear) "
    // +"                                                               OR (s.childtype = 'tv' AND YEAR(t.lastAirDate) <= :endYear) "
    // +"                                                          ) "
    // +"         ) "
    // +"    )")
    // Page<ServieDtoHomePage> findByAllFilters(@Param("userId") Integer userId,
    //                                                     @Param("type") String type,
    //                                                     @Param("watched") Boolean watched,
    //                                                     @Param("languages") List<String> languages,
    //                                                     @Param("statuses") List<String> statuses,
    //                                                     @Param("startYear") Integer startYear,
    //                                                     @Param("endYear") Integer endYear,
    //                                                     Pageable pageable);

    @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage(s.imdbId, s.tmdbId, s.childtype, s.title, s.posterPath, m.releaseDate, t.numberOfEpisodes, t.firstAirDate, t.lastAirDate, usd.episodesWatched, usd.completed) FROM Servie AS s "+"LEFT JOIN Movie AS m ON s.childtype = m.childtype AND s.tmdbId = m.tmdbId "+"LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId "+"JOIN UserServieData AS usd ON s.childtype = usd.childtype AND s.tmdbId = usd.tmdbId "+"WHERE usd.userId = :userId "+"AND (:type = '' OR s.childtype = :type) "+"AND (:watched IS NULL OR usd.completed = :watched) "+"AND (:languages IS NULL OR s.originalLanguage IN :languages) "+"AND (:statuses IS NULL OR s.status IN :statuses) "+"AND ( "+"      (:startYear IS NULL AND :endYear IS NULL) "+"      OR (:startYear IS NOT NULL AND :endYear IS NOT NULL AND ( "
            +"                                                               (s.childtype = 'movie' AND m.releaseDate IS NOT NULL AND YEAR(m.releaseDate) BETWEEN :startYear AND :endYear) "+"                                                               OR (s.childtype = 'tv' AND YEAR(t.firstAirDate) >= :startYear AND YEAR(t.lastAirDate) <= :endYear) "+"                                                              ) "+"         ) "+"      OR (:startYear IS NOT NULL AND :endYear IS NULL AND ( "+"                                                               (s.childtype = 'movie' AND (m.releaseDate IS NULL OR YEAR(m.releaseDate) >= :startYear)) "+"                                                               OR (s.childtype = 'tv' AND YEAR(t.firstAirDate) >= :startYear) "+"                                                          ) "+"         ) "+"      OR (:startYear IS NULL AND :endYear IS NOT NULL AND ( "
            +"                                                               (s.childtype = 'movie' AND m.releaseDate IS NOT NULL AND YEAR(m.releaseDate) <= :endYear) "+"                                                               OR (s.childtype = 'tv' AND YEAR(t.lastAirDate) <= :endYear) "+"                                                          ) "+"         ) "+"    )")
    Page<ServieDtoHomePage> findByAllFilters(@Param("userId") Integer userId, @Param("type") String type, @Param("watched") Boolean watched, @Param("languages") List<String> languages, @Param("statuses") List<String> statuses, @Param("startYear") Integer startYear, @Param("endYear") Integer endYear, Pageable pageable);

    @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsSeriesPageDtos.SeriesDtoSeriesPage(s.imdbId, s.tmdbId, s.childtype, s.title, s.tagline, s.overview, s.backdropPath, m.releaseDate, t.numberOfEpisodes, usd.episodesWatched, usd.completed) FROM Servie AS s "+"LEFT JOIN Movie AS m ON s.childtype = m.childtype AND s.tmdbId = m.tmdbId "+"LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId "+"JOIN UserServieData AS usd ON s.childtype = usd.childtype AND s.tmdbId = usd.tmdbId "+"WHERE usd.userId = :userId AND s.childtype = :type AND s.tmdbId = :tmdbId")
    SeriesDtoSeriesPage findServieByUserId(@Param("userId") Integer userId, @Param("type") String type, @Param("tmdbId") Integer tmdbId);

    @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsSeriesPageDtos.GenreDtoSeriesPage(g.id, g.name) FROM Genre g JOIN g.servies s WHERE s.imdbId = :imdbId")
    Set<GenreDtoSeriesPage> getGenresFromServie(@Param("imdbId") String imdbId);

    Servie findByImdbId(String servieId);
}