package servie.track_servie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "movies_bkp")
public class ServieBkp
{
	@Id
	@Column(name = "tmdb_id")
	private Integer tmdbId;
}
