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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="shop")
public class Shop implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="shop_code")
	private int shopCode;
	
	@Column(name="shopname", nullable=false)
	private String shopName;
	
	@Column(name="image_path")
	private String imagePath;
	
	private String content;
	
	@Column(name="shop_status")
	private boolean shopStatus = true; // 샵을 생성하면 디폴트로 열린상태(false는 샵 닫힘)
	
	@OneToMany(mappedBy="shop", cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Hashtag> hashtags = new ArrayList<Hashtag>();
	
	@ManyToOne
	@JoinColumn(name="stylist_code")
	private Stylist stylist;
	
	@OneToMany(mappedBy="shop", cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Time> times = new ArrayList<Time>();
	
	@OneToMany(mappedBy="shop", cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Postscript> postscripts = new ArrayList<Postscript>();
	
	@OneToMany(mappedBy="shop", cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Product> products = new ArrayList<Product>();

}
