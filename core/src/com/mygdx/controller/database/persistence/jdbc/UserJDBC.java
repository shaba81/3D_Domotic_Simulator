package com.mygdx.controller.database.persistence.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import com.badlogic.gdx.Gdx;
import com.mygdx.controller.database.model.User;
import com.mygdx.controller.database.persistence.PostgreDAOFactory;
import com.mygdx.controller.database.persistence.dao.UserDAO;
import com.mygdx.controller.database.persistence.exception.PersistenceException;

import utilis.Configuration;
import utilis.Utils;

public class UserJDBC implements UserDAO {

	private BasicDataSource basicDataSource;

	public UserJDBC(BasicDataSource basicDataSource) {
		this.basicDataSource = basicDataSource;
	}

	@Override
	public boolean registration(User user) throws Exception {

		Connection conn = null;
		PreparedStatement statement = null;

		try {

			conn = basicDataSource.getConnection();

			// Configuration config = (Configuration) Utils.getJsonFile(Configuration.class,
			// Utils.DB_PATH_QUERY);
			String query = Configuration.insertUserNormal;

			if( Utils.isFirstAccess )
				query = Configuration.insertUserAdmin;

			statement = conn.prepareStatement(query);
			statement.setString(1, user.getEmail());
			statement.setString(2, user.getNickName());
			statement.setString(3, user.getTelefonNumber());
			statement.setString(4, user.getPathImage());
			statement.setBoolean(5, user.isAdministrator());

			int result = statement.executeUpdate();

			if( result != 0 )
				return true;

			return false;

		} catch (SQLException e) {

			if (e.getSQLState().equals("23505")) {
				throw new PersistenceException(10006L);
			} else {
				System.out.println(e.getMessage());
				throw e;
			}

		} finally {
			try {
				if (statement != null)
					statement.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				throw e;
			}
		}

	}

	@Override
	public int userExist(String email, String telephoneNumber, String nickName) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			connection = basicDataSource.getConnection();

			// Configuration config = (Configuration) Utils.getJsonFile(Configuration.class,
			// Utils.DB_PATH_QUERY);
			/*
			 * Check is the check is separeted to give the client more detail of the mistake
			 * made.
			 * 
			 * Check if email, telephoneNumber and nickName exist.
			 */
			statement = connection.prepareStatement(Configuration.checkIfEmailExist);

			statement.setString(1, email);
			resultSet = statement.executeQuery();

			if (resultSet.next())
				return 1;

			/*
			 * Check if telephoneNumber exist.
			 */
			resultSet = null;
			statement = null;

			statement = connection.prepareStatement(Configuration.checkIfTelephoneNumberExist);
			statement.setString(1, telephoneNumber);
			resultSet = statement.executeQuery();

			if (resultSet.next())
				return 2;

			/*
			 * Check if nickName exist.
			 */
			resultSet = null;
			statement = null;

			statement = connection.prepareStatement(Configuration.checkIfNickNameExist);
			statement.setString(1, nickName);
			resultSet = statement.executeQuery();

			if (resultSet.next())
				return 3;

			return -1;

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw e;
		} finally {
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

			// Configuration config = (Configuration) Utils.getJsonFile(Configuration.class,
			// Utils.DB_PATH_QUERY);

			// statement = conn.prepareStatement(config.deleteUtenteByEmail);
			statement.setString(1, email);

			statement.executeUpdate();

		} catch (SQLException e) {

			if (e.getSQLState().equals("23505")) {
				throw new PersistenceException(10006L);
			} else {
				System.out.println(e.getMessage());
				throw e;
			}

		} finally {
			try {
				if (statement != null)
					statement.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
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

			// Configuration config = (Configuration) Utils.getJsonFile(Configuration.class,
			// Gdx.files.internal("query.json"));
			statement = connection.prepareStatement(Configuration.validateUserAdminPass);

			statement.setString(1, password);
			statement.setString(2, id);

			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				if (resultSet.getString("pswmatch").equals("t"))
					return true;
			}

			return false;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw e;
		} finally {
			if (resultSet != null)
				resultSet.close();
			if (statement != null)
				statement.close();
			if (connection != null)
				connection.close();
		}
	}

	@Override
	public boolean isFirstRegistrationForThisForniture(Long idSupply, String idUser) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			connection = basicDataSource.getConnection();

			// Configuration config = (Configuration) Utils.getJsonFile(Configuration.class,
			// Gdx.files.internal("query.json"));
			statement = connection.prepareStatement(Configuration.isFirstRegistrationForThisForniture);

			statement.setLong(1, idSupply);
			statement.setString(2, idUser);

			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				if (resultSet.getString("email").equals("") ) {
					return true;
				}
			}

			return false;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw e;
		} finally {
			if (resultSet != null)
				resultSet.close();
			if (statement != null)
				statement.close();
			if (connection != null)
				connection.close();
		}
	}

	@Override
	public String[] updateCredentilsAdministrator(String idUser, Long idSupply, String newPass) throws Exception {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			conn = basicDataSource.getConnection();

			// Configuration config = (Configuration) Utils.getJsonFile(Configuration.class,
			// Utils.DB_PATH_QUERY);

			/*
			 * Update pass
			 */
			statement = conn.prepareStatement(Configuration.updateUserAdminPass);
			statement.setString(1, newPass);
			statement.setString(2, idUser);
			statement.setLong(3, idSupply);

			statement.executeUpdate();

			/*
			 * Select admin email
			 */
			statement = null;
			statement = conn.prepareStatement(Configuration.findById);
			statement.setString(1, idUser);

			resultSet = statement.executeQuery();

			String[] emailCredentialsAdmin = new String[2];
			if (resultSet.next()) {
				emailCredentialsAdmin[0] = resultSet.getString("email");
				emailCredentialsAdmin[1] = resultSet.getString("telephone_number");
			}

			return emailCredentialsAdmin;			
		} catch (SQLException e) {

			if (e.getSQLState().equals("23505")) {
				throw new PersistenceException(10006L);
			} else {
				System.out.println(e.getMessage());
				throw e;
			}

		} finally {
			try {
				if (statement != null)
					statement.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				throw e;
			}
		}
	}

}