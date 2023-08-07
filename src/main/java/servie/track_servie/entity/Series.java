package servie.track_servie.entity;

import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
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
    // // @JsonProperty("id")
    // // @Column(name = "tmdb_id")
    // private Integer tmdbId;
    // // ---------------------------------------------------------------
    // // @Id
    // // @Column(name = "childtype", insertable = false, updatable = false)
    // private String childtype;
    // // ---------------------------------------------------------------
    // // ---------------------------------------------------------------
    // // ---------------------------------------------------------------
    @JsonProperty("number_of_seasons")
    @Column(name = "number_of_seasons")
    private Integer numberOfSeasons;
    // ---------------------------------------------------------------
    @JsonProperty("number_of_episodes")
    @Column(name = "number_of_episodes")
    private Integer numberOfEpisodes;
    // ---------------------------------------------------------------
    @JsonProperty("first_air_date")
    @Column(name = "first_air_date")
    private LocalDate firstAirDate;
    // ---------------------------------------------------------------
    @JsonProperty("last_air_date")
    @Column(name = "last_air_date")
    private LocalDate lastAirDate;
    // ---------------------------------------------------------------
    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL)
    private List<Season> seasons;
}
