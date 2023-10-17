package servie.track_servie.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class MovieCollectionInfo
{
	@Column(name = "id")
	@JsonProperty("id")
	private Integer collectionId;
	// ---------------------------------------------------------------
	@Column(name = "collection_name")
	@JsonProperty("name")
	private String collectionName;
	// ---------------------------------------------------------------
	@Column(name = "collection_poster_path")
	@JsonProperty("poster_path")
	private String posterPath;
	// ---------------------------------------------------------------
	@Column(name = "collection_backdrop_path")
	@JsonProperty("backdrop_path")
	private String collectionBackdropPath;
}
