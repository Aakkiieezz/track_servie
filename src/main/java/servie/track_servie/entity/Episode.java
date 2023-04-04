package servie.track_servie.entity;

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
    @Column(name = "episode_number")
    @JsonProperty("episode_number")
    private Integer episodeNumber;
    // ---------------------------------------------------------------
    @Column(name = "season_number")
    @JsonProperty("season_number")
    private Integer seasonNumber;
    // ---------------------------------------------------------------
    @Column(name = "runtime")
    private int runtime;
    // ---------------------------------------------------------------
    @Column(name = "overview", length = 10000)
    private String overview;
    // ---------------------------------------------------------------
    @Column(name = "still_path")
    @JsonProperty("still_path")
    private String stillPath;
    // ---------------------------------------------------------------
    @Column(name = "tmdb_id")
    @JsonProperty("show_id")
    private Integer tmdbId;
}