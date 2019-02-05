package com.mygdx.services.sms;

public class SendSMSResponse {
	private String result;

	public String getResult() {
		return result;
	}

	public boolean isValid() {
		return "OK".equals(result);
	}
}
