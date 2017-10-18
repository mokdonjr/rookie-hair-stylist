package yapp.devcamp.hairstylistserver.model;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yapp.devcamp.hairstylistserver.enums.ShopStatus;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="shop")
public class Shop implements Serializable {
	private static final long serialVersionUID = 486537536990323693L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="shop_code")
	private int shopCode;
	
	@Column(name="shopname", nullable=false)
	@NotEmpty(message="헤어샵 입력란은 필수 항목 입니다.")
	private String shopName;
	
	@Column(name="image_path")
	private String imagePath;
	
	@NotEmpty(message="헤어스타일링 설명 입력란은 필수 항목 입니다.")
	private String content; // 헤어스타일링 설명
	
	@NotEmpty(message="고객과 만날 장소 입력란은 필수 항목 입니다.")
	private String location;
	
	@Column(name="shopstatus")
	private ShopStatus shopStatus;
	
	@Column(name="shop_time")
	@NotEmpty(message="가능한 시간대 입력란은 필수 항목 입니다.")
	private String shopTime;
	
	@Column(name="shop_date")
	@NotEmpty(message="가능한 날짜 입력란은 필수 항목 입니다.")
	private String shopDate;
	
	@Column(name="shop_day")
	@NotEmpty(message="가능한 요일 입력란은 필수 항목 입니다.")
	private String shopDay;
	
	@Transient
//	private File[] files;
	private MultipartFile shopImage;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="stylist_code")
	private Stylist stylist;
	
	@OneToMany(mappedBy="shop", cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Product> products;
	
	@OneToMany(mappedBy="shop", cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<ProductOption> options;
}
