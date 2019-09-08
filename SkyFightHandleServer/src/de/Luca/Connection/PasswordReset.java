package de.Luca.Connection;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class PasswordReset {
	
	private static Session session;
	private static HashMap<String, String> tokens = new HashMap<String, String>();
	
	private static final String from = "skyfight.net";

	public static void addToken(String email, String token) {
		tokens.put(token, email);
	}
	
	public static void removeTokcen(String token) {
		tokens.remove(token);
	}
	
	public static String getEmailForToken(String token) {
		return tokens.get(token);
	}
	
	public static void init() {
		Properties props = new Properties();
        session = Session.getDefaultInstance(props, null);
	}

	public static void sendReset(String email, String username, String resetToken) throws AddressException, MessagingException, UnsupportedEncodingException {
		Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from, "noreply"));
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email, username));
        msg.setSubject("Password reset token for SkyFight");
        msg.setText("Here is your password reset token for SkyFight\n"
        		+ "Please use this token to reset your password.\n"
        		+ "If you have not requested a password reset just ignore this E-Mail\n\n"
        		+ "Token: " + resetToken);
        Transport.send(msg);
	}

}
