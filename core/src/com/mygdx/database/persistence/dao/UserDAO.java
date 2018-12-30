package com.mygdx.database.persistence.dao;

import com.mygdx.database.model.User;

public interface UserDAO {

	public void registration(User utente) throws Exception;
	public void deleteUtente(String email) throws Exception;
	public User findByEmail(String email) throws Exception;
	public boolean validateUserAdminCredentials(String password, String id) throws Exception;

}
