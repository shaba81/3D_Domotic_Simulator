package com.mygdx.services.sms;

import java.util.ArrayList;
import java.util.List;

/**
 * This object is used to create a request for sending messages
 * SMS.
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
