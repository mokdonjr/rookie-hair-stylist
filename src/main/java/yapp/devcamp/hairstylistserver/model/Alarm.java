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

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="alarm")
public class Alarm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7990379282850030723L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="alarm_code")
	private int alarmCode;
	
	@Column(name="read_status")
	private boolean readStatus; // 알람 읽은 여부
	
	private String sender;
	
	private String receiver;
	
	@Column(name="book_status") // Book에서 복사해서 유지해야함
	private boolean bookStatus = true; // 예약 생성시 디폴트로 수락상태 (false는 취소상태)
	
	@OneToOne(optional=false, cascade=CascadeType.ALL)
	@JoinColumn(unique=true)
	private Book book;
}
