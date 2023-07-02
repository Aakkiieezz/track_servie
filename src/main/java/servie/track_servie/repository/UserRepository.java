package servie.track_servie.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import servie.track_servie.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>
{
    User findByEmail(String email);

    Optional<User> findOneByEmail(String email);
}
