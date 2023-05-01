package servie.track_servie.entities;

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
    @Id
    @Column(name = "id")
    private String id;
    // ---------------------------------------------------------------
    @Column(name = "name")
    private String name;
    // ---------------------------------------------------------------
    @Column(name = "tmdb_id")
    private Integer tmdbId;
    // ---------------------------------------------------------------
    @JsonProperty("episode_count")
    @Column(name = "episode_count")
    private Integer episodeCount;
    // ---------------------------------------------------------------
    // ??? Once try SQL's IF statement
    // Replacing
    // (SELECT COUNT(*) FROM episode e WHERE e.tmdb_id = tmdb_id AND e.season_number = season_number AND e.watched = 1)
    // with
    // (SELECT COUNT(*) FROM user_episode_data ued WHERE ued.tmdb_id = tmdb_id AND ued.season_number = season_number AND ued.watched = 1)
    @Formula(value = "(SELECT CASE WHEN (SELECT COUNT(*) FROM user_episode_data ued WHERE ued.tmdb_id = tmdb_id AND ued.season_number = season_number AND ued.watched = 1) = episode_count THEN true ELSE false END)")
    private Boolean watched = false;
    // ---------------------------------------------------------------
    // Replacing
    // (SELECT COUNT(*) FROM episode e WHERE e.tmdb_id = tmdb_id AND e.season_number = season_number AND e.watched=1)
    // with
    // (SELECT COUNT(*) FROM user_episode_data ued WHERE ued.tmdb_id = tmdb_id AND ued.season_number = season_number AND ued.watched=1)
    @Formula(value = "(SELECT COUNT(*) FROM user_episode_data ued WHERE ued.tmdb_id = tmdb_id AND ued.season_number = season_number AND ued.watched=1)")
    private Integer episodesWatched;
    // ---------------------------------------------------------------
    @Column(name = "overview", length = 10000)
    private String overview;
    // ---------------------------------------------------------------
    @JsonProperty("poster_path")
    @Column(name = "poster_path")
    private String posterPath;
    // ---------------------------------------------------------------
    @JsonProperty("season_number")
    @Column(name = "season_number")
    private Integer seasonNumber;
    // ---------------------------------------------------------------
    @OneToMany(cascade = CascadeType.ALL)
    List<Episode> episodes;
}
