package com.mygdx.database.persistence.dao;

import com.mygdx.database.model.User;

public interface UserDAO {

	public void registration(User user) throws Exception;
	public void deleteUtente(String email) throws Exception;
	public int userExist(String email, String telephoneNumber, String nickName ) throws Exception;
	public boolean validateUserAdminCredentials(String password, String id) throws Exception;
	public boolean isFirstRegistrationForThisForniture(Long idSupply) throws Exception;

}
