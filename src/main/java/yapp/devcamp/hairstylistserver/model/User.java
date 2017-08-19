package yapp.devcamp.hairstylistserver.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="user")
public class User implements Serializable{

   @Id
   private long id; // kakao, facebook id (Integer)
   
   private String nickname; // kakao, facebook 등록된 이메일의 아이디

   private String username; // kakao, facebook 등록된 사용자 이름

   @Column(name="profile_image_path")
   private String profileImagePath;
   
   @Transient
   private MultipartFile profileImage;
   
   @OneToMany(mappedBy="user", cascade=CascadeType.ALL)
   @LazyCollection(LazyCollectionOption.FALSE)   
   private List<Chat> chats = new ArrayList<Chat>();
   
   @OneToMany(mappedBy="user", cascade=CascadeType.ALL)
   @LazyCollection(LazyCollectionOption.FALSE)
   private List<Book> books = new ArrayList<Book>();
}