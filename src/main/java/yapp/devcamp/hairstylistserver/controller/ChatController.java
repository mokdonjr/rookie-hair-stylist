package yapp.devcamp.hairstylistserver.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import yapp.devcamp.hairstylistserver.model.Chat;
import yapp.devcamp.hairstylistserver.model.Stylist;
import yapp.devcamp.hairstylistserver.model.User;
import yapp.devcamp.hairstylistserver.service.ChatService;
import yapp.devcamp.hairstylistserver.service.StylistService;
import yapp.devcamp.hairstylistserver.service.UserService;

@Controller
public class ChatController {
	Logger logger = LoggerFactory.getLogger(ChatController.class);
	
	@Autowired
	private ChatService chatService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StylistService stylistService;
	
	@RequestMapping("/chat")
	public String retrieveChatMessages(Model model){
		
		// 2. 나에게 보내진 것(receiver)과 내가 보낸 것(user) retrieve
		User currentUser = getCurrentUser();
		String receiver = String.valueOf(currentUser.getId());
		List<Chat> chatMesssages = chatService.findByReceiverAndUser(receiver, currentUser);
		
		model.addAttribute("chatMessages", chatMesssages);
		return "chat";
	}

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	@SendToUser
	public Chat handleChatMessage(Chat chat) throws Exception{
		
		return chat;
	}
	
	private User getCurrentUser(){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String principal = authentication.getName();
		logger.warn(principal);
		
		User user = userService.findByPrincipal(principal);
		return user;
	}
}
