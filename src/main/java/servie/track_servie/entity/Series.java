package servie.track_servie.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.PrimaryKeyJoinColumns;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@DiscriminatorValue(value = "tv")
@PrimaryKeyJoinColumns({@PrimaryKeyJoinColumn(name = "tmdb_id", referencedColumnName = "tmdb_id"), @PrimaryKeyJoinColumn(name = "childtype", referencedColumnName = "childtype")})
@Table(name = "series")
public class Series extends Servie
{
	@Column(name = "total_seasons", nullable = false)
	@JsonProperty("number_of_seasons")
	private Integer totalSeasons;
	// ---------------------------------------------------------------
	@Column(name = "total_episodes", nullable = false)
	@JsonProperty("number_of_episodes")
	private Integer totalEpisodes;
	// ---------------------------------------------------------------
	@Column(name = "first_air_date")
	@JsonProperty("first_air_date")
	private LocalDate firstAirDate;
	// ---------------------------------------------------------------
	@Column(name = "last_air_date")
	@JsonProperty("last_air_date")
	private LocalDate lastAirDate;
	// ---------------------------------------------------------------
	@OneToMany(mappedBy = "series", cascade = CascadeType.ALL)
	private List<Season> seasons;
	// ---------------------------------------------------------------
	@Column(name = "type")
	private String type;
	// ---------------------------------------------------------------
	@Column(name = "in_production")
	@JsonProperty("in_production")
	private boolean inProduction;
	// ---------------------------------------------------------------
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JsonProperty("created_by")
	@JoinTable(	name = "series_createdby",
				joinColumns = {@JoinColumn(name = "tmdb_id", referencedColumnName = "tmdb_id"),
						@JoinColumn(name = "childtype", referencedColumnName = "childtype")},
				inverseJoinColumns = @JoinColumn(name = "credit_id", referencedColumnName = "credit_id"))
	private Set<CreatedBy> createdBy;
	// ---------------------------------------------------------------
	// PROBABLY NOT REQUIRED
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(	name = "series_networks",
				joinColumns = {@JoinColumn(name = "tmdb_id", referencedColumnName = "tmdb_id"),
						@JoinColumn(name = "childtype", referencedColumnName = "childtype")},
				inverseJoinColumns = @JoinColumn(name = "id"))
	@JsonProperty("networks")
	private Set<Network> networks;
	// ---------------------------------------------------------------
	// @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@ElementCollection
	@CollectionTable(	name = "origin_country",
						joinColumns = {@JoinColumn(name = "tmdb_id", referencedColumnName = "tmdb_id"),
								@JoinColumn(name = "childtype", referencedColumnName = "childtype")})
	@Column(name = "origin_country")
	@JsonProperty("origin_country")
	private Set<String> originCountry;
	// ---------------------------------------------------------------
	// TO THINK IN FUTURE
	// List<Integer> episode_run_time // not required
	// List<String> languages         // not required
	// Object last_episode_to_air     // not required
	// Object next_episode_to_air     // not required

	@Override
	public String toString()
	{
		return "Series {id="+this.getTmdbId()+'}';
	}
}