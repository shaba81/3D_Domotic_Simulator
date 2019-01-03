package com.mygdx.controller.database.persistence.exception;

/**
 * Classe per la gestione degli errori
 * @author anton
 *
 */
public class PersistenceException extends Exception {

	public PersistenceException(long l) {
		System.out.println(this.getMessage());
	}

}
