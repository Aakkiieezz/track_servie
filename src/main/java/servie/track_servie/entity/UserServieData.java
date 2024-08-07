package servie.track_servie.entity;

import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.Formula;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import lombok.NoArgsConstructor;
import servie.track_servie.payload.primaryKeys.UserServieDataKey;

@Data
@Entity
@IdClass(UserServieDataKey.class)
@NoArgsConstructor
public class UserServieData
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
	// ---------------------------------------------------------------
	@Column(name = "movie_watched", nullable = false)
	private Boolean movieWatched = false;
	// ---------------------------------------------------------------
	@Column(name = "poster_path")
	private String posterPath;
	// ---------------------------------------------------------------
	@Column(name = "backdrop_path")
	private String backdropPath;
	// ---------------------------------------------------------------
	@Formula(value = "(CASE WHEN childtype = 'movie' THEN movie_watched ELSE (SELECT CASE WHEN (SELECT COUNT(*) FROM user_episode_data AS ued"
			+" WHERE ued.user_id = user_id"
			+" AND ued.tmdb_id = tmdb_id"
			+" AND ued.childtype = childtype"
			+" AND ued.season_no > 0"
			+" AND ued.watched = 1) = (SELECT s.total_episodes FROM series AS s WHERE s.tmdb_id = tmdb_id) THEN true ELSE false END) END)")
	private Boolean completed = false;
	// ---------------------------------------------------------------
	@Formula(value = "(SELECT COUNT(*) FROM user_episode_data AS ued"
			+" WHERE ued.user_id = user_id"
			+" AND ued.tmdb_id = tmdb_id"
			+" AND ued.childtype = childtype"
			+" AND ued.season_no > 0"
			+" AND ued.watched = 1)")
	private Integer episodesWatched;
	// ---------------------------------------------------------------
	// NO USAGE
	// @Formula(value = "(SELECT SUM(e.runtime) FROM user_episode_data AS ued"
	// 		+" JOIN episode AS e"
	// 		+"   ON ued.tmdb_id = e.tmdb_id"
	// 		+"     AND ued.season_no = e.season_no"
	// 		+"     AND ued.episode_no = e.episode_no"
	// 		+" WHERE ued.user_id = user_id"
	// 		+"   AND ued.tmdb_id = tmdb_id"
	// 		+"   AND ued.watched = 1)")
	// private int totalWatchedRuntime;
	// ---------------------------------------------------------------
	@OneToMany(mappedBy = "userServieData", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<UserSeasonData> seasons;
	// ---------------------------------------------------------------
	@Column(name = "notes")
	private String notes;
	// ---------------------------------------------------------------
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@PrePersist
	protected void onCreate()
	{
		updatedAt = createdAt = LocalDateTime.now();
	}

	// ---------------------------------------------------------------
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@PreUpdate
	protected void onUpdate()
	{
		updatedAt = LocalDateTime.now();
	}

	// ---------------------------------------------------------------
	public UserServieData(User user, Servie servie)
	{
		this.user = user;
		this.servie = servie;
	}
}
