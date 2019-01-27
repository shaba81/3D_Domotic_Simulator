package com.mygdx.controller.proxy;

import com.mygdx.textToSpeech.TextToSpeech;

import utilis.Utils;

public class UserCommand implements AbstractCommand {

	UserAdministratorCommand uac;

	public UserCommand() {
		uac = new UserAdministratorCommand();
	}

	@Override
	public void lightOn() {
		uac.lightOn();
	}

	@Override
	public void tvOn() {
		Utils.saveOnLog(Utils.TV_ON_LOG);
		new TextToSpeech("Mi dispiace il comando non è permesso a questo tipo di utente.");
	}

	@Override
	public void fanOn() {
		uac.fanOn();

	}

	@Override
	public void speakerOn() {
		uac.speakerOn();
		
	}

	@Override
	public void lightOff() {
		uac.lightOff();
		
	}

	@Override
	public void tvOff() {
		uac.tvOff();
	}

	@Override
	public void fanOff() {
		uac.fanOff();
		
	}

	@Override
	public void speakerOff() {
		uac.speakerOff();
	}

	@Override
	public void goToMainMenuScreen() {
		new TextToSpeech("Mi dispiace il comando non è permesso a questo tipo di utente.");
	}

	@Override
	public void help() {
		uac.help();
		
	}

}
