package yapp.devcamp.hairstylistserver.model;

import java.io.File;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="postscript")
public class Postscript implements Serializable {
	private static final long serialVersionUID = 3928839638644199699L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="postscript_code")
	private int postscriptCode;
	
	private float grade;
	
	private String content; 
	
	@Column(name="write_date")
	private String writeDate; 
	
	@Column(name="image_path")
	private String imagePath;
	
	@Transient
	private MultipartFile postscriptImg;
	
	@Transient
	private String opinion;
	
	@ManyToOne
	@JoinColumn(name="shop_code")
	private Shop shop;
	
	@ManyToOne
	@JoinColumn(unique=true)
	private User user;

}
