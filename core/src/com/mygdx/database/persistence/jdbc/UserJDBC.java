package com.mygdx.database.persistence.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import com.badlogic.gdx.Gdx;
import com.mygdx.database.model.User;
import com.mygdx.database.persistence.dao.UserDAO;
import com.mygdx.database.persistence.exception.PersistenceException;

import utilis.Configuration;
import utilis.Utils;

public class UserJDBC implements UserDAO {

	private BasicDataSource basicDataSource;

	public UserJDBC(BasicDataSource basicDataSource) {
		this.basicDataSource = basicDataSource;
	}

	@Override
	public void registration(User utente) throws Exception {

		Connection conn = null;
		PreparedStatement statement = null;

		try {

			conn = basicDataSource.getConnection();

			Configuration config = (Configuration) Utils.getJsonFile(Configuration.class, Utils.DB_PATH_QUERY);
			statement = conn.prepareStatement(config.insertUser);

			statement.setString(1, utente.getEmail());
			statement.setString(2, utente.getNickName());
			statement.setString(3, utente.getTelefonNumber());
			statement.setString(4, utente.getPathImage());
			statement.setBoolean(5, utente.isAdministrator());

			statement.executeUpdate();

		} catch (SQLException e) {

			if (e.getSQLState().equals("23505")) {
				throw new PersistenceException(10006L);
			} else {
				throw e;
			}

		} finally {
			try {
				if (statement != null)
					statement.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}

	}

	@Override
	public User findByEmail(String email) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			connection = basicDataSource.getConnection();

			Configuration config = (Configuration) Utils.getJsonFile(Configuration.class, Utils.DB_PATH_QUERY);
			statement = connection.prepareStatement(config.findByEmail);

			statement.setString(1, email);
			resultSet = statement.executeQuery();

			User user = null;

			if (resultSet.next()) {

				if (user == null) {
					user = new User();
				}

				user.setEmail(resultSet.getString("email"));
				user.setNickName(resultSet.getString("nick_name"));
				user.setPathImage(resultSet.getString("image_path"));
				user.setTelefonNumber(resultSet.getString("phone_number"));

			}

			return user;

		} catch (SQLException e) {
			throw e;
		}  finally {
			if (resultSet != null)
				resultSet.close();
			if (statement != null)
				statement.close();
			if (connection != null)
				connection.close();
		}

	}

	@Override
	public void deleteUtente(String email) throws Exception {
		Connection conn = null;
		PreparedStatement statement = null;

		try {
			
			conn = basicDataSource.getConnection();

			Configuration config = (Configuration) Utils.getJsonFile(Configuration.class, Utils.DB_PATH_QUERY);

		//	statement = conn.prepareStatement(config.deleteUtenteByEmail);
			statement.setString(1, email);

			statement.executeUpdate();

		} catch (SQLException e) {

			if (e.getSQLState().equals("23505")) {
				throw new PersistenceException(10006L);
			} else {
				throw e;
			}

		} finally {
			try {
				if (statement != null)
					statement.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	@Override
	public boolean validateUserAdminCredentials(String password, String id) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			connection = basicDataSource.getConnection();

			//Configuration config = (Configuration) Utils.getJsonFile(Configuration.class, Gdx.files.internal("query.json"));
			statement = connection.prepareStatement(Configuration.validateUserAdminPass);

			statement.setString(1, password);
			statement.setString(2, id);

			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				if( resultSet.getString("pswmatch").equals("t")  )
					return true;
			}

			return false;
		} catch (SQLException e) {
			throw e;
		}  finally {
			if (resultSet != null)
				resultSet.close();
			if (statement != null)
				statement.close();
			if (connection != null)
				connection.close();
		}
	}

}
