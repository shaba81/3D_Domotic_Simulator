package com.mygdx.controller;

import java.util.Random;

import org.apache.commons.dbcp2.BasicDataSource;

import com.mygdx.controller.database.persistence.dao.UserDAO;
import com.mygdx.controller.database.persistence.jdbc.UserJDBC;

import utilis.Configuration;

public class Controller {

	private static Controller controller;

	private Controller() {}
	
	public static Controller getController() {
		if( controller == null )
			controller = new Controller();
		return controller;
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
