package yapp.devcamp.hairstylistserver.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name="users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(nullable=false)
	@Email(message="E-mail 주소를 올바르게 입력해 주세요.")
	@NotEmpty(message="E-mail 주소를 작성해 주셔야 합니다.")
	private String email;
	
	@Column(nullable=false)
	@NotEmpty(message="사용하실 닉네임을 작성해 주셔야 합니다.")
	private String username; // username appear to profile
	
	@Column(nullable=false)
	@Length(min=5, message="비밀번호를 5자리 이상으로 입력해 주세요.")
	@NotEmpty(message="비밀번호를 작성해 주셔야 합니다.")
	private String password;
	
}
