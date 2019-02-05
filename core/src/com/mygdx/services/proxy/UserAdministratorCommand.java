package com.mygdx.services.proxy;

import java.sql.SQLException;

import com.mygdx.application.screen_manager.ScreenManager;
import com.mygdx.foundation.utils.Utils;
import com.mygdx.services.factory_methos_screens.MainMenuScreenCreator;
import com.mygdx.ui.graphics.game.Housim;

public class UserAdministratorCommand implements AbstractCommand {

	@Override
	public void lightOn() throws SQLException {
		Housim.getHousim().inputManager.isLightOn = true;
		Utils.commandLog("luce", "lampada", Utils.LIGHT_ON_LOG);
	}

	@Override
	public void tvOn() throws SQLException {
		Housim.getHousim().inputManager.isTvOn = true;
		Utils.commandLog("tv", "televisione", Utils.TV_ON_LOG);
	}

	@Override
	public void fanOn() throws SQLException {
		Housim.getHousim().inputManager.activateFan = true;
		Utils.commandLog("ventilatore", "aria", Utils.FAN_ON_LOG);
	}

	@Override
	public void speakerOn() throws SQLException {
		Housim.getHousim().inputManager.activateSpeaker = true;
		Utils.commandLog("stereo", "radio", Utils.RADIO_ON_LOG);
	}
	
	@Override
	public void openDoorA() throws SQLException {
		Housim.getHousim().inputManager.openDoorA = true;
		Utils.commandLog("porta A", "porta uno", Utils.DOORA_OPEN_LOG);
		
	}

	@Override
	public void openDoorB() throws SQLException {
		Housim.getHousim().inputManager.openDoorB = true;
		Utils.commandLog("porta B", "porta due", Utils.DOORB_OPEN_LOG);
		
	}
	

	@Override
	public void lightOff() throws SQLException {
		Housim.getHousim().inputManager.isLightOn = false;
		Utils.commandLog("luce", "lampada", Utils.LIGHT_OFF_LOG);
	}

	@Override
	public void tvOff() throws SQLException {
		Housim.getHousim().inputManager.isTvOn = false;
		Utils.commandLog("tv", "televisione", Utils.TV_OFF_LOG);
	}

	@Override
	public void fanOff() {
		Housim.getHousim().inputManager.activateFan = false;
	}

	@Override
	public void speakerOff() throws SQLException {
		Housim.getHousim().inputManager.activateSpeaker = false;
		Utils.commandLog("stereo", "radio", Utils.RADIO_OFF_LOG);
	}

	@Override
	public void goToMainMenuScreen() {
		ScreenManager.getInstance().showScreen(new MainMenuScreenCreator());
	}

	@Override
	public void help() throws SQLException {
		Housim.getHousim().inputManager.help = true;
		Housim.getHousim().hc = false;
		Utils.resp = "";
		Utils.commandLog("aiuto", "", Utils.HELP_LOG);
	}

	@Override
	public void safeBoxOn() throws Exception {
		Housim.getHousim().inputManager.safeBox = true;
		Utils.commandLog("cassaforte", "", Utils.SAFEBOX_ON_LOG);
		
	}

	@Override
	public void safeBoxClose() throws Exception {
		Housim.getHousim().inputManager.safeBox = false;
		Utils.commandLog("cassaforte", "", Utils.SAFEBOX_OFF_LOG);
		
	}



	

}
