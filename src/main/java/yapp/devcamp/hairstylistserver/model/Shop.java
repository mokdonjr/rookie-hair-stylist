package yapp.devcamp.hairstylistserver.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
import org.springframework.beans.factory.annotation.Configurable;

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
	
	private String location;
	
	@Column(name="shopstatus")
	private String shopStatus;
	
	@Column(name="shop_time")
	private String shopTime;
	
	@Column(name="shop_date")
	private String shopDate;
	
	@Column(name="shop_day")
	private String shopDay;
	
	@OneToMany(mappedBy="shop", cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Hashtag> hashtags = new ArrayList<Hashtag>();
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="stylist_code")
	private Stylist stylist;
	
<<<<<<< HEAD
	
=======
>>>>>>> d7b16c4aff5a6210b9f3d34899015fe3a005261b
	@OneToMany(mappedBy="shop", cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Postscript> postscripts;
	
	@OneToMany(mappedBy="shop", cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Product> products;
	
	@OneToMany(mappedBy="shop", cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<ProductOption> options;

	// 
//	@OneToMany(mappedBy="shop", cascade=CascadeType.ALL)
//	@LazyCollection(LazyCollectionOption.FALSE)
//	private List<Time> times;
	
	@Column(name="shop_time")
	private String shopTime;
	
	@Column(name="shop_date")
	private String shopDate; 
	
	@Column(name="shop_day")
	private String shopDay;

}
