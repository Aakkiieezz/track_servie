package servie.track_servie.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;
import servie.track_servie.payload.primaryKeys.UserEpisodeKey;

@Data
@Entity
@IdClass(UserEpisodeKey.class)
public class UserEpisodeData
{
    @Column(name = "episode_id")
    @Id
    private String episodeId;
    // ---------------------------------------------------------------
    @Column(name = "episode_number")
    private Integer episodeNumber;
    // ---------------------------------------------------------------
    @Column(name = "user_id")
    @Id
    private Integer userId;
    // ---------------------------------------------------------------
    @Column(name = "tmdb_id")
    @Id
    private Integer tmdbId;
    // ---------------------------------------------------------------
    @Column(name = "season_number")
    @Id
    private Integer seasonNumber;
    // ---------------------------------------------------------------
    @Column(name = "watched", nullable = false)
    private Boolean watched = false;
}
