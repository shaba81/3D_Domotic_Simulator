package com.mygdx.controller.proxy;

import java.sql.SQLException;

import com.mygdx.textToSpeech.TextToSpeech;

import utilis.Utils;

public class UserCommand implements AbstractCommand {

	UserAdministratorCommand uac;

	public UserCommand() {
		uac = new UserAdministratorCommand();
	}

	@Override
	public void lightOn() throws SQLException {
		uac.lightOn();
	}

	@Override
	public void tvOn() throws SQLException {
		Utils.saveOnLog(Utils.TV_ON_LOG);
		new TextToSpeech("Mi dispiace il comando non è permesso a questo tipo di utente.");
	}

	@Override
	public void fanOn() throws SQLException {
		uac.fanOn();

	}
	
	@Override
	public void openDoorA() throws SQLException {
		uac.openDoorA();
		//new TextToSpeech("Mi dispiace il comando non è permesso a questo tipo di utente.");

	}
	
	@Override
	public void openDoorB() throws SQLException {
		uac.openDoorB();

	}

	@Override
	public void speakerOn() throws SQLException {
		uac.speakerOn();
		
	}

	@Override
	public void lightOff() throws SQLException {
		uac.lightOff();
		
	}

	@Override
	public void tvOff() throws SQLException {
		uac.tvOff();
	}

	@Override
	public void fanOff() {
		uac.fanOff();
		
	}

	@Override
	public void speakerOff() throws SQLException {
		uac.speakerOff();
	}

	@Override
	public void goToMainMenuScreen() {
		new TextToSpeech("Mi dispiace il comando non è permesso a questo tipo di utente.");
	}

	@Override
	public void help() throws SQLException {
		uac.help();
	}


	@Override
	public void safeBoxOn() throws Exception {
		uac.safeBoxOn();
		
	}

	@Override
	public void safeBoxClose() throws Exception {
		uac.safeBoxClose();
		
	}

}
