package servie.track_servie.entity.credits;

import java.util.Objects;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import servie.track_servie.entity.Episode;

@Data
@Entity
@Table(name = "ecrew")
public class EpisodeCrew
{
	@Id
	@Column(name = "credit_id")
	@JsonProperty("credit_id")
	private String creditId;
	// ---------------------------------------------------------------
	@Column(name = "job")
	@JsonProperty("job")
	private String job;
	// ---------------------------------------------------------------
	@Column(name = "department")
	@JsonProperty("department")
	private String department;
	// ---------------------------------------------------------------
	@Column(name = "adult")
	@JsonProperty("adult")
	private boolean adult;
	// ---------------------------------------------------------------
	@Column(name = "gender")
	@JsonProperty("gender")
	private Integer gender;
	// ---------------------------------------------------------------
	@Column(name = "person_id")
	@JsonProperty("id")
	private Integer personId;
	// ---------------------------------------------------------------
	@JsonProperty("known_for_department")
	@Column(name = "knownForDepartment")
	private String knownForDepartment;
	// ---------------------------------------------------------------
	@Column(name = "name")
	@JsonProperty("name")
	private String name;
	// ---------------------------------------------------------------
	@Column(name = "original_name")
	@JsonProperty("original_name")
	private String originalName;
	// ---------------------------------------------------------------
	@Column(name = "popularity")
	@JsonProperty("popularity")
	private Double popularity;
	// ---------------------------------------------------------------
	@Column(name = "profile_path")
	@JsonProperty("profile_path")
	private String profilePath;
	// ---------------------------------------------------------------
	@ManyToMany(mappedBy = "crew")
	private Set<Episode> episodes;

	@Override
	public boolean equals(Object o)
	{
		if(this==o)
		{
			return true;
		}
		if(o==null || getClass()!=o.getClass())
		{
			return false;
		}
		EpisodeCrew that = (EpisodeCrew) o;
		return Objects.equals(creditId, that.creditId);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(creditId);
	}
}
