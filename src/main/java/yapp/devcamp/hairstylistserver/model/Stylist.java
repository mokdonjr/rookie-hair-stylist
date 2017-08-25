package yapp.devcamp.hairstylistserver.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="stylist")
public class Stylist implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="stylist_code")
	private int stylistCode;
	
	@Column(name="stylist_nickname")
	private String stylistNickname; // stylist 별명
	
	private boolean qualified = false; // stylist 자격증 인증 여부
	
	@Column(name="license_image_path")
	private String licenseImagePath;
	
	@Transient
	private MultipartFile licenseImage;
	
	@Column(nullable=false)
	private String location; // 도로명주소
	
	@Column(name="detail_location", nullable=false)
	private String detailLocation; // 상세주소. 몇동몇호
	
	private String career;
	
//	@OneToOne(optional=false, cascade=CascadeType.ALL)
//	@JoinColumn(unique=true)
	@OneToOne
	@JoinColumn(name="user_id") // fk
	private User user;
	
	@OneToMany(mappedBy="stylist", cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Shop> shops = new ArrayList<Shop>();
}
