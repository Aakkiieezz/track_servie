package servie.track_servie.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import servie.track_servie.entity.Servie;
import servie.track_servie.entity.User;
import servie.track_servie.entity.WatchList;
import servie.track_servie.payload.primaryKeys.UserServieDataKey;

@Repository
public interface ListRepository extends JpaRepository<WatchList, UserServieDataKey>
{
	Optional<WatchList> findByUserAndServie(User user, Servie servie);
}
