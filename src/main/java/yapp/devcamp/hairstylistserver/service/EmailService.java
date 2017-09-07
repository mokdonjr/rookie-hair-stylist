package yapp.devcamp.hairstylistserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import yapp.devcamp.hairstylistserver.model.Stylist;
import yapp.devcamp.hairstylistserver.model.User;

@Service
public class EmailService {
	
	@Value("${spring.mail.username}")
	private String rookiesEmailAddress; // max size multipart data
	
	private String currentContextPath = "http://localhost:8080";
	
	private JavaMailSender mailSender;
	
	@Autowired
	public EmailService(JavaMailSender mailSender){
		this.mailSender = mailSender;
	}
	
	@Async
	public void sendWelcomeUserEmail(User user) throws MailException, InterruptedException{
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(user.getEmail());
		mail.setFrom(rookiesEmailAddress);
		mail.setSubject("[Rookies] 예비 헤어디자이너 플랫폼 Rookies에 오신것을 환영합니다!");
		mail.setText("가입 완료 되었습니다.");
		
		mailSender.send(mail);
	}
	
	@Async
	public void sendApplyStylistEmail(User user) throws MailException, InterruptedException{
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(user.getEmail());
		mail.setFrom(rookiesEmailAddress);
		mail.setSubject("[Rookies] 예비 헤어디자이너 플랫폼 Rookies 스타일리스트 등록 심사중입니다!");
		mail.setText("심사 완료 뒤 메일이 발송될 예정입니다.");
		
		mailSender.send(mail);
	}
	
	@Async
	public void sendAdminStylistEmail(User user, Stylist stylist) throws MailException, InterruptedException{
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(rookiesEmailAddress);
		mail.setFrom(user.getEmail());
		mail.setSubject("[Rookies ADMIN] 스타일리스트 등록 알림입니다. - " + user.getPrincipal() + " | " + user.getUsername());
		mail.setText("스타일리스트 등록 알림입니다. 해당 사용자에게 스타일리스트 권한을 부여하시겠습니까?\n"
				+ "유저정보" + user.getProfileImagePath() + "\n" + user.getUsername() + "\n" + user.getAuthorityType() + "\n" + user.getSocialType() + "\n"
				+ "스타일리스트정보" + stylist.getStylistRealname() + "\n" + stylist.getLicenseImagePath() + "\n" + stylist.getPhone() + "\n" + stylist.getStylistNickname()
				+ "\n" + stylist.getLocation() + "\n" + stylist.getDetailLocation()
				+ "\n" + grantStylistAuthorityUrl(user, stylist));
		
		mailSender.send(mail);
	}
	
	@Async
	public void sendEnrollStylistEmail(User admin) throws MailException, InterruptedException{
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(admin.getEmail());
		mail.setFrom(rookiesEmailAddress);
		mail.setSubject("[Rookies] 예비 헤어디자이너 플랫폼 Rookies 스타일리스트가 되신 것을 축하드립니다!");
		mail.setText("로그아웃 후 다시 로그인 하여 권한을 얻습니다.\n" + getStylistAuthorityUrl());
		
		mailSender.send(mail);
	}
	
	// admin do
	private String grantStylistAuthorityUrl(User user, Stylist stylist){
		StringBuilder url = new StringBuilder(currentContextPath);
		url.append("/admin").append("/grant").append("/stylist").append("/" + user.getId());
		return url.toString();
	}
	
	// stylist applicants do
	private String getStylistAuthorityUrl(){
		StringBuilder url = new StringBuilder(currentContextPath);
		url.append("/stylist").append("/enroll");
		return url.toString();
	}
}
