package yapp.devcamp.hairstylistserver.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="product_option")
public class ProductOption implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="option_code")
	private int optionCode;
	
	@Column(name="option_name")
	private String optionName;
	
	@Column(name="option_price")
	private int optionPrice;

	@ManyToOne
	@JoinColumn(name="shop_code")
	private Shop shop;

}
