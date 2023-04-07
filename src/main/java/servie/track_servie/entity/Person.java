package servie.track_servie.entity;

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
    private boolean adult;
    // ---------------------------------------------------------------
    @Column(name = "gender")
    private int gender;
    // ---------------------------------------------------------------
    @Id
    @Column(name = "id")
    private int id;
    // ---------------------------------------------------------------
    @Column(name = "known_for_department")
    @JsonProperty("known_for_department")
    private String knownForDepartment;
    // ---------------------------------------------------------------
    @Column(name = "name")
    private String name;
    // ---------------------------------------------------------------
    @Column(name = "popularity")
    private Float popularity;
    // ---------------------------------------------------------------
    @Column(name = "profile_path")
    @JsonProperty("profile_path")
    private String profilePath;
    // ---------------------------------------------------------------
    @Column(name = "character")
    private String character;
    // ---------------------------------------------------------------
    @Column(name = "order")
    private int order;
}
