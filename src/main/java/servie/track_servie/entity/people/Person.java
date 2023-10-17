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
    @JsonProperty("imdb_id")
    @Column(name = "imdb_id")
    private Integer imdbId;
    // ---------------------------------------------------------------
    @Column(name = "name")
    @JsonProperty("name")
    private String name;
    // ---------------------------------------------------------------
    @JsonProperty("known_for_department")
    @Column(name = "knownForDepartment")
    private String knownForDepartment;
    // ---------------------------------------------------------------
    @Column(name = "gender")
    private Integer gender;
    // ---------------------------------------------------------------
    @Column(name = "adult")
    private Boolean adult;
    // ---------------------------------------------------------------
    @Column(name = "popularity")
    @JsonProperty("popularity")
    private Double popularity;
    // ---------------------------------------------------------------
    @JsonProperty("profile_path")
    @Column(name = "profile_path")
    private String profilePath;
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
    // ---------------------------------------------------------------
    @JsonProperty("homepage")
    @Column(name = "homepage")
    private String homepage;
}
// -----------------
// "also_known_as": [
//     "full name 1",
//     "full name 2"
//   ],