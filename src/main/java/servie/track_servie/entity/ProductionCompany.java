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
public class ProductionCompany
{
	@Id
	@Column(name = "id")
	@JsonProperty("id")
	private Integer id;
	// ---------------------------------------------------------------
	@Column(name = "name", nullable = false)
	@JsonProperty("name")
	private String name;
	// ---------------------------------------------------------------
	@Column(name = "logo_path")
	@JsonProperty("logo_path")
	private String logoPath;
	// ---------------------------------------------------------------
	@Column(name = "origin_country")
	@JsonProperty("origin_country")
	private String originCountry;
	// ---------------------------------------------------------------
	@ManyToMany(mappedBy = "productionCompanies")
	private Set<Servie> servies;

	@Override
	public boolean equals(Object o)
	{
		if(this==o)
			return true;
		if(o==null || getClass()!=o.getClass())
			return false;
		ProductionCompany productionCompany = (ProductionCompany) o;
		return Objects.equals(id, productionCompany.id);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id);
	}
}