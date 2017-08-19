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
@Table(name="hashtag")
public class Hashtag implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int hashtag_code;
	
	@Column(name="hash_tag")
	private String hashtagContent;
	
	@ManyToOne
	@JoinColumn(name="shop_code")
	private Shop shop;
	
}
