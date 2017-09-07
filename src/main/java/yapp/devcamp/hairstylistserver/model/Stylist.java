package yapp.devcamp.hairstylistserver.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="stylist")
public class Stylist implements Serializable {
	private static final long serialVersionUID = 2792820973956387153L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="stylist_code")
	private int stylistCode;
	
	@Column(name="stylist_nickname")
	private String stylistNickname; // stylist 별명
	
	@Column(name="stylist_realname")
	@NotEmpty(message="실명 입력란은 필수 항목 입니다.")
	private String stylistRealname; // stylist 실명
	
	@NotEmpty(message="연락처 입력란은 필수 항목 입니다.")
	@Size(min=9, max=11, message="연락처를 정확히 입력해주세요.") // 025994498 ~ 01071504498
//	@Pattern(regexp="/^[0-9]+$/", message="-(하이픈) 없이 숫자만 입력해주세요.")
	// front 에서 칸 3개 나눠주면 phone number validation 다시
	private String phone; // 전화번호
	
	private boolean qualified = false; // stylist 자격증 인증 여부
	
	@Column(name="license_image_path")
	private String licenseImagePath;
	
	@Transient
	private MultipartFile licenseImage;
	
	@Column(nullable=false)
	private String location; // 도로명주소
	
	@Column(name="detail_location", nullable=false)
	private String detailLocation; // 상세주소. 몇동몇호

	@OneToOne
	@JoinColumn(name="user_id") // fk
	private User user;
	
	@OneToMany(mappedBy="stylist", cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Shop> shops = new ArrayList<Shop>();
}
