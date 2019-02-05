package com.mygdx.services.persistence.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mygdx.domain.models.User;
import com.mygdx.foundation.utils.Log;

public interface UserDAO {

	public String getIdUser()throws SQLException;
	public boolean registration(User user) throws SQLException;
	public void deleteUtente(String email) throws SQLException;
	public int userExist(String email, String telephoneNumber, String nickName ) throws SQLException;
	public boolean validateUserAdminCredentials(String password, String id) throws SQLException;
	public boolean validateUserOneTimePAss(String password, String email) throws SQLException;
	public boolean isFirstRegistrationForThisForniture(Long idSupply, String idUser) throws SQLException;
	public String[] updateCredentilsAdministrator(String idUser, Long idSupply, String newPass) throws SQLException;
	public void insertCommand(String idUser, String command) throws SQLException;
	public ArrayList<Log> selectCommandLog() throws SQLException;
	public boolean currentlyUserIsAdministrator(String email) throws SQLException;
	public boolean emailIsRegister(String email) throws SQLException;
	public void updateOneTimePass(String oneTimePass, String email) throws SQLException;
	public User getUserByPathImage(String path) throws SQLException;
	public User getUserByEmail(String email) throws SQLException;
	public HashMap<Integer, User> getAllUser() throws SQLException;
	public void updateUserCredentials(String email, String telephoneNumbre, String nickName, String pathImage, String idUser) throws SQLException;
	public boolean registrationIsAvailable() throws SQLException;
	public int userChangeCredentials(String idUser, String email, String telephoneNumber, String nickName) throws SQLException;

}
