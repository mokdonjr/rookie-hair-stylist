package yapp.devcamp.hairstylistserver.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name="book")
public class Book implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="book_code")
	private int bookCode;
	
	@Column(name="book_date")
	private String bookDate;
	
	@Column(name="book_day")
	private String bookDay;
	
	@Column(name="book_time")
	private String bookTime;
	
	@Column(name="book_status")
	private boolean bookStatus = true;
	
	@Column(name="product_code")
	private int productCode;
	
	@Column(name="option_code")
	private int optionCode;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="shop_code")
	private Shop shop;
	
//	// product fk (2017-09-04 13:43 카톡회의)
//	@OneToMany(mappedBy="book", cascade=CascadeType.ALL)
//	@LazyCollection(LazyCollectionOption.FALSE)
//	private List<Product> products = new ArrayList<>();
//	
//	// productOption fk (2017-09-04 13:43 카톡회의)
//	@OneToMany(mappedBy="book", cascade=CascadeType.ALL)
//	@LazyCollection(LazyCollectionOption.FALSE)
//	private List<ProductOption> productOptions = new ArrayList<>();

}
