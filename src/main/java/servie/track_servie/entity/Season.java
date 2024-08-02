package servie.track_servie.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.hibernate.annotations.Formula;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import servie.track_servie.entity.credits.SeasonCast;

@Data
@Entity
public class Season
{
	@Id
	@Column(name = "id")
	@JsonProperty("id")
	private String id;
	// ---------------------------------------------------------------
	@Column(name = "name", nullable = false)
	private String name;
	// ---------------------------------------------------------------
	@Column(name = "episode_count")
	@JsonProperty("episode_count")
	private Integer episodeCount;
	// ---------------------------------------------------------------
	@ManyToOne
	@JoinColumn(name = "tmdb_id", referencedColumnName = "tmdb_id")
	@JoinColumn(name = "childtype", referencedColumnName = "childtype")
	private Series series;
	// ---------------------------------------------------------------
	@Column(name = "overview", length = 10000)
	private String overview;
	// ---------------------------------------------------------------
	@Column(name = "poster_path")
	@JsonProperty("poster_path")
	private String posterPath;
	// ---------------------------------------------------------------
	@Column(name = "season_no")
	@JsonProperty("season_number")
	private Integer seasonNo;
	// ---------------------------------------------------------------
	@OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
	List<Episode> episodes;
	// ---------------------------------------------------------------
	@Column(name = "_id")
	@JsonProperty("_id")
	private String id2;
	// ---------------------------------------------------------------
	@Column(name = "air_date")
	@JsonProperty("air_date")
	private LocalDate airDate;
	// ---------------------------------------------------------------
	@Column(name = "vote_average")
	@JsonProperty("vote_average")
	private Double voteAverage;
	// ---------------------------------------------------------------
	@Column(name = "last_modified", nullable = false, columnDefinition = "DATETIME")
	private LocalDateTime lastModified;
	// ---------------------------------------------------------------
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(	name = "season_cast",
				joinColumns = @JoinColumn(name = "season_id"),
				inverseJoinColumns = @JoinColumn(name = "credit_id"))
	private Set<SeasonCast> seasonCast;
	// ---------------------------------------------------------------
	@Formula(value = "(SELECT COALESCE(SUM(ep.runtime), 0) FROM episode AS ep"
			+" WHERE ep.tmdb_id = tmdb_id"
			+"   AND ep.season_no = season_no)")
	private int totalRuntime = 0;

	// ---------------------------------------------------------------
	@Override
	public boolean equals(Object o)
	{
		if(this==o)
			return true;
		if(o==null || getClass()!=o.getClass())
			return false;
		Season season = (Season) o;
		return Objects.equals(id, season.id);
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