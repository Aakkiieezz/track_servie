package servie.track_servie.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class MovieCollection
{
    @JsonProperty("id")
    @Column(name = "id")
    private Integer collectionId;
    // ---------------------------------------------------------------
    @JsonProperty("name")
    @Column(name = "collection_name")
    private String collectionName;
    // ---------------------------------------------------------------
    @JsonProperty("poster_path")
    @Column(name = "collection_poster_path")
    private String collectionPosterPath;
    // ---------------------------------------------------------------
    @JsonProperty("backdrop_path")
    @Column(name = "collection_backdrop_path")
    private String collectionBackdropPath;
}
