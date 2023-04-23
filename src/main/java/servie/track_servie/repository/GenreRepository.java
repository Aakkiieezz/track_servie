package servie.track_servie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import servie.track_servie.entities.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer>
{}
