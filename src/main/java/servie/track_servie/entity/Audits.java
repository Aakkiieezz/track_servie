package servie.track_servie.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import servie.track_servie.enums.AuditType;

@Data
@Entity
public class Audits
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	// ---------------------------------------------------------------
	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private AuditType type;
	// ---------------------------------------------------------------
	@Column(name = "description")
	private String desciption;
	// ---------------------------------------------------------------
	@Column(name = "identity")
	private String identity;
	// ---------------------------------------------------------------
	@Column(name = "entryTime")
	private LocalDateTime entryTime = LocalDateTime.now();

	public Audits(AuditType type, String desciption)
	{
		this.type = type;
		this.desciption = desciption;
	}

	public Audits(AuditType type, String desciption, String identity)
	{
		this.type = type;
		this.desciption = desciption;
		this.identity = identity;
	}
}