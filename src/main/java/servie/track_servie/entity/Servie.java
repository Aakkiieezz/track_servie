package servie.track_servie.entity;

import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import servie.track_servie.payload.primaryKeys.ServieKey;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@IdClass(ServieKey.class)
@Table(name = "servie")
public class Servie
{
    @Id
    @JsonProperty("id")
    @Column(name = "tmdb_id")
    private Integer tmdbId;
    // ---------------------------------------------------------------
    @Id
    @Column(name = "childtype", insertable = false, updatable = false)
    private String childtype;
    // ---------------------------------------------------------------
    @JsonProperty("imdb_id")
    @Column(name = "imdb_id")
    private String imdbId;
    // ---------------------------------------------------------------
    @JsonAlias({"title", "name"})
    @Column(name = "title")
    private String title;
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
    @JsonProperty("popularity")
    @Column(name = "popularity")
    private Double popularity;
    // ---------------------------------------------------------------
    @OneToMany(mappedBy = "servie", cascade = CascadeType.REMOVE)
    private List<UserServieData> userServies;
    // ---------------------------------------------------------------
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Genre> genres;
    // ---------------------------------------------------------------
    @OneToMany(cascade = CascadeType.REMOVE)
    List<Cast> casts;
}
