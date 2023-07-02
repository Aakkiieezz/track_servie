package servie.track_servie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import servie.track_servie.entity.MovieCollection;

@Repository
public interface MovieCollectionRepository extends JpaRepository<MovieCollection, Integer>
{}
