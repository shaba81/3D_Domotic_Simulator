package com.mygdx.controller.proxy;

import com.mygdx.game.GameScreen;

import utilis.Utils;

public class UserAdministratorCommand implements AbstractCommand {

	@Override
	public void lightOn() {
		GameScreen.getGameScreen().inputManager.isLightOn = true;
		Utils.commandLog("luce", "lampada",Utils.LIGHT_ON_LOG);
	}

	@Override
	public void tvOn() {
		GameScreen.getGameScreen().inputManager.isTvOn = true;
		Utils.commandLog("tv", "televisione", Utils.TV_ON_LOG);
	}

	@Override
	public void fanOn() {
		GameScreen.getGameScreen().inputManager.activateFan = true;
		Utils.commandLog("ventilatore", "aria",Utils.FAN_ON_LOG);
	}

	@Override
	public void speakerOn() {
		GameScreen.getGameScreen().inputManager.activateSpeaker = true;
		Utils.commandLog("stereo", "radio",Utils.RADIO_ON_LOG);
	}

	@Override
	public void lightOff() {
		GameScreen.getGameScreen().inputManager.isLightOn = false;
		Utils.commandLog("luce", "lampada", Utils.LIGHT_OFF_LOG);
	}

	@Override
	public void tvOff() {
		GameScreen.getGameScreen().inputManager.isTvOn = false;
		Utils.commandLog("tv", "televisione",Utils.TV_OFF_LOG);
	}

	@Override
	public void fanOff() {
		GameScreen.getGameScreen().inputManager.activateFan = false;
	}

	@Override
	public void speakerOff() {
		GameScreen.getGameScreen().inputManager.activateSpeaker = false;
		Utils.commandLog("stereo", "radio",Utils.RADIO_OFF_LOG);
	}

}
