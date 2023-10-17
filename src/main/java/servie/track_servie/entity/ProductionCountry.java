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
public class ProductionCountry
{
	@Id
	@Column(name = "iso_3166_1")
	@JsonProperty("iso_3166_1")
	private String iso;
	// ---------------------------------------------------------------
	@Column(name = "name")
	@JsonProperty("name")
	private String name;
	// ---------------------------------------------------------------
	@ManyToMany(mappedBy = "productionCountries")
	private Set<Servie> servies;
}