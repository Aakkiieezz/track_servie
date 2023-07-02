package servie.track_servie.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "collection")
public class MovieCollection
{
    @Column(name = "id")
    @Id
    private Integer id;
    // ---------------------------------------------------------------
    @Column(name = "name")
    private String name;
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
    @JsonProperty("parts")
    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Movie> movies;
}
