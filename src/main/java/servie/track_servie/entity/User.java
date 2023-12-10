package servie.track_servie.entity;

import java.util.List;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class User
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	// ---------------------------------------------------------------
	@Column(name = "name")
	private String name;
	// ---------------------------------------------------------------
	// @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	// private Set<Catalog> lists;
	// ---------------------------------------------------------------
	@Column(name = "email", unique = true)
	private String email;
	// ---------------------------------------------------------------
	@Column(name = "password")
	private String password;
	// ---------------------------------------------------------------
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<UserServieData> userServies;
	// ---------------------------------------------------------------
	@ManyToMany
	private Set<Role> roles;
}
