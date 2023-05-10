package servie.track_servie.entities;

import java.time.LocalDate;
import java.util.List;
import org.hibernate.annotations.Formula;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.PrimaryKeyJoinColumns;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@DiscriminatorValue(value = "tv")
@PrimaryKeyJoinColumns({@PrimaryKeyJoinColumn(name = "tmdbId", referencedColumnName = "tmdb_id"), @PrimaryKeyJoinColumn(name = "childtype", referencedColumnName = "childtype")})
@Table(name = "series")
public class Series extends Servie
{
    @JsonProperty("number_of_seasons")
    @Column(name = "number_of_seasons")
    private Integer numberOfSeasons;
    // ---------------------------------------------------------------
    @JsonProperty("number_of_episodes")
    @Column(name = "number_of_episodes")
    private Integer numberOfEpisodes;
    // ---------------------------------------------------------------
    @Formula(value = "(SELECT CASE WHEN (SELECT COUNT(*) FROM user_episode_data AS ued WHERE ued.tmdb_id = tmdb_id AND ued.watched = 1) = number_of_episodes THEN true ELSE false END)")
    private Boolean watched = false;
    // ---------------------------------------------------------------
    @JsonProperty("first_air_date")
    @Column(name = "first_air_date")
    private LocalDate firstAirDate;
    // ---------------------------------------------------------------
    @JsonProperty("last_air_date")
    @Column(name = "last_air_date")
    private LocalDate lastAirDate;
    // ---------------------------------------------------------------
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "series_seasons", joinColumns = @JoinColumn(name = "tmdb_id", referencedColumnName = "tmdb_id"), inverseJoinColumns = @JoinColumn(name = "seasons_id"))
    private List<Season> seasons;
}
