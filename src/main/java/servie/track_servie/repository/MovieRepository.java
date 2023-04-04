package servie.track_servie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import servie.track_servie.entity.Key;
import servie.track_servie.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Key>
{}
