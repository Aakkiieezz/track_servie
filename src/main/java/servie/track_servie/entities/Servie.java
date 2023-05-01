package servie.track_servie.entities;

import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Formula;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import servie.track_servie.payload.primaryKeys.ServieKey;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "childtype")
@IdClass(ServieKey.class)
@Table(name = "servie", uniqueConstraints = @UniqueConstraint(columnNames = {"tmdb_id", "childtype"}))
public class Servie
{
    @Id
    @JsonProperty("imdb_id")
    @Column(name = "imdb_id")
    private String imdbId;
    // ---------------------------------------------------------------
    @Column(name = "childtype", insertable = false, updatable = false)
    private String childtype;
    // ---------------------------------------------------------------
    @JsonProperty("id")
    @Column(name = "tmdb_id")
    private Integer tmdbId;
    // ---------------------------------------------------------------
    @JsonAlias({"title", "name"})
    @Column(name = "title")
    private String title;
    // ---------------------------------------------------------------
    // ??? does not work when the field name is kept same as the child's field name "watched", don't know why ? (also remember to make changes in repo and service)
    // replacing
    // (SELECT * FROM movie m WHERE m.tmdb_id = tmdb_id AND m.watched = true)
    // with
    // (SELECT * FROM user_servie_data AS usd WHERE usd.tmdb_id = tmdb_id AND usd.movie_watched = true)
    // 
    // replacing
    // (SELECT COUNT(*) FROM episode e WHERE e.tmdb_id = tmdb_id AND e.watched = 1)
    // with
    // (SELECT COUNT(*) FROM user_episode_data ued WHERE ued.tmdb_id = tmdb_id AND ued.watched = 1)
    @Formula("(CASE WHEN EXISTS (SELECT * FROM user_servie_data AS usd WHERE usd.tmdb_id = tmdb_id AND usd.movie_watched = true) THEN true ELSE (SELECT CASE WHEN (SELECT COUNT(*) FROM user_episode_data ued WHERE ued.tmdb_id = tmdb_id AND ued.watched = 1) = (SELECT s.number_of_episodes FROM series s WHERE s.tmdb_id = tmdb_id) THEN true ELSE false END) END)")
    private Boolean completed = false;
    // ---------------------------------------------------------------
    @JsonProperty("original_language")
    @Column(name = "original_language")
    private String originalLanguage;
    // ---------------------------------------------------------------
    @Column(name = "status")
    private String status;
    // ---------------------------------------------------------------
    @Column(name = "tagline")
    private String tagline;
    // ---------------------------------------------------------------
    @Column(name = "overview", length = 10000)
    private String overview;
    // ---------------------------------------------------------------
    @JsonProperty("poster_path")
    @Column(name = "poster_path")
    private String posterPath;
    // ---------------------------------------------------------------
    @JsonProperty("backdrop_path")
    @Column(name = "backdrop_path")
    private String backdropPath;
    // ---------------------------------------------------------------
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "servies_genres", joinColumns = @JoinColumn(name = "imdb_id", referencedColumnName = "imdb_id"), inverseJoinColumns = @JoinColumn(name = "genres_id", referencedColumnName = "id"))
    private Set<Genre> genres = new HashSet<>();
}
