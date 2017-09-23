package yapp.devcamp.hairstylistserver.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Table(name="alarm")
public class Alarm implements Serializable {
	private static final long serialVersionUID = 7990379282850030723L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="alarm_code")
	private int alarmCode;
	
	@Column(name="read_status")
	private boolean readStatus;
	
	private String sender;
	
	private String receiver;
	
	@Column(name="book_status") 
	private boolean bookStatus = true;
	
	@OneToOne(optional=false, cascade=CascadeType.ALL)
	@JoinColumn(unique=true)
	private Book book;
}
