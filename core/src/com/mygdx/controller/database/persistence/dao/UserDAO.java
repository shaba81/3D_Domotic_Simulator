package com.mygdx.controller.database.persistence.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mygdx.controller.database.model.User;

import utilis.Log;

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

}
