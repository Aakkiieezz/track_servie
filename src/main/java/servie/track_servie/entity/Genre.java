package servie.track_servie.entity;

import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Genre
{
    @Id
    @Column(name = "id")
    private Integer id;
    // ---------------------------------------------------------------
    @Column(name = "name")
    private String name;
    // ---------------------------------------------------------------
    @ManyToMany(mappedBy = "genres")
    private Set<Servie> servies;

    public Genre(Integer id, String name)
    {
        this.id = id;
        this.name = name;
    }
}
