package servie.track_servie.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "casting")
public class Cast
{
	@Id
	@Column(name = "credit_id")
	@JsonProperty("credit_id")
	private String creditId;
	// ---------------------------------------------------------------
	// @Column(name = "person_adult")
	// @JsonProperty("adult")
	// private Boolean personAdult;
	// ---------------------------------------------------------------
	@Column(name = "person_gender")
	@JsonProperty("gender")
	private Integer personGender;
	// ---------------------------------------------------------------
	// @OneToOne
	@Column(name = "person_id")
	@JsonProperty("id")
	private Integer personId;
	// ---------------------------------------------------------------
	@Column(name = "person_name")
	@JsonProperty("name")
	private String personName;
	// ---------------------------------------------------------------
	// @JsonProperty("original_name")
	// @Column(name = "person_original_name")
	// private String personOriginalName;
	// ---------------------------------------------------------------
	// @JsonProperty("known_for_department")
	// @Column(name = "person_known_for_department")
	// private String personKnownForDepartment;
	// ---------------------------------------------------------------
	@Column(name = "character_name")
	@JsonProperty("character")
	private String characterName;
	// ---------------------------------------------------------------
	// @JsonProperty("popularity")
	// @Column(name = "person_popularity")
	// private double personPopularity;
	// ---------------------------------------------------------------
	@Column(name = "personProfile_path")
	@JsonProperty("profile_path")
	private String personProfilePath;
	// ---------------------------------------------------------------
	@Column(name = "cast_id")
	@JsonProperty("cast_id")
	private Integer castId;
	// ---------------------------------------------------------------
	@Column(name = "priority")
	@JsonProperty("order")
	private Integer priority;
	// ---------------------------------------------------------------
	// @ManyToOne
	// @JoinColumn(name = "tmdb_id", referencedColumnName = "tmdb_id")
	// @JoinColumn(name = "childtype", referencedColumnName = "childtype")
	// private Servie servie;
}