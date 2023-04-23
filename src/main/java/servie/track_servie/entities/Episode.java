package servie.track_servie.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@Entity
public class Episode
{
    @Column(name = "id")
    @Id
    private String id;
    // ---------------------------------------------------------------
    @Column(name = "name")
    private String name;
    // ---------------------------------------------------------------
    @Column(name = "watched", nullable = false)
    private Boolean watched = false;
    // ---------------------------------------------------------------
    @JsonProperty("episode_number")
    @Column(name = "episode_number")
    private Integer episodeNumber;
    // ---------------------------------------------------------------
    @JsonProperty("season_number")
    @Column(name = "season_number")
    private Integer seasonNumber;
    // ---------------------------------------------------------------
    @Column(name = "runtime")
    private int runtime;
    // ---------------------------------------------------------------
    @Column(name = "overview", length = 10000)
    private String overview;
    // ---------------------------------------------------------------
    @JsonProperty("still_path")
    @Column(name = "still_path")
    private String stillPath;
    // ---------------------------------------------------------------
    @JsonProperty("show_id")
    @Column(name = "tmdb_id")
    private Integer tmdbId;
}