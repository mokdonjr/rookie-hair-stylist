package yapp.devcamp.hairstylistserver.model;

import java.io.File;
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
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.beans.factory.annotation.Configurable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	
	@Transient
	private File[] files;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="stylist_code")
	private Stylist stylist;
}
