package servie.track_servie.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import servie.track_servie.entities.Genre;
import servie.track_servie.entities.Servie;
import servie.track_servie.payload.primaryKeys.ServieKey;

@Repository
public interface ServieRepository extends JpaRepository<Servie, ServieKey>
{
    // Returns list of Servies which consists the Watched filter
    Page<Servie> findByCompleted(Boolean watched, Pageable pageable);

    // Returns list of Servies which consists all the mentioned Genres
    @Query("SELECT s FROM Servie s JOIN s.genres g WHERE g IN :genreList GROUP BY s HAVING COUNT(DISTINCT g) = :genreListSize")
    Page<Servie> findByGenres(@Param("genreList") List<Genre> genres, @Param("genreListSize") long genreListSize, Pageable pageable);

    // @Query(value = "SELECT DISTINCT m.* FROM servie m "+"JOIN servies_genres mg ON m.imdb_id = mg.imdb_id "+"WHERE mg.genres_id IN :genreIds "+"GROUP BY m.imdb_id "+"HAVING COUNT(DISTINCT mg.genres_id) = :genreListSize", nativeQuery = true)
    // Page<Servie> findByGenreIds(@Param("genreIds") List<Integer> genreIds, @Param("genreListSize") long genreListSize, Pageable pageable);
    // 
    // Returns list of Servies which consists the Watched filter & all the mentioned Genres
    @Query(value = "SELECT s FROM Servie AS s LEFT JOIN s.genres sk WHERE s.completed=:watched AND sk IN :genreList GROUP BY s HAVING COUNT( sk) = :genreListSize")
    Page<Servie> findByCompletedAndGenres(@Param("watched") Boolean watched, @Param("genreList") List<Genre> genres, @Param("genreListSize") long genreListSize, Pageable pageable);

    Servie findByImdbId(String servieId);

    @Query(value = "SELECT s FROM Servie AS s INNER JOIN UserServieData AS usd ON s.childtype = usd.childtype AND s.tmdbId = usd.tmdbId WHERE usd.userId=:userId")
    Page<Servie> findAllMoviesByUserId(@Param("userId") Integer userId, Pageable pageable);
}