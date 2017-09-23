package yapp.devcamp.hairstylistserver.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import yapp.devcamp.hairstylistserver.oauth.AuthorityType;
import yapp.devcamp.hairstylistserver.oauth.SocialType;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User implements Serializable {
	private static final long serialVersionUID = -2962225427529797300L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id; // pk

	@Column(name="principal")
	private String principal; // kakao and facebook pk
	
	@Column(name="social_type")
	@Enumerated(EnumType.STRING)
	private SocialType socialType; // 추후 default profile image시 kakao/facebook 구별
	
	@Column(name="authority_type")
	@Enumerated(EnumType.STRING)
	private AuthorityType authorityType; // USER -> STYLIST / ADMIN
	
	private String email; // kakao, facebook 등록된 이메일 계정 // test용

	private String nickname; // kakao, facebook 등록된 이메일의 아이디

	private String username; // kakao, facebook 등록된 사용자 이름

	@Column(name = "profile_image_path")
	private String profileImagePath;

	@Transient
	private MultipartFile profileImage;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Chat> chats = new ArrayList<Chat>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Book> books;
	
	@OneToOne(mappedBy="user", cascade=CascadeType.ALL)
	@JsonIgnore // issue(recursion) http://www.baeldung.com/jackson-bidirectional-relationships-and-infinite-recursion
	private Stylist stylist; // user(cascade) is parent for stylist
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Postscript> postscripts;
}