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
public class SpokenLanguage
{
	@Id
	@Column(name = "iso_639_1")
	@JsonProperty("iso_639_1")
	private String iso;
	// ---------------------------------------------------------------
	@Column(name = "name")
	@JsonProperty("name")
	private String name;
	// ---------------------------------------------------------------
	@Column(name = "eng_name")
	@JsonProperty("english_name")
	private String engName;
	// ---------------------------------------------------------------
	@ManyToMany(mappedBy = "spokenLanguages")
	private Set<Servie> servies;
}