package servie.track_servie.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "cast")
public class Cast
{
    @Id
    @JsonProperty("credit_id")
    @Column(name = "credit_id")
    private String creditId;
    // ---------------------------------------------------------------
    // @JsonProperty("adult")
    // @Column(name = "person_adult")
    // private Boolean personAdult;
    // ---------------------------------------------------------------
    @JsonProperty("gender")
    @Column(name = "person_gender")
    private Integer personGender;
    // ---------------------------------------------------------------
    // @OneToOne
    @JsonProperty("id")
    @Column(name = "person_id")
    private Integer personId;
    // ---------------------------------------------------------------
    @JsonProperty("name")
    @Column(name = "person_name")
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
    @JsonProperty("character")
    @Column(name = "character_name")
    private String characterName;
    // ---------------------------------------------------------------
    // @JsonProperty("popularity")
    // @Column(name = "person_popularity")
    // private double personPopularity;
    // ---------------------------------------------------------------
    @JsonProperty("profile_path")
    @Column(name = "personProfile_path")
    private String personProfilePath;
    // ---------------------------------------------------------------
    @JsonProperty("cast_id")
    @Column(name = "cast_id")
    private Integer castId;
    // ---------------------------------------------------------------
    @JsonProperty("order")
    @Column(name = "priority")
    private Integer priority;
    // ---------------------------------------------------------------
    // @ManyToOne
    // @JoinColumn(name = "tmdb_id", referencedColumnName = "tmdb_id")
    // @JoinColumn(name = "childtype", referencedColumnName = "childtype")
    // private Servie servie;
}