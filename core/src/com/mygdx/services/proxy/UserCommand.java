package com.mygdx.services.proxy;

import java.sql.SQLException;

import com.mygdx.application.exception_manager.CommandPermissionException;
import com.mygdx.foundation.utils.Utils;
import com.mygdx.services.vocal_recognition.TextToSpeech;

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
		uac.tvOn();
	}

	@Override
	public void fanOn() throws SQLException {
		uac.fanOn();

	}

	@Override
	public void openDoorA() throws SQLException {
		uac.openDoorA();
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
		CommandPermissionException.getCommandPermissionException().messageException("Mi dispiace il comando non è permesso a questo tipo di utente.");
	}

	@Override
	public void help() throws SQLException {
		uac.help();
	}


	@Override
	public void safeBoxOn() throws Exception {
		Utils.resp = "";
		CommandPermissionException.getCommandPermissionException().messageException("Mi dispiace il comando non è permesso a questo tipo di utente.");
	}

	@Override
	public void safeBoxClose() throws Exception {
		uac.safeBoxClose();
		
	}

}
