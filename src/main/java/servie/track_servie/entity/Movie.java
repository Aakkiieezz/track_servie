package servie.track_servie.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.PrimaryKeyJoinColumns;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@DiscriminatorValue(value = "movie")
@PrimaryKeyJoinColumns({@PrimaryKeyJoinColumn(name = "tmdbId", referencedColumnName = "tmdb_id"), @PrimaryKeyJoinColumn(name = "childtype", referencedColumnName = "childtype")})
@Table(name = "movie")
public class Movie extends Servie
{
    @Column(name = "watched", nullable = false)
    private Boolean watched = false;
    // ---------------------------------------------------------------
    @Column(name = "release_date")
    @JsonProperty("release_date")
    private String releaseDate;
    // ---------------------------------------------------------------
    @Column(name = "runtime")
    private Integer runtime;
}