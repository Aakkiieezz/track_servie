package servie.track_servie.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import servie.track_servie.entity.User;
import servie.track_servie.entity.UserServieData;
import servie.track_servie.entity.vault.VaultUserServie;
import servie.track_servie.payload.primaryKeys.UserServieDataKey;

@Repository
public interface UserServieDataRepository extends JpaRepository<UserServieData, UserServieDataKey>
{
        UserServieData findByUserId(Integer userId);

        @Query(value = "SELECT new servie.track_servie.entity.vault.VaultUserServie(s.tmdbId, s.title)"
                        +" FROM Servie AS s"
                        +" JOIN UserServieData AS usd ON s = usd.servie"
                        +" WHERE usd.user = :user AND s.childtype = 'movie'")
        List<VaultUserServie> getUserMovies(@Param("user") User user);

        @Query(value = "SELECT new servie.track_servie.entity.vault.VaultUserServie(s.tmdbId, s.title)"
                        +" FROM Servie AS s"
                        +" JOIN UserServieData AS usd ON s = usd.servie"
                        +" WHERE usd.user = :user AND s.childtype = 'tv'")
        List<VaultUserServie> getUserSeries(@Param("user") User user);

        List<UserServieData> findAllByUser(User user);

        // for modifying search results
        // @Query(value = "SELECT new servie.track_servie.payload.dtos.operationsSearch.SearchPageDtos.SeriesDtoSearchPage(usd.servie.tmdbId, usd.title, usd.posterPath, usd.childtype, usd.servie.numberOfEpisodes, usd.episodesWatched, usd.completed) FROM UserServieData usd WHERE usd.servie.tmdbId IN :tmdbIds AND usd.user = :user")
        @Query(value = "SELECT usd FROM UserServieData AS usd"
                        +" WHERE usd.servie.tmdbId IN :tmdbIds"
                        +" AND usd.servie.childtype = :childtype"
                        +" AND usd.user = :user")
        List<UserServieData> findByUserIdAndChildtypeAndTmdbIdIn(@Param("user") User user, @Param("childtype") String childtype, @Param("tmdbIds") List<Integer> tmdbIds);
}