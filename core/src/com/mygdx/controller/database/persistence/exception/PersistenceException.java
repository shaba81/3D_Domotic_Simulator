package com.mygdx.controller.database.persistence.exception;

/**
 * Classe per la gestione degli errori
 * @author anton
 *
 */
public class PersistenceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PersistenceException(long l) {
		System.out.println(this.getMessage());
	}

}
