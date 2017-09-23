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
@Table(name="product_option")
public class ProductOption implements Serializable {
	private static final long serialVersionUID = 2280008708531643990L;

	@Transient
	private List<ProductOption> optionList;
	
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
