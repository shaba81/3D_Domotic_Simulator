package com.mygdx.controller.email;

import java.io.FileNotFoundException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

import com.sun.mail.smtp.SMTPTransport;

import utilis.Configuration;
import utilis.Utils;

/**
 * Classe che invia la mail utilizzando una libreria javax.mail
 * 
 * @author Antonio
 *
 */
public abstract class EmailSender {

	/**
	 * Metodo che invia la mail data la mail a cui mandare, l'oggetto e il messaggio
	 * di testo. Controlla se la mail è stata mandata correttamente e se il o gli
	 * indirizzi email da mandare esistono.
	 * 
	 * @param send        {@link String} stirnga che identifica il o i mittenti.
	 * @param object      {@link Stirng} stringa che identifica l'oggetto del
	 *                    messaggio.
	 * @param messageArea {@link String} sctringa che identifica il messaggio da
	 *                    mettere nel corpo della mail.
	 */
	public static boolean sendMessage(String send, String object, String messageArea) {

		try {
			boolean result = true;

			InternetAddress emailAddr = new InternetAddress(send);
			emailAddr.validate();
			if (result) {
				MimeMessage message;

				// Setta le propriet� della connessione
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

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return false;
	}

	/**
	 * Metodo che setta le proprietà della connesione smtp.
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
	 * Meotodo che setta il Destinatario della mail
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
	 * Metodo che setta l'oggetto dell'email
	 * 
	 * @param m
	 * @param jTx
	 * @throws MessagingException
	 */
	private static void setObject(MimeMessage m, String jTx) throws MessagingException {
		m.setSubject(jTx);
	}

	/**
	 * Meotod che setta il testo dell'email
	 * 
	 * @param m
	 * @param jTx
	 * @throws MessagingException
	 */
	private static void setText(MimeMessage m, String jTx) throws MessagingException {
		m.setText(jTx);
	}

	/**
	 * Metodo che manda effettivamente la email settando le infomrazioni per la
	 * connessione alla casella di posta su gmail. Le informazioni le prende da un
	 * file .json attraverso l'uso della libreria GSON di Google.
	 * 
	 * @param m
	 * @param s
	 * @throws FileNotFoundException
	 * @throws MessagingException
	 */
	private static void sendEmail(MimeMessage m, Session s) throws FileNotFoundException, MessagingException {
		// Configuration config = (Configuration) Utils.getJsonFile(Configuration.class,
		// Utils.CONFIG_PATH_EMAIL);

		String email = Configuration.userEmail;
		String passwd = Configuration.userPassword;

		SMTPTransport t = (SMTPTransport) s.getTransport("smtps");
		t.connect("smtp.gmail.com", email, passwd);
		t.sendMessage(m, m.getAllRecipients());
		t.close();
	}
}