package servie.track_servie.entity;

import java.time.LocalDate;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.PrimaryKeyJoinColumns;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import servie.track_servie.entity.credits.MovieCast;
import servie.track_servie.entity.credits.MovieCrew;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@DiscriminatorValue(value = "movie")
@PrimaryKeyJoinColumns({@PrimaryKeyJoinColumn(name = "tmdb_id", referencedColumnName = "tmdb_id"), @PrimaryKeyJoinColumn(name = "childtype", referencedColumnName = "childtype")})
@Table(name = "movie")
public class Movie extends Servie
{
	@Column(name = "release_date")
	@JsonProperty("release_date")
	private LocalDate releaseDate;
	// ---------------------------------------------------------------
	@Column(name = "runtime")
	@JsonProperty("runtime")
	private Integer runtime;
	// ---------------------------------------------------------------
	@Embedded
	@JsonProperty("belongs_to_collection")
	private MovieCollectionInfo belongsToCollection;
	// ---------------------------------------------------------------
	@JoinColumn(name = "collection_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private MovieCollection collection;
	// ---------------------------------------------------------------
	@Column(name = "video")
	@JsonProperty("video")
	private boolean video; // Don't know exactly
	// ---------------------------------------------------------------
	@Column(name = "budget")
	@JsonProperty("budget")
	private Integer budget;
	// ---------------------------------------------------------------
	@Column(name = "revenue")
	@JsonProperty("revenue")
	private Integer revenue;
	// ---------------------------------------------------------------
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(	name = "movie_cast",
				joinColumns = {@JoinColumn(name = "tmdb_id", referencedColumnName = "tmdb_id"),
						@JoinColumn(name = "childtype", referencedColumnName = "childtype")},
				inverseJoinColumns = @JoinColumn(name = "credit_id"))
	private Set<MovieCast> cast;
	// ---------------------------------------------------------------
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(	name = "movie_crew",
				joinColumns = {@JoinColumn(name = "tmdb_id", referencedColumnName = "tmdb_id"),
						@JoinColumn(name = "childtype", referencedColumnName = "childtype")},
				inverseJoinColumns = @JoinColumn(name = "credit_id"))
	private Set<MovieCrew> crew;
}