package yapp.devcamp.hairstylistserver.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="role")
public class Role {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="role_code")
	private int roleCode;
	
	@Column(name="role_name", nullable=false, unique=true)
	private String roleName;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
}
