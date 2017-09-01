package yapp.devcamp.hairstylistserver.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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

import lombok.Getter;
import lombok.Setter;
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
	private String principal; // kakao, facebook pk
	
	@Column(name="social_type")
	@Enumerated(EnumType.STRING)
	private SocialType socialType;
	
	private String email; // kakao, facebook 등록된 이메일 계정 // test용

	private String nickname; // kakao, facebook 등록된 이메일의 아이디

	private String username; // kakao, facebook 등록된 사용자 이름

	@Column(name = "profile_image_path")
	private String profileImagePath;

	@Transient
	private MultipartFile profileImage;
	
	@OneToMany(mappedBy="user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Role> roles;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Chat> chats = new ArrayList<Chat>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Book> books = new ArrayList<Book>();
	
	@OneToOne(mappedBy="user", cascade=CascadeType.ALL)
	private Stylist stylist; // user(cascade) is parent for stylist
}