package servie.track_servie.entity;

import java.time.LocalDateTime;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
	@Column(name = "tmdb_id")
	@JsonProperty("id")
	private Integer tmdbId;
	// ---------------------------------------------------------------
	@Id
	@Column(name = "childtype", insertable = false, updatable = false)
	private String childtype;
	// ---------------------------------------------------------------
	@Column(name = "imdb_id")
	@JsonProperty("imdb_id")
	private String imdbId;
	// ---------------------------------------------------------------
	@Column(name = "title")
	@JsonAlias({"title", "name"})
	private String title;
	// ---------------------------------------------------------------
	@Column(name = "original_language")
	@JsonProperty("original_language")
	private String originalLanguage;
	// ---------------------------------------------------------------
	@Column(name = "status")
	@JsonProperty("status")
	private String status;
	// ---------------------------------------------------------------
	@Column(name = "tagline")
	@JsonProperty("tagline")
	private String tagline;
	// ---------------------------------------------------------------
	@Column(name = "overview", length = 10000)
	@JsonProperty("overview")
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
	@Column(name = "popularity")
	@JsonProperty("popularity")
	private Double popularity;
	// ---------------------------------------------------------------
	@OneToMany(mappedBy = "servie", cascade = CascadeType.REMOVE)
	private List<UserServieData> userServies;
	// ---------------------------------------------------------------
	@Column(name = "adult")
	@JsonProperty("adult")
	private boolean adult;
	// ---------------------------------------------------------------
	@Column(name = "homepage")
	@JsonProperty("homepage")
	private String homepage;
	// ---------------------------------------------------------------
	@Column(name = "vote_average")
	@JsonProperty("vote_average")
	private Double voteAverage;
	// ---------------------------------------------------------------
	@Column(name = "vote_count")
	@JsonProperty("vote_count")
	private Integer voteCount;
	// ---------------------------------------------------------------
	@Column(name = "original_title")
	@JsonAlias({"original_title", "original_name"})
	private String originalTitle;
	// ---------------------------------------------------------------
	@Column(name = "last_modified", columnDefinition = "DATETIME")
	private LocalDateTime lastModified;
	// ---------------------------------------------------------------
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(	name = "servie_genres",
				joinColumns = {@JoinColumn(name = "tmdb_id", referencedColumnName = "tmdb_id"),
						@JoinColumn(name = "childtype", referencedColumnName = "childtype")},
				inverseJoinColumns = @JoinColumn(name = "genre_id"))
	private Set<Genre> genres;
	// ---------------------------------------------------------------
	// PROBABLY NOT REQUIRED
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(	name = "servie_production_companies",
				joinColumns = {@JoinColumn(name = "tmdb_id", referencedColumnName = "tmdb_id"),
						@JoinColumn(name = "childtype", referencedColumnName = "childtype")},
				inverseJoinColumns = @JoinColumn(name = "production_company_id"))
	@JsonProperty("production_companies")
	private Set<ProductionCompany> productionCompanies;
	// ---------------------------------------------------------------
	// PROBABLY NOT REQUIRED
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(	name = "servie_production_countries",
				joinColumns = {@JoinColumn(name = "tmdb_id", referencedColumnName = "tmdb_id"),
						@JoinColumn(name = "childtype", referencedColumnName = "childtype")},
				inverseJoinColumns = @JoinColumn(name = "iso"))
	@JsonProperty("production_countries")
	private Set<ProductionCountry> productionCountries;
	// ---------------------------------------------------------------
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(	name = "servie_spoken_languages",
				joinColumns = {@JoinColumn(name = "tmdb_id", referencedColumnName = "tmdb_id"),
						@JoinColumn(name = "childtype", referencedColumnName = "childtype")},
				inverseJoinColumns = @JoinColumn(name = "iso", referencedColumnName = "iso_639_1"))
	@JsonProperty("spoken_languages")
	private Set<SpokenLanguage> spokenLanguages;
}