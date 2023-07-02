package servie.track_servie.entity.people;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Person
{
    @Id
    @JsonProperty("id")
    @Column(name = "id")
    private Integer id;
    // ---------------------------------------------------------------
    @Column(name = "adult")
    private Boolean adult;
    // ---------------------------------------------------------------
    @Column(name = "gender")
    private Integer gender;
    // ---------------------------------------------------------------
    @JsonProperty("imdb_id")
    @Column(name = "imdb_id")
    private Integer imdbId;
    // ---------------------------------------------------------------
    @JsonProperty("known_for_department")
    @Column(name = "knownForDepartment")
    private String knownForDepartment;
    // ---------------------------------------------------------------
    @Column(name = "name")
    private String name;
    // ---------------------------------------------------------------
    @Column(name = "popularity")
    private Double popularity;
    // ---------------------------------------------------------------
    @JsonProperty("profile_path")
    @Column(name = "profile_path")
    private String profilePath;
    // ---------------------------------------------------------------
    @JsonProperty("order")
    @Column(name = "position")
    private Integer position;
    // ---------------------------------------------------------------
    @JsonProperty("birthday")
    @Column(name = "birthday")
    private LocalDate birthday;
    // ---------------------------------------------------------------
    @JsonProperty("deathday")
    @Column(name = "deathday")
    private LocalDate deathday;
    // ---------------------------------------------------------------
    @JsonProperty("biography")
    @Column(name = "biography")
    private String biography;
    // ---------------------------------------------------------------
    @JsonProperty("place_of_birth")
    @Column(name = "birth_place")
    private String birthPlace;
}