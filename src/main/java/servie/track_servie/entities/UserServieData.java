package servie.track_servie.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;
import servie.track_servie.payload.primaryKeys.UserServieKey;

@Data
@Entity
@IdClass(UserServieKey.class)
public class UserServieData
{
    @Column(name = "user_id")
    @Id
    private Integer userId;
    // ---------------------------------------------------------------
    @Id
    private Integer tmdbId;
    // ---------------------------------------------------------------
    @Id
    private String childtype;
    // ---------------------------------------------------------------
    @Column(name = "movie_watched", nullable = false)
    private Boolean movieWatched = false;
    // ---------------------------------------------------------------
    @Column(name = "poster_path")
    private String posterPath;
    // ---------------------------------------------------------------
    @Column(name = "backdrop_path")
    private String backdropPath;
}
