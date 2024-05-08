package servie.track_servie.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import servie.track_servie.entity.credits.EpisodeCrew;
import servie.track_servie.entity.credits.EpisodeCast;

@Data
@Entity
public class Episode
{
	@Id
	@Column(name = "id")
	@JsonProperty("id")
	private String id;
	// ---------------------------------------------------------------
	@Column(name = "name", nullable = false)
	@JsonProperty("name")
	private String name;
	// ---------------------------------------------------------------
	@Column(name = "episode_no", nullable = false)
	@JsonProperty("episode_number")
	private Integer episodeNo;
	// ---------------------------------------------------------------
	@Column(name = "season_no", nullable = false)
	@JsonProperty("season_number")
	private Integer seasonNo;
	// ---------------------------------------------------------------
	@Column(name = "runtime")
	@JsonProperty("runtime")
	private Integer runtime;
	// ---------------------------------------------------------------
	@Column(name = "overview", length = 10000)
	@JsonProperty("overview")
	private String overview;
	// ---------------------------------------------------------------
	@Column(name = "still_path")
	@JsonProperty("still_path")
	private String stillPath;
	// ---------------------------------------------------------------
	@Column(name = "tmdb_id", nullable = false)
	@JsonProperty("show_id")
	private Integer tmdbId;
	// ---------------------------------------------------------------
	@ManyToOne
	private Season season;
	// ---------------------------------------------------------------
	@Column(name = "air_date")
	@JsonProperty("air_date")
	private LocalDate airDate;
	// ---------------------------------------------------------------
	@Column(name = "type")
	@JsonProperty("episode_type")
	private String type;
	// ---------------------------------------------------------------
	@Column(name = "vote_average")
	@JsonProperty("vote_average")
	private Double voteAverage;
	// ---------------------------------------------------------------
	@Column(name = "vote_count")
	@JsonProperty("vote_count")
	private Integer voteCount;
	// ---------------------------------------------------------------
	@Column(name = "production_code")
	@JsonProperty("production_code")
	private String productionCode;
	// ---------------------------------------------------------------
	@Column(name = "last_modified", nullable = false, columnDefinition = "DATETIME")
	private LocalDateTime lastModified;
	// ---------------------------------------------------------------
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JsonProperty("guest_stars")
	@JoinTable(	name = "episode_cast",
				joinColumns = @JoinColumn(name = "episode_id"),
				inverseJoinColumns = @JoinColumn(name = "credit_id"))
	private Set<EpisodeCast> guestStars; // akash TO FIX, WHY EPISODE DELETEION IS NOT DELETING CAST AND CREW
	// ---------------------------------------------------------------
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JsonProperty("crew")
	@JoinTable(	name = "episode_crew",
				joinColumns = @JoinColumn(name = "episode_id"),
				inverseJoinColumns = @JoinColumn(name = "credit_id"))
	private Set<EpisodeCrew> crew;

	@Override
	public boolean equals(Object o)
	{
		if(this==o)
			return true;
		if(o==null || getClass()!=o.getClass())
			return false;
		Episode episode = (Episode) o;
		return Objects.equals(id, episode.id);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id);
	}

	@Override
	public String toString()
	{
		return "Season {id="+id+'}';
	}
}