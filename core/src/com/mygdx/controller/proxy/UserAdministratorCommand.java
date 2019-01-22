package com.mygdx.controller.proxy;

import com.mygdx.game.GameScreen;

import utilis.Utils;

public class UserAdministratorCommand implements AbstractCommand {

	@Override
	public void lightOn() {
		GameScreen.getGameScreen().inputManager.isLightOn = true;
		Utils.commandLog("luce", "lampada");

	}

	@Override
	public void tvOn() {
		GameScreen.getGameScreen().inputManager.isTvOn = true;
		Utils.commandLog("tv", "televisione");

	}

	@Override
	public void fanOn() {
		GameScreen.getGameScreen().inputManager.activateFan = true;
		Utils.commandLog("ventilatore", "aria");

	}

	@Override
	public void speakerOn() {
		GameScreen.getGameScreen().inputManager.activateSpeaker = true;
		Utils.commandLog("stereo", "radio");

	}

	@Override
	public void lightOff() {
		GameScreen.getGameScreen().inputManager.isLightOn = false;
		Utils.commandLog("luce", "lampada");

	}

	@Override
	public void tvOff() {
		GameScreen.getGameScreen().inputManager.isTvOn = false;
		Utils.commandLog("tv", "televisione");

	}

	@Override
	public void fanOff() {
		GameScreen.getGameScreen().inputManager.activateFan = false;
		Utils.commandLog("ventilatore", "aria");
	}

	@Override
	public void speakerOff() {
		GameScreen.getGameScreen().inputManager.activateSpeaker = false;
		Utils.commandLog("stereo", "radio");

	}

}
