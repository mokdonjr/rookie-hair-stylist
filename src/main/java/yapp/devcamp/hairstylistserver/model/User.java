package yapp.devcamp.hairstylistserver.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="user")
public class User {

   @Id
   private long id; // kakao, facebook id (Integer)
   
   private String nickname; // kakao, facebook 등록된 이메일의 아이디

   private String username; // kakao, facebook 등록된 사용자 이름

   @Column(name="profile_image_path")
   private String profileImagePath;
   
   @Transient
   private MultipartFile profileImage;
}