package com.mygdx.domain.controller;

import java.util.Random;

import com.mygdx.services.face_recognition.FaceDetectionController;
import com.mygdx.services.persistence.dao.UserDAO;
import com.mygdx.services.persistence.jdbc.UserJDBC;

/**
 * FACADE
 * @author anton
 *
 */
public class Controller {

	private static Controller controller;
	private FaceDetectionController faceController;

	private Controller() {
		faceController = new FaceDetectionController();
	}
	
	public static Controller getController() {
		if( controller == null )
			controller = new Controller();
		return controller;
	}

	public FaceDetectionController getFaceController() {
		return faceController;
	}

	/**
	 * 
	 * @return
	 */
	public UserDAO getUserDAO() {
		return UserJDBC.getUserJDBC();
	}

	public String generatePassword() {
		String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		String chars = CHARS.toLowerCase();
		String string;
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();

		while (salt.length() < 8) {

			Random r = new Random();
			boolean choice = r.nextBoolean();
			string = chars;
			if (choice)
				string = CHARS;

			int index = (int) (rnd.nextFloat() * string.length());
			salt.append(string.charAt(index));
		}
		return salt.toString();
	}

}
