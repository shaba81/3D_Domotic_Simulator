package com.mygdx.sms;

/**
 * This class represents the API Response. It is automatically created starting
 * from the JSON object returned by the server, using GSon
 */
public class SendSMSResponse {
	private String result;

	public String getResult() {
		return result;
	}

	public boolean isValid() {
		return "OK".equals(result);
	}
}
