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
@Table(name = "movie_collection")
public class MovieCollection
{
	@Id
	@Column(name = "id")
	private Integer id;
	// ---------------------------------------------------------------
	@Column(name = "name")
	private String name;
	// ---------------------------------------------------------------
	@Column(name = "overview", length = 10000)
	private String overview;
	// ---------------------------------------------------------------
	@Column(name = "poster_path")
	@JsonProperty("poster_path")
	private String posterPath;
	// ---------------------------------------------------------------
	@Column(name = "backdrop_path")
	@JsonProperty("backdrop_path")
	private String backdropPath;
	// ---------------------------------------------------------------
	@OneToMany(mappedBy = "collection", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonProperty("parts")
	private List<Movie> movies;
}
