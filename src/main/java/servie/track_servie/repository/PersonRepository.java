package servie.track_servie.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import servie.track_servie.entity.people.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>
{
	@Query(value = "SELECT usd.tmdb_id, usd.childtype FROM mcast mc"
			+" JOIN movie_cast m_c ON m_c.credit_id = mc.credit_id"
			+" JOIN user_servie_data usd ON m_c.tmdb_id = usd.tmdb_id AND m_c.childtype = usd.childtype"
			+" WHERE mc.person_id = :personId"
			+"     AND usd.movie_watched = TRUE"
			+"     AND usd.user_id = :userId", nativeQuery = true)
	List<Object[]> getWatchedServiesOfPerson(Integer userId, Integer personId);
}
// SELECT usd.tmdb_id, usd.childtype FROM mcast mc
// JOIN movie_cast m_c ON m_c.credit_id = mc.credit_id
// JOIN user_servie_data usd ON m_c.tmdb_id = usd.tmdb_id AND m_c.childtype = usd.childtype
// WHERE user_id = 1
//     AND usd.movie_watched = TRUE
//     AND mc.person_id = 78245;