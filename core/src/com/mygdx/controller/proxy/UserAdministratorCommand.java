package com.mygdx.controller.proxy;

import java.sql.SQLException;

import com.mygdx.game.GameScreen;
import com.mygdx.game.ScreenManager;
import com.mygdx.simulator.factory_methos_screens.MainMenuScreenCreator;

import utilis.Utils;

public class UserAdministratorCommand implements AbstractCommand {

	@Override
	public void lightOn() throws SQLException {
		GameScreen.getGameScreen().inputManager.isLightOn = true;
		Utils.commandLog("luce", "lampada", Utils.LIGHT_ON_LOG);
	}

	@Override
	public void tvOn() throws SQLException {
		GameScreen.getGameScreen().inputManager.isTvOn = true;
		Utils.commandLog("tv", "televisione", Utils.TV_ON_LOG);
	}

	@Override
	public void fanOn() throws SQLException {
		GameScreen.getGameScreen().inputManager.activateFan = true;
		Utils.commandLog("ventilatore", "aria", Utils.FAN_ON_LOG);
	}

	@Override
	public void speakerOn() throws SQLException {
		GameScreen.getGameScreen().inputManager.activateSpeaker = true;
		Utils.commandLog("stereo", "radio", Utils.RADIO_ON_LOG);
	}

	@Override
	public void lightOff() throws SQLException {
		GameScreen.getGameScreen().inputManager.isLightOn = false;
		Utils.commandLog("luce", "lampada", Utils.LIGHT_OFF_LOG);
	}

	@Override
	public void tvOff() throws SQLException {
		GameScreen.getGameScreen().inputManager.isTvOn = false;
		Utils.commandLog("tv", "televisione", Utils.TV_OFF_LOG);
	}

	@Override
	public void fanOff() {
		GameScreen.getGameScreen().inputManager.activateFan = false;
	}

	@Override
	public void speakerOff() throws SQLException {
		GameScreen.getGameScreen().inputManager.activateSpeaker = false;
		Utils.commandLog("stereo", "radio", Utils.RADIO_OFF_LOG);
	}

	@Override
	public void goToMainMenuScreen() {
		ScreenManager.getInstance().showScreen(new MainMenuScreenCreator());
	}

	@Override
	public void help() {
		GameScreen.getGameScreen().inputManager.help = true;
		GameScreen.getGameScreen().hc = false;
		Utils.resp = "";

	}

}
