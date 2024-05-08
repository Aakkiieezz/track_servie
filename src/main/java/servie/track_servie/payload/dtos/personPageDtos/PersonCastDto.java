package servie.track_servie.payload.dtos.personPageDtos;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PersonCastDto
{
	// @JsonProperty("adult")
	// private boolean adult;
	// ---------------------------------------------------------------
	// @JsonProperty("backdrop_path")
	// private String backdropPath;
	// ---------------------------------------------------------------
	// "genre_ids": [28, 12, 878],
	// ---------------------------------------------------------------
	@JsonProperty("id")
	private Integer tmdbId;
	// ---------------------------------------------------------------
	// "origin_country" (only when mediaType = tv)
	// ---------------------------------------------------------------
	// "original_language": "en",
	// ---------------------------------------------------------------
	// @JsonAlias({"original_title", "original_name"})
	// private String originalTitle;
	// ---------------------------------------------------------------
	// "overview": "blah blah"
	// ---------------------------------------------------------------
	// "popularity": 35.994,
	// ---------------------------------------------------------------
	@JsonProperty("poster_path")
	private String posterPath;
	// ---------------------------------------------------------------
	// ??? when mediaType = movie
	@JsonProperty("release_date")
	private LocalDate releaseDate;
	// ---------------------------------------------------------------
	// ??? when mediaType = tv
	// ToDo FAKE
	private LocalDate firstAirDate = LocalDate.of(2000, 1, 1);
	// ---------------------------------------------------------------
	// ??? when mediaType = tv
	// ToDo not in paylaod
	// ToDo FAKE
	private LocalDate lastAirDate = LocalDate.of(2000, 1, 10);
	// ---------------------------------------------------------------
	@JsonAlias({"title", "name"})
	private String title;
	// ---------------------------------------------------------------
	// ??? when mediaType = movie
	// "video": false,
	// ---------------------------------------------------------------
	// "vote_average": 6.873,
	// ---------------------------------------------------------------
	// "vote_count": 9016,
	// ---------------------------------------------------------------
	@JsonProperty("character")
	private String character;
	// ---------------------------------------------------------------
	@JsonProperty("credit_id")
	private String creditId;
	// ---------------------------------------------------------------
	// ??? when mediaType = movie
	@JsonProperty("order")
	private Integer order;
	// ---------------------------------------------------------------
	// ??? when mediaType = tv
	@JsonProperty("episode_count")
	private Integer totalEpisodes;
	// ---------------------------------------------------------------
	@JsonProperty("media_type")
	private String childtype;
	// ---------------------------------------------------------------
	// ToDo not in paylaod
	// ToDo FAKE
	private boolean completed = false;
	// ---------------------------------------------------------------
	// ToDo not in paylaod
	// ToDo FAKE
	private Integer episodesWatched = 0;
}