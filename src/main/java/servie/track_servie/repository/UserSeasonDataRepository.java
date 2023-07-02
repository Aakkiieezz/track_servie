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
    @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsServiePageDtos.SeasonDtoServiePage(season.id, season.name, season.seasonNumber, season.posterPath, season.episodeCount, usd2.episodesWatched, usd2.watched) FROM Servie AS s"+" LEFT JOIN Series AS t ON s.childtype = t.childtype AND s.tmdbId = t.tmdbId"+" JOIN Season AS season ON season.series = t"+" JOIN UserServieData AS usd ON usd.servie = s"+" JOIN UserSeasonData AS usd2 ON usd2.userServieData = usd"+" WHERE usd2.seasonNumber = season.seasonNumber AND usd.user = :user AND s.childtype = :childtype AND s.tmdbId = :tmdbId")
    List<SeasonDtoServiePage> getSeasons(@Param("user") User user, @Param("childtype") String childtype, @Param("tmdbId") Integer tmdbId);
}