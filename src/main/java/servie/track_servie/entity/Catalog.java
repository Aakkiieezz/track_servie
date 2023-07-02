package servie.track_servie.entity;

import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Catalog
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    // ---------------------------------------------------------------
    private String name;
    // ---------------------------------------------------------------
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    // ---------------------------------------------------------------
    @ManyToMany
    private Set<Servie> servies;
    // ---------------------------------------------------------------
    private String description;
}
