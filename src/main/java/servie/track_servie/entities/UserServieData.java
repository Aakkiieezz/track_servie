package servie.track_servie.entities;

import org.hibernate.annotations.Formula;
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
    @Column(name = "tmdb_id")
    @Id
    private Integer tmdbId;
    // ---------------------------------------------------------------
    @Column(name = "childtype")
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
    // ---------------------------------------------------------------
    @Formula(value = "(CASE WHEN childtype = 'movie' THEN movie_watched ELSE (SELECT CASE WHEN (SELECT COUNT(*) FROM user_episode_data AS ued WHERE ued.tmdb_id = tmdb_id AND ued.watched = 1 AND ued.user_id = user_id) = (SELECT s.number_of_episodes FROM series AS s WHERE s.tmdb_id = tmdb_id) THEN true ELSE false END) END)")
    private Boolean completed = false;
    // ---------------------------------------------------------------
    @Formula(value = "(SELECT COUNT(*) FROM user_episode_data AS ued WHERE ued.tmdb_id = tmdb_id AND ued.watched = 1 AND ued.user_id = user_id)")
    private Integer episodesWatched;
}
