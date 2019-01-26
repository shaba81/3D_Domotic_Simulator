package com.mygdx.controller.database.persistence.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.dbcp2.BasicDataSource;

import com.mygdx.controller.database.model.User;
import com.mygdx.controller.database.persistence.dao.UserDAO;
import com.mygdx.controller.database.persistence.exception.PersistenceException;

import utilis.Configuration;
import utilis.Log;
import utilis.Utils;

public class UserJDBC implements UserDAO {

	private static UserJDBC userJDBC;
	private static BasicDataSource basicDataSource;

	static {
		try {
			System.out.println("B");
			String url = String.format("jdbc:%s://%s:%s/%s", Configuration.jdbc, Configuration.host, Configuration.port,
					Configuration.database);

			String username = Configuration.username;
			String password = Configuration.password;
			String driver = Configuration.driver;

			basicDataSource = new BasicDataSource();
			basicDataSource.setDriverClassName(driver);
			basicDataSource.setUrl(url);
			basicDataSource.setUsername(username);
			basicDataSource.setPassword(password);
			basicDataSource.setMaxIdle(0);
			System.out.println("C");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private UserJDBC() {
		System.out.println("A");
	}

	public static UserJDBC getUserJDBC() {
		if (userJDBC == null)
			userJDBC = new UserJDBC();
		return userJDBC;
	}

	@Override
	public boolean registration(User user) throws Exception {

		Connection conn = null;
		PreparedStatement statement = null;

		try {

			conn = basicDataSource.getConnection();

			String query = Configuration.insertUserNormal;

			if (Utils.isFirstAccess)
				query = Configuration.updateUserAdmin;

			System.out.println(query);
			statement = conn.prepareStatement(query);

			if (!Utils.isFirstAccess) {
				statement.setString(1, user.getIdUser());
				statement.setString(2, user.getEmail());
				statement.setString(3, user.getNickName());
				statement.setString(4, user.getTelefonNumber());
				statement.setString(5, user.getPathImage());
				statement.setBoolean(6, user.isAdministrator());
			} else {
				statement.setString(1, user.getPathImage());
				statement.setString(2, user.getIdUser());
			}

			int result = statement.executeUpdate();

			if (result != 0)
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

	@SuppressWarnings("resource")
	@Override
	public int userExist(String email, String telephoneNumber, String nickName) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			connection = basicDataSource.getConnection();

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

	@SuppressWarnings({ "null" })
	@Override
	public void deleteUtente(String email) throws Exception {
		Connection conn = null;
		PreparedStatement statement = null;

		try {

			conn = basicDataSource.getConnection();

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

	@SuppressWarnings("resource")
	@Override
	public boolean validateUserOneTimePAss(String password, String email) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			connection = basicDataSource.getConnection();
			connection.setAutoCommit(false);

			statement = connection.prepareStatement(Configuration.selectFunction);
			statement.executeQuery();

			statement = null;
			statement = connection.prepareStatement(Configuration.validateUserOneTimePAss);

			statement.setString(1, password);
			statement.setString(2, email);

			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				if (resultSet.getString("pswmatch").equals("t")) {
					statement = null;
					statement = connection.prepareStatement(Configuration.deleteOneTimaPAss);
					statement.setString(1, email);
					statement.executeUpdate();
					connection.commit();
					return true;
				}
			}

			connection.commit();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			connection.rollback();
			throw e;
		} finally {
			connection.setAutoCommit(true);
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

			System.out.println("D");
			connection = basicDataSource.getConnection();

			statement = connection.prepareStatement(Configuration.isFirstRegistrationForThisForniture);

			statement.setLong(1, idSupply);
			statement.setString(2, idUser);

			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				System.out.println("E");
				return false;
			}

			System.out.println("F");
			return true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
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

	@SuppressWarnings("resource")
	@Override
	public String[] updateCredentilsAdministrator(String idUser, Long idSupply, String newPass) throws Exception {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			conn = basicDataSource.getConnection();

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

	@Override
	public String getIdUser() throws Exception {

		Connection connection = null;
		Long id = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		try {

			connection = basicDataSource.getConnection();
			statement = connection.prepareStatement(Configuration.nextId);

			result = statement.executeQuery();
			result.next();
			id = result.getLong("id");

			return id.toString();
		} catch (SQLException e) {
			throw e;
		} finally {
			if (statement != null)
				statement.close();
			if (result != null)
				result.close();
		}
	}

	@Override
	public void insertCommand(String idUser, String command) throws Exception {

		Connection conn = null;
		PreparedStatement statement = null;

		try {

			conn = basicDataSource.getConnection();

			statement = conn.prepareStatement(Configuration.insertCommandLog);

			statement.setString(1, idUser);
			statement.setString(2, command);

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
	public ArrayList<Log> selectCommandLog() throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<Log> logs = new ArrayList<>();

		try {

			connection = basicDataSource.getConnection();

			statement = connection.prepareStatement(Configuration.selectCommandLog);

			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Log log = new Log();
				log.setNickname(resultSet.getString("nick_name"));
				log.setTimestamp(resultSet.getTimestamp("time_request"));
				log.setCommand(resultSet.getString("command"));

				logs.add(log);
			}

			return logs;
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
	public boolean currentlyUserIsAdministrator(String idUser) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			connection = basicDataSource.getConnection();

			statement = connection.prepareStatement(Configuration.currentlyUserIsAdministrator);

			statement.setString(1, idUser);

			resultSet = statement.executeQuery();
			while (resultSet.next()) {

				User user = new User();
				user.setAdministrator(resultSet.getBoolean("is_administrator"));

				return user.isAdministrator();
			}

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
		return false;

	}

	@Override
	public boolean emailIsRegister(String email) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			connection = basicDataSource.getConnection();

			statement = connection.prepareStatement(Configuration.findByEmail);

			statement.setString(1, email);
			resultSet = statement.executeQuery();

			if (resultSet.next())
				return true;

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
	public void updateOneTimePass(String oneTimePass, String email) throws Exception {
		Connection conn = null;
		PreparedStatement statement = null;

		try {

			conn = basicDataSource.getConnection();

			statement = conn.prepareStatement(Configuration.updateOneTimePass);

			statement.setString(1, oneTimePass);
			statement.setString(2, email);

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
	public User getUserByPathImage(String path) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		User user = null;

		try {

			connection = basicDataSource.getConnection();

			statement = connection.prepareStatement(Configuration.selectUserByPathImage);

			statement.setString(1, path);
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				user = new User();
				user.setEmail(resultSet.getString("email"));
				user.setTelefonNumber(resultSet.getString("telephone_number"));
				user.setNickName(resultSet.getString("nick_name"));
				user.setPathImage(resultSet.getString("path_image"));
				user.setAdministrator(resultSet.getBoolean("is_administrator"));
			}

			return user;
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
	public User getUserByEmail(String email) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		User user = null;

		try {

			connection = basicDataSource.getConnection();

			statement = connection.prepareStatement(Configuration.selectUserEmail);

			statement.setString(1, email);
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				user = new User();
				user.setEmail(resultSet.getString("email"));
				user.setTelefonNumber(resultSet.getString("telephone_number"));
				user.setNickName(resultSet.getString("nick_name"));
				user.setPathImage(resultSet.getString("path_image"));
				user.setAdministrator(resultSet.getBoolean("is_administrator"));
			}

			return user;
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
	public HashMap<Integer, User> getAllUser() throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		HashMap<Integer, User> users = new HashMap<>();
		int index = 0;

		try {

			connection = basicDataSource.getConnection();

			statement = connection.prepareStatement(Configuration.selectAllUser);

			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (!resultSet.getString("id_user").equals("-1")) {

					User user = new User();
					user.setEmail(resultSet.getString("email"));
					user.setNickName(resultSet.getString("nick_name"));
					user.setTelefonNumber(resultSet.getString("telephone_number"));
					user.setPathImage(resultSet.getString("path_image"));
					user.setIdUser(resultSet.getString("id_user"));

					users.put(index++, user);
				}
			}

			return users;
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
	public void updateUserCredentials(String email, String telephoneNumbre, String nickName, String pathImage,
			String idUser) throws Exception {
		Connection conn = null;
		PreparedStatement statement = null;

		try {

			conn = basicDataSource.getConnection();

			statement = conn.prepareStatement(Configuration.updateUserCredentials);

			statement.setString(1, email);
			statement.setString(2, telephoneNumbre);
			statement.setString(3, nickName);
			statement.setString(4, pathImage);
			statement.setString(5, idUser);

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

}
