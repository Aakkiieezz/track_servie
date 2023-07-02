package servie.track_servie.repository.tmdbExports;

// import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import servie.track_servie.entity.tmdbExports.TmdbMovieExports;

@Repository
public interface TmdbMovieExportsRepository extends JpaRepository<TmdbMovieExports, Integer>
{}