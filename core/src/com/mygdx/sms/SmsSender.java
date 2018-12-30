package com.mygdx.sms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import utilis.Configuration;
import utilis.Utils;

/**
 * Classe che invia l'SMS e prende le configurazioni dal file .json
 * 
 * @author anton
 *
 */
public class SmsSender {

	/**
	 * Metodo che invia l'SMS
	 */
	public void sendSms() {
		try {

			Configuration config = (Configuration) Utils.getJsonFile(Configuration.class, Utils.CONFIG_PATH_SMS);

			String baseUrlSms = config.baseUrlSms;
			String messageQuality = config.messageQuality;
			String userNameSms = config.userNameSms;
			String userPasswordSms = config.userPasswordSms;
			String messageBody = config.messageBody;
			String recipient = config.recipient;

			String[] authKeys = login(baseUrlSms, userNameSms, userPasswordSms);

			SendSMSRequest sendSMS = new SendSMSRequest();
			sendSMS.setMessage(messageBody);
			sendSMS.setMessageType(messageQuality);
			sendSMS.addRecipient(recipient);

			if (sendSMS(baseUrlSms, authKeys, sendSMS))
				System.out.println("INVIATO");
			else
				System.err.println("NON INVIATO");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Metodo che fa la login e si fa restituire authle AuthKey ( user_key,
	 * Session_key) per poter inviare l'SMS. Li preleva facendo una chiamata REST
	 * passando in GET le credenziali.
	 * 
	 * @param baseUrl  url di base su cui fare la login
	 * @param username
	 * @param password
	 * @return AuthKey ( user_key, Session_key)
	 * 
	 * @throws IOException
	 */
	private String[] login(String baseUrl, String username, String password) throws IOException {
		URL url = new URL(baseUrl + "/login?username=" + username + "&password=" + password);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("GET");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("ERRORE: " + conn.getResponseCode());
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String response = "";
		String output;
		while ((output = br.readLine()) != null) {
			response += output;
		}
		conn.disconnect();

		String[] parts = response.split(";");

		return parts;
	}

	/**
	 * Metodo che effettivamete l'Sms utilizzando ( user_key, Session_key) del
	 * metodo
	 * 
	 * @see login tramite una chiamata REST in POST.
	 * 
	 * @param authKeys user_key e Session_key
	 * @param sendSMS  l'oggetto {@link SendSMSRequest}
	 * @throws IOException
	 */
	private boolean sendSMS(String baseUrl, String[] authKeys, SendSMSRequest sendSMS) throws IOException {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();

		URL url = new URL(baseUrl + "/sms");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestProperty("user_key", authKeys[0]);
		conn.setRequestProperty("Session_key", authKeys[1]);

		conn.setRequestMethod("POST");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Content-type", "application/json");
		conn.setDoOutput(true);

		String payload = gson.toJson(sendSMS);

		OutputStream os = conn.getOutputStream();
		os.write(payload.getBytes());
		os.flush();

		if (conn.getResponseCode() != 201) {
			String error = "";
			String output;
			BufferedReader errorbuffer = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
			while ((output = errorbuffer.readLine()) != null) {
				error += output;
			}
			System.out.println("Errore " + conn.getResponseCode() + ", \nMessaggio : " + error);
			throw new RuntimeException("ERRORE HTTP");
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

		String response = "";
		String output;
		while ((output = br.readLine()) != null) {
			response += output;
		}
		conn.disconnect();

		SendSMSResponse responseObj = gson.fromJson(response, SendSMSResponse.class);

		return responseObj.isValid();
	}

	public static void main(String[] args) {
		SmsSender smsSender = new SmsSender();
		smsSender.sendSms();
	}
}