package servie.track_servie.entity;

import java.util.List;
import org.hibernate.annotations.Formula;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Season
{
    @Column(name = "id")
    @Id
    private String id;
    // ---------------------------------------------------------------
    @Column(name = "name")
    private String name;
    // ---------------------------------------------------------------
    @Column(name = "tmdb_id")
    private Integer tmdbId;
    // ---------------------------------------------------------------
    @Column(name = "episode_count")
    @JsonProperty("episode_count")
    private Integer episodeCount;
    // ---------------------------------------------------------------
    // ??? Once try SQL's IF statement
    @Formula(value = "(SELECT CASE WHEN (SELECT COUNT(*) FROM episode e WHERE e.tmdb_id = tmdb_id AND e.season_number = season_number AND e.watched = 1) = episode_count THEN true ELSE false END)")
    private Boolean watched = false;
    // ---------------------------------------------------------------
    @Formula(value = "(SELECT COUNT(*) FROM episode e WHERE e.tmdb_id = tmdb_id AND e.season_number = season_number AND e.watched=1)")
    private Integer episodesWatched;
    // ---------------------------------------------------------------
    @Column(name = "overview", length = 10000)
    private String overview;
    // ---------------------------------------------------------------
    @Column(name = "poster_path")
    @JsonProperty("poster_path")
    private String posterPath;
    // ---------------------------------------------------------------
    @Column(name = "season_number")
    @JsonProperty("season_number")
    private Integer seasonNumber;
    // ---------------------------------------------------------------
    @OneToMany(cascade = CascadeType.ALL)
    List<Episode> episodes;
}
