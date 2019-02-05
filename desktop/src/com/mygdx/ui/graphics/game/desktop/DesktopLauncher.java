package com.mygdx.ui.graphics.game.desktop;

import org.opencv.core.Core;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.ui.graphics.game.Drop;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "HOUSIM - face detection and vocal recognition house simulator";
		config.width = 1366;
		config.height = 768;
		config.addIcon("resources/logo.png", FileType.Internal);
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new LwjglApplication(new Drop(), config);
	}
}