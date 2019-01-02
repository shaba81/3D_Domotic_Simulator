package com.mygdx.database.persistence.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import com.badlogic.gdx.Gdx;
import com.mygdx.database.model.User;
import com.mygdx.database.persistence.PostgreDAOFactory;
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
	public void registration(User user) throws Exception {

		Connection conn = null;
		PreparedStatement statement = null;

		try {

			conn = basicDataSource.getConnection();

			//Configuration config = (Configuration) Utils.getJsonFile(Configuration.class, Utils.DB_PATH_QUERY);
			statement = conn.prepareStatement(Configuration.insertUser);

			statement.setString(1, user.getEmail());
			statement.setString(2, user.getNickName());
			statement.setString(3, user.getTelefonNumber());
			statement.setString(4, user.getPathImage());
			statement.setBoolean(5, user.isAdministrator());

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
	public int userExist(String email, String telephoneNumber, String nickName ) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			connection = basicDataSource.getConnection();

		//	Configuration config = (Configuration) Utils.getJsonFile(Configuration.class, Utils.DB_PATH_QUERY);
			/*
			 * Check is the check is separeted to give the client more detail of the mistake made.
			 * 
			 * Check if email, telephoneNumber and nickName exist.
			 */
			statement = connection.prepareStatement(Configuration.checkIfEmailExist);

			statement.setString(1, email);
			resultSet = statement.executeQuery();

			if (resultSet.next()) 
				return 1;

			/*
			 * Check if  telephoneNumber exist.
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

			//Configuration config = (Configuration) Utils.getJsonFile(Configuration.class, Utils.DB_PATH_QUERY);

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

	
	@Override
	public boolean isFirstRegistrationForThisForniture(Long idSupply) throws Exception {
		return false;
	}

}
