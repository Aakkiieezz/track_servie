package servie.track_servie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import servie.track_servie.entities.UserServieData;
import servie.track_servie.payload.primaryKeys.UserServieKey;

@Repository
public interface UserServieDataRepository extends JpaRepository<UserServieData, UserServieKey>
{
    UserServieData findByUserIdAndTmdbIdAndChildtype(Integer userId, Integer tmdbId, String childtype);
}
