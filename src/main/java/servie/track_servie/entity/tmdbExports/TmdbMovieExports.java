package servie.track_servie.entity.tmdbExports;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TmdbMovieExports
{
    @Id
    @JsonProperty("id")
    private Integer tmdbId;
    // ---------------------------------------------------------------
    @JsonProperty("adult")
    private Boolean adult;
    // ---------------------------------------------------------------
    @JsonProperty("original_title")
    private String originalTitle;
    // ---------------------------------------------------------------
    @JsonProperty("popularity")
    private Double popularity;
    // ---------------------------------------------------------------
    @JsonProperty("video")
    private Boolean video;
}
