package servie.track_servie.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Person
{
    @Column(name = "adult")
    private Boolean adult;
    // ---------------------------------------------------------------
    @Column(name = "gender")
    private Integer gender;
    // ---------------------------------------------------------------
    @Id
    @Column(name = "id")
    private Integer id;
    // ---------------------------------------------------------------
    @JsonProperty("known_for_department")
    @Column(name = "known_for_department")
    private String knownForDepartment;
    // ---------------------------------------------------------------
    @Column(name = "name")
    private String name;
    // ---------------------------------------------------------------
    @Column(name = "popularity")
    private Integer popularity;
    // ---------------------------------------------------------------
    @JsonProperty("profile_path")
    @Column(name = "profile_path")
    private String profilePath;
    // ---------------------------------------------------------------
    @JsonProperty("character")
    @Column(name = "role")
    private String role;
    // ---------------------------------------------------------------
    @JsonProperty("order")
    @Column(name = "position")
    private Integer position;
}
