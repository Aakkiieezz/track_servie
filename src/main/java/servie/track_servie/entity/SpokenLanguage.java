package servie.track_servie.entity;

import java.util.Objects;
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

	@Override
	public boolean equals(Object o)
	{
		if(this==o)
			return true;
		if(o==null || getClass()!=o.getClass())
			return false;
		SpokenLanguage spokenLanguage = (SpokenLanguage) o;
		return Objects.equals(iso, spokenLanguage.iso);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(iso);
	}
}