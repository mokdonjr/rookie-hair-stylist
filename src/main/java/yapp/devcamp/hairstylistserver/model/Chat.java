package yapp.devcamp.hairstylistserver.model;

import java.io.Serializable;
import java.util.Date;

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
@Table(name="chat")
public class Chat implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="chat_code")
	private int chatCode;
	
	private String receiver;
	
	private Date writeDate;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	private String content;
	
	@Column(name="image_path")
	private String imagePath;
}
