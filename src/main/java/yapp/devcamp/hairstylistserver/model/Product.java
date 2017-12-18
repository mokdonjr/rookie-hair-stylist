package yapp.devcamp.hairstylistserver.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="product")
public class Product implements Serializable {
	private static final long serialVersionUID = 8581922302743979460L;

	@Transient
	private List<Product> productList;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="product_code")
	private int productCode;
	
	@Column(name="product_name")
	private String productName;
	
	@Column(name="product_price")
	private int productPrice;
	
	@Transient
	private String productPriceStr;
	
	@ManyToOne
	@JoinColumn(name="shop_code")
	private Shop shop;
}
