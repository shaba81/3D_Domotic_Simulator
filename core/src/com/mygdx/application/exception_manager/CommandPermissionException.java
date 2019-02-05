package com.mygdx.application.exception_manager;

import com.mygdx.services.vocal_recognition.TextToSpeech;

public class CommandPermissionException{

	private static CommandPermissionException commandPermissionException;

	private CommandPermissionException() {
	}

	public static CommandPermissionException getCommandPermissionException() {
		if (commandPermissionException == null)
			commandPermissionException = new CommandPermissionException();

		return commandPermissionException;
	}
	
	public void messageException(String message) {
		new TextToSpeech(message);
	}
}
