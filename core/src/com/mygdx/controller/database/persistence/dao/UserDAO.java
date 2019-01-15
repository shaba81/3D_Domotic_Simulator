package com.mygdx.controller.database.persistence.dao;

import com.mygdx.controller.database.model.User;

public interface UserDAO {

	public boolean registration(User user) throws Exception;
	public void deleteUtente(String email) throws Exception;
	public int userExist(String email, String telephoneNumber, String nickName ) throws Exception;
	public boolean validateUserAdminCredentials(String password, String id) throws Exception;
	public boolean validateUserOneTimePAss(String password, String email) throws Exception;
	public boolean isFirstRegistrationForThisForniture(Long idSupply, String idUser) throws Exception;
	public String[] updateCredentilsAdministrator(String idUser, Long idSupply, String newPass) throws Exception;

}
