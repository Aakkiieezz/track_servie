package servie.track_servie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import servie.track_servie.entity.people.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>
{}
