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
@Table(name="time")
public class Time implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="time_code")
	private int timeCode;
	
	@Column(name="shoptime")
	private String shopTime;
	
	@Column(name="shopdate")
	private String shopDate; 
	
	@Column(name="shopday")
	private String shopDay; 
	
	@ManyToOne
	@JoinColumn(name="shop_code")
	private Shop shop;

}
