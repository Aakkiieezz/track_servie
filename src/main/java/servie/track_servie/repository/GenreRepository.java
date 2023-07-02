package servie.track_servie.repository;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import servie.track_servie.entity.Genre;
import servie.track_servie.payload.dtos.operationsServiePageDtos.GenreDtoServiePage;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer>
{
    @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsServiePageDtos.GenreDtoServiePage(g.id, g.name) FROM Genre g"+" JOIN g.servies s"+" WHERE s.childtype = :childtype AND s.tmdbId = :tmdbId")
    Set<GenreDtoServiePage> getGenresForServiePage(@Param("tmdbId") Integer tmdbId, @Param("childtype") String childtype);
}
