package com.mygdx.foundation.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.sound.sampled.FloatControl;

import com.mygdx.application.exception_manager.ExceptionsManager;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Music {
	FileInputStream fileInputStream;
	Player player;

	public Music() {
		start();
	}

	public void start() {
		Thread thread = new Thread(() -> {
			try {
				fileInputStream = new FileInputStream("resources/song1.mp3");
				player = new Player(fileInputStream);
				player.play();
				Utils.songPlay = true;

			} catch (FileNotFoundException | JavaLayerException e) {
				System.out.println("File not found");
			}
		});

		// non facciamo terminare l'app prima del thread
		thread.setDaemon(false);

		// avviamo thread
		thread.start();
	}

	public void close() {
		player.close();
		Utils.songPlay = false;
	}
}
