package yapp.devcamp.hairstylistserver.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="postscript")
public class Postscript implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="postscript_code")
	private int postscriptCode;
	
	private int grade;
	
	private String content; 
	
	@Column(name="write_date")
	private Date writeDate; 
	
	@Column(name="image_path")
	private String imagePath;
	
	@ManyToOne
	@JoinColumn(name="shop_code")
	private Shop shop;
	
	@OneToOne(optional=false, cascade=CascadeType.ALL)
	@JoinColumn(unique=true)
	private User user;

}
