package com.mygdx.email;

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
public class EmailSender {

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
	public void sendMessage(String send, String object, String messageArea) {

		try {
			boolean result = true;

			InternetAddress emailAddr = new InternetAddress(send);
			emailAddr.validate();
			if (result) {
				MimeMessage message;

				// Setta le propriet� della connessione
				Session session = Session.getDefaultInstance(this.setPropertiesStream());

				message = new MimeMessage(session);
				message.setFrom(new InternetAddress());

				// chiamata al Set to
				this.setTo(message, send);

				// chiamata al set Oggeto
				this.setObject(message, object);

				// chiamata al set Messaggio
				this.setText(message, messageArea);

				// Send message
				this.sendEmail(message, session);

			} else {
				JOptionPane.showMessageDialog(null, "La email inserita non esiste");
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Metodo che setta le proprietà della connesione smtp.
	 * @return
	 */
	private Properties setPropertiesStream() {
		  Properties properties = System.getProperties();
	      properties.setProperty("mail.smtp.port", "25");
	      properties.setProperty("mail.smtp.socketFactory.port", "25");

	      properties.setProperty("smtp.gmail.com", "localhost");
	      return properties;
	}

	/**
	 * Meotodo che setta il Destinatario della mail
	 * @param m
	 * @param jTx
	 * @throws MessagingException 
	 * @throws AddressException 
	 */
	private void setTo(MimeMessage m, String jTx) throws AddressException, MessagingException {
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(jTx));
	}

	/**
	 * Metodo che setta l'oggetto dell'email
	 * @param m
	 * @param jTx
	 * @throws MessagingException
	 */
	private void setObject(MimeMessage m, String jTx) throws MessagingException {
			m.setSubject(jTx);
	}

	/**
	 * Meotod che setta il testo dell'email
	 * @param m
	 * @param jTx
	 * @throws MessagingException
	 */
	public void setText(MimeMessage m, String jTx) throws MessagingException {
			m.setText(jTx);
	}

	/**
	 * Metodo che manda effettivamente la email settando le infomrazioni per 
	 * la connessione alla casella di posta su gmail. Le informazioni le prende
	 * da un file .json attraverso l'uso della libreria GSON di Google.
	 * 
	 * @param m
	 * @param s
	 * @throws FileNotFoundException
	 * @throws MessagingException
	 */
	public void sendEmail(MimeMessage m, Session s) throws FileNotFoundException, MessagingException {
        //	Configuration config = (Configuration) Utils.getJsonFile(Configuration.class, Utils.CONFIG_PATH_EMAIL);

			String email = Configuration.userEmail;
			String passwd = Configuration.userPassword;


        	SMTPTransport t = (SMTPTransport)s.getTransport("smtps");
			t.connect("smtp.gmail.com", email, passwd);
			t.sendMessage(m, m.getAllRecipients());      
			t.close();
			JOptionPane.showMessageDialog(null, "Email inviata con successo.");
	}
}