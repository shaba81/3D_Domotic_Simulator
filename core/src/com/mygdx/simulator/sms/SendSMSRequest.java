package com.mygdx.simulator.sms;

import java.util.ArrayList;
import java.util.List;

/**
 * Questo oggetto viene utilizzato per creare una richiesta di invio di messaggi
 * SMS. L'oggetto JSon viene quindi creato automaticamente a partire da
 * un'istanza di questa classe, utilizzando GSon.
 * 
 * @author anton
 *
 */

public class SendSMSRequest {

	private String message;

	private String message_type;

	private boolean returnCredits = false;

	private ArrayList<String> recipient = new ArrayList<String>();

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageType() {
		return message_type;
	}

	public void setMessageType(String messageType) {
		this.message_type = messageType;
	}

	public boolean isReturnCredits() {
		return returnCredits;
	}

	public void setReturnCredits(boolean returnCredits) {
		this.returnCredits = returnCredits;
	}

	public List<String> getRecipient() {
		return recipient;
	}

	public void addRecipient(String recipient) {
		this.recipient.add(recipient);
	}
}
