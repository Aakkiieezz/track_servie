package servie.track_servie.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @JsonProperty("release_date")
    @Column(name = "release_date")
    private String releaseDate;
    // ---------------------------------------------------------------
    @Column(name = "runtime")
    private Integer runtime;
    // ---------------------------------------------------------------
    @JsonProperty("belongs_to_collection")
    @Embedded
    private MovieCollection belongsToCollection;
    // ---------------------------------------------------------------
    // @Column(name = "collid")
    // private Integer collectionId;
    // ---------------------------------------------------------------
    // @Column(name = "collection_name")
    // private String collectionName;
    // ---------------------------------------------------------------
    // @Column(name = "collection_poster_path")
    // private String collectionPosterPath;
    // ---------------------------------------------------------------
    // @Column(name = "collection_backdrop_path")
    // private String collectionBackdropPath;
    // ---------------------------------------------------------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id")
    // @Column(name = "collection_id")
    private MovieCollectionDetails collection;
}