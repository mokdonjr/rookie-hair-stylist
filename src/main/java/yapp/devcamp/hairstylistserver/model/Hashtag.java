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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="hashtag")
public class Hashtag implements Serializable {
	private static final long serialVersionUID = -3717115168611329619L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int hashtag_code;
	
	@Column(name="hash_tag")
	private String hashtagContent;
	
	@ManyToOne
	@JoinColumn(name="shop_code")
	private Shop shop;
	
}
