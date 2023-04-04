package servie.track_servie.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
// import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable; // ??? which pageable ?
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import servie.track_servie.entity.Genre;
import servie.track_servie.entity.Key;
import servie.track_servie.entity.Servie;

@Repository
public interface ServieRepository extends JpaRepository<Servie, Key>
{
    // Returns list of Series which consists the Watched filter
    Page<Servie> findByCompleted(Boolean watched, Pageable pageable);

    // Returns list of Series which consists all the mentioned Genres
    // @Query(value = "SELECT s FROM Servie AS s LEFT JOIN s.genres sk WHERE sk IN :genreList"+" GROUP BY s HAVING COUNT( sk) = :genreListSize")
    // @Query(value = "SELECT s FROM Servie s LEFT JOIN s.genres gl LEFT JOIN gl.genre g WHERE g IN :genreList GROUP BY s HAVING COUNT(DISTINCT g) = :genreListSize")
    @Query("SELECT s FROM Servie s JOIN s.genres g WHERE g IN :genreList GROUP BY s HAVING COUNT(DISTINCT g) = :genreListSize")
    Page<Servie> findByGenres(@Param("genreList") List<Genre> genres, @Param("genreListSize") long genreListSize, Pageable pageable);
    // @Query("SELECT s from servie s left join movie m on s.id=m.imdbId left join series t on s.id=t.id left JOIN s.genres g WHERE g IN :genreList GROUP BY s HAVING COUNT(DISTINCT g) = :genreListSize")
    // from servie s left join movie m on s.id=m.imdbId left join series t on s.id=t.id
    // @Query("SELECT s FROM Servie s JOIN s.genres g WHERE g IN :genres GROUP BY s HAVING COUNT(DISTINCT g) = :count")
    // List<Show> findByGenres(@Param("genres") List<Genre> genres, @Param("count") Long count);

    // Returns list of Series which consists the Watched filter & all the mentioned Genres
    // ??? to yet understand
    @Query(value = "SELECT s FROM Servie AS s LEFT JOIN s.genres sk WHERE s.completed=:watched AND sk IN :genreList"+" GROUP BY s HAVING COUNT( sk) = :genreListSize")
    Page<Servie> findByCompletedAndGenres(@Param("watched") Boolean watched, @Param("genreList") List<Genre> genres, @Param("genreListSize") long genreListSize, Pageable pageable);

    Servie findByImdbId(String servieId);
}
