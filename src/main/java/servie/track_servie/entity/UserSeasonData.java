package servie.track_servie.entity;

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
import lombok.Data;
import lombok.NoArgsConstructor;
import servie.track_servie.payload.primaryKeys.UserSeasonDataKey;

@Data
@Entity
@NoArgsConstructor
@IdClass(UserSeasonDataKey.class)
public class UserSeasonData
{
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @JoinColumn(name = "tmdb_id", referencedColumnName = "tmdb_id")
    @JoinColumn(name = "childtype", referencedColumnName = "childtype")
    private UserServieData userServieData;
    // ---------------------------------------------------------------
    @Id
    @Column(name = "season_number")
    private Integer seasonNumber;
    // ---------------------------------------------------------------
    // @Column(name = "episode_count")
    // private Integer episodeCount;
    // ---------------------------------------------------------------
    @Formula(value = "(SELECT CASE WHEN (SELECT COUNT(*) FROM user_episode_data AS ued WHERE ued.user_id = user_id AND ued.tmdb_id = tmdb_id AND ued.season_number = season_number AND ued.watched = 1) = (SELECT s.episode_count FROM season AS s WHERE s.series_tmdb_id = tmdb_id AND s.season_number = season_number) THEN true ELSE false END)")
    private Boolean watched = false;
    // ---------------------------------------------------------------
    @Formula(value = "(SELECT COUNT(*) FROM user_episode_data AS ued WHERE ued.user_id = user_id AND ued.tmdb_id = tmdb_id AND ued.season_number = season_number AND ued.watched = 1)")
    private Integer episodesWatched;
    // ---------------------------------------------------------------
    @Column(name = "poster_path")
    private String posterPath;
    // ---------------------------------------------------------------
    @OneToMany(mappedBy = "userSeasonData", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<UserEpisodeData> episodes;

    public UserSeasonData(UserServieData userServieData, Integer seasonNumber)
    {
        this.userServieData = userServieData;
        this.seasonNumber = seasonNumber;
    }
}