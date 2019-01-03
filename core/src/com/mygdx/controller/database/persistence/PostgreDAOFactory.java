package com.mygdx.controller.database.persistence;

import org.apache.commons.dbcp2.*;

import com.mygdx.controller.database.persistence.dao.UserDAO;
import com.mygdx.controller.database.persistence.jdbc.UserJDBC;

import utilis.Configuration;

public class PostgreDAOFactory {

	private static BasicDataSource basicDataSource;

	static {
		try {
			//Configuration config = (Configuration) Utils.getJsonFile(Configuration.class, Utils.CONFIG_PATH_DB);

			String url = String.format("jdbc:%s://%s:%s/%s", Configuration.jdbc, Configuration.host, Configuration.port, Configuration.database);

			String username = Configuration.username;
			String password = Configuration.password;
			String driver = Configuration.driver;

			basicDataSource = new BasicDataSource();
			basicDataSource.setDriverClassName(driver);
			basicDataSource.setUrl(url);
			basicDataSource.setUsername(username);
			basicDataSource.setPassword(password);
			basicDataSource.setMaxIdle(0);
		} catch (Exception e) {
			
		}
	}

	/**
	 * Metodo fabbrica
	 * @return
	 */
	public UserDAO getUtenteDAO() {
		return new UserJDBC(basicDataSource);
	}

}
