package com.mygdx.controller.database.persistence.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mygdx.controller.database.model.User;

import utilis.Log;

public interface UserDAO {

	public String getIdUser()throws Exception;
	public boolean registration(User user) throws Exception;
	public void deleteUtente(String email) throws Exception;
	public int userExist(String email, String telephoneNumber, String nickName ) throws Exception;
	public boolean validateUserAdminCredentials(String password, String id) throws Exception;
	public boolean validateUserOneTimePAss(String password, String email) throws Exception;
	public boolean isFirstRegistrationForThisForniture(Long idSupply, String idUser) throws Exception;
	public String[] updateCredentilsAdministrator(String idUser, Long idSupply, String newPass) throws Exception;
	public void insertCommand(String idUser, String command) throws Exception;
	public ArrayList<Log> selectCommandLog() throws Exception;
	public boolean currentlyUserIsAdministrator(String email) throws Exception;
	public boolean emailIsRegister(String email) throws Exception;
	public void updateOneTimePass(String oneTimePass, String email) throws Exception;
	public User getUserByPathImage(String path) throws Exception;
	public User getUserByEmail(String email) throws Exception;
	public HashMap<Integer, User> getAllUser() throws Exception;
	public void updateUserCredentials(String email, String telephoneNumbre, String nickName, String pathImage, String idUser) throws Exception;

}
