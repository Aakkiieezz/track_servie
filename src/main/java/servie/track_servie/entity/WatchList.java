package servie.track_servie.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import servie.track_servie.payload.primaryKeys.UserServieDataKey;

@Data
@Entity
@IdClass(UserServieDataKey.class)
@NoArgsConstructor
public class WatchList
{
	@Id
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	// ---------------------------------------------------------------
	@Id
	@ManyToOne
	@JoinColumn(name = "tmdb_id", referencedColumnName = "tmdb_id")
	@JoinColumn(name = "childtype", referencedColumnName = "childtype")
	private Servie servie;
}
