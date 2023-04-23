package servie.track_servie.entities;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
import jakarta.persistence.Id;
// import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;

// @Entity
public class UserServieData
{
    @Column(name = "user_id")
    @Id
    private Integer userId;
    // ---------------------------------------------------------------
    @Column(name = "watched", nullable = false)
    private Boolean watched = false;
    // ---------------------------------------------------------------
    @Column(name = "poster_path")
    private String posterPath;
    // ---------------------------------------------------------------
    @Column(name = "backdrop_path")
    private String backdropPath;
    // ---------------------------------------------------------------
    @OneToMany(cascade = CascadeType.ALL)
    // @JoinTable(name = "user_series_user_seasons", joinColumns = @JoinColumn(name = "tmdb_id", referencedColumnName = "tmdb_id"), inverseJoinColumns = @JoinColumn(name = "seasons_id"))
    private List<UserSeasonData> seasons;
}
