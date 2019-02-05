package com.mygdx.services.email;

import java.io.FileNotFoundException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.mygdx.application.exception_manager.ExceptionsManager;
import com.mygdx.foundation.utils.Configuration;
import com.sun.mail.smtp.SMTPTransport;

/**
 * Class that sends mail to a javax.mail library
 * 
 * @author Antonio
 *
 */
public abstract class EmailSender {

	/**
	 * Method that sends the email given the email to send to, the subject and the message
	 * of text. Check if the mail was sent correctly and if the or
	 * email addresses to send exist.
	 *
	 * @param send {@link String} stirnga that identifies the sender (s).
	 * @param object {@link Stirng} string that identifies the object of the
	 * message.
	 * @param messageArea {@link String} sctringa that identifies the message from
	 * put in the body of the mail.
	 */
	public static boolean sendMessage(String send, String object, String messageArea) throws FileNotFoundException,MessagingException {

		boolean result = true;

		InternetAddress emailAddr;
		emailAddr = new InternetAddress(send);

		emailAddr.validate();

		if (result) {
			MimeMessage message;

			// Setta le proprietà della connessione
			Session session = Session.getDefaultInstance(setPropertiesStream());

			message = new MimeMessage(session);
			message.setFrom(new InternetAddress());

			// chiamata al Set to
			setTo(message, send);

			// chiamata al set Oggeto
			setObject(message, object);

			// chiamata al set Messaggio
			setText(message, messageArea);

			// Send message
			sendEmail(message, session);

			return true;
		}

		return false;
	}

	/**
	 * Method that sets the properties of the smtp connection.
	 * 
	 * @return
	 */
	private static Properties setPropertiesStream() {
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.port", "25");
		properties.setProperty("mail.smtp.socketFactory.port", "25");

		properties.setProperty("smtp.gmail.com", "localhost");
		return properties;
	}

	/**
	 * Meotodo that sets the Recipient of the mail
	 * 
	 * @param m
	 * @param jTx
	 * @throws MessagingException
	 * @throws AddressException
	 */
	private static void setTo(MimeMessage m, String jTx) throws AddressException, MessagingException {
		m.addRecipient(Message.RecipientType.TO, new InternetAddress(jTx));
	}

	/**
	 * Method that sets the subject of the email
	 * 
	 * @param m
	 * @param jTx
	 * @throws MessagingException
	 */
	private static void setObject(MimeMessage m, String jTx) throws MessagingException {
		m.setSubject(jTx);
	}

	/**
	 * Meotod that sets the text of the email
	 * 
	 * @param m
	 * @param jTx
	 * @throws MessagingException
	 */
	private static void setText(MimeMessage m, String jTx) throws MessagingException {
		m.setText(jTx);
	}

	/**
	 * Method that actually sends the email by setting the information for the
	 * connection to the mailbox on gmail. 
	 * 
	 * @param m
	 * @param s
	 * @throws FileNotFoundException
	 * @throws MessagingException
	 */
	private static void sendEmail(MimeMessage m, Session s) throws FileNotFoundException, MessagingException {

		String email = Configuration.userEmail;
		String passwd = Configuration.userPassword;

		SMTPTransport t = (SMTPTransport) s.getTransport("smtps");
		t.connect("smtp.gmail.com", email, passwd);
		t.sendMessage(m, m.getAllRecipients());
		t.close();
	}
}