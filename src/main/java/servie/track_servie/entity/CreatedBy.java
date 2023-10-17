package servie.track_servie.entity;

import java.util.Set;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Data
@Entity
public class CreatedBy
{
	@Id
	@Column(name = "credit_id")
	@JsonProperty("credit_id")
	private String creditId;
	// ---------------------------------------------------------------
	@Column(name = "name")
	@JsonProperty("name")
	private String name;
	// ---------------------------------------------------------------
	@Column(name = "gender")
	@JsonProperty("gender")
	private Integer gender;
	// ---------------------------------------------------------------
	@Column(name = "profile_path")
	@JsonProperty("profile_path")
	private String profilePath;
	// ---------------------------------------------------------------
	@ManyToMany(mappedBy = "createdBy")
	private Set<Series> series;
}