package servie.track_servie.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Season
{
    @Id
    @JsonProperty("id")
    @Column(name = "id")
    private String id;
    // ---------------------------------------------------------------
    @Column(name = "name")
    private String name;
    // ---------------------------------------------------------------
    @JsonProperty("episode_count")
    @Column(name = "episode_count")
    private Integer episodeCount;
    // ---------------------------------------------------------------
    @ManyToOne
    private Series series;
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
    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    List<Episode> episodes;
}
