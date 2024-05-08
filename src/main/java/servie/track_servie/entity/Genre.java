package servie.track_servie.entity;

import java.util.Objects;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Genre
{
	@Id
	@Column(name = "id")
	private Integer id;
	// ---------------------------------------------------------------
	@Column(name = "name", nullable = false)
	private String name;
	// ---------------------------------------------------------------
	@ManyToMany(mappedBy = "genres")
	private Set<Servie> servies;

	public Genre(Integer id, String name)
	{
		this.id = id;
		this.name = name;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this==o)
			return true;
		if(o==null || getClass()!=o.getClass())
			return false;
		Genre genre = (Genre) o;
		return Objects.equals(id, genre.id);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id);
	}
}
