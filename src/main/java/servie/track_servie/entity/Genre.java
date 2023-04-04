package servie.track_servie.entity;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Genre
{
    @Column(name = "id")
    @Id
    private Integer id;
    // ---------------------------------------------------------------
    @Column(name = "name")
    private String name;
    // ---------------------------------------------------------------
    @ManyToMany(mappedBy = "genres")
    private Set<Servie> servies = new HashSet<>();
}
