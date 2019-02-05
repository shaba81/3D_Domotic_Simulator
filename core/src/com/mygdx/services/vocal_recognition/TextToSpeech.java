package com.mygdx.services.vocal_recognition;

import java.io.IOException;

import com.darkprograms.speech.synthesiser.SynthesiserV2;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

public class TextToSpeech {

	String message;
	// creiamo un sintetizzatore con la relativa chiave google
	SynthesiserV2 synthesizer = new SynthesiserV2("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");

	// costrutore di prova
	public TextToSpeech(String message) {
		// prova
		synthesizer.setLanguage("it");
	
		this.message = message;
		speak(this.message);
	}

	// funzione che riproduce la stringa che gli si passa
	public void speak(String text) {

		// creiamo un nuovo thread per evitare lag
		Thread thread = new Thread(() -> {
			try {

				// Creiamo un JPLayer
				AdvancedPlayer player = new AdvancedPlayer(synthesizer.getMP3Data(text));
				player.play();

			} catch (IOException | JavaLayerException e) {

				System.out.println("Wrong");// eccezione

			}
		});

		// non facciamo terminare l'app prima del thread
		thread.setDaemon(false);

		// avviamo thread
		thread.start();

	}

}
