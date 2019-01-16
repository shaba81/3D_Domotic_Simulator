package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.mygdx.speechToText.SpeechRecognition;

import net.sourceforge.javaflacencoder.FLACFileWriter;

public class InputManager implements InputProcessor {

	private int dragX, dragY;
	public float movementSpeed = 25f;
	public boolean forward = false;
	public boolean back = false;
	public boolean left = false;
	public boolean right = false;
	public boolean isSpeaking = false;
	public boolean doCommand = false;
	public boolean activateFan = false;
	public boolean isLightOn = false;
	public boolean isTvOn = false;
	public boolean activateSpeaker = false;
	public boolean nAccessButton;
	float rotateSpeed = 0.05f;
	private PerspectiveCamera camera;

	private SpeechRecognition speechRecognition;
	final Microphone mic = new Microphone(FLACFileWriter.FLAC);
	GSpeechDuplex duplex = new GSpeechDuplex("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");

	public InputManager(PerspectiveCamera cam) {
		camera = cam;
		// inizializzazione e settaggio della lingua italiana (funziona anche in
		// inglese)
		speechRecognition = new SpeechRecognition();
		duplex.setLanguage("it");
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.W) {
			forward = true;
		}
		if (keycode == Input.Keys.A) {
			left = true;
		}
		if (keycode == Input.Keys.S) {
			back = true;
		}
		if (keycode == Input.Keys.D) {
			right = true;
		}
		if (keycode == Input.Keys.T) {
			isTvOn = !isTvOn;
		}
		if (keycode == Input.Keys.L) {
			isLightOn = !isLightOn;
		}
		if (keycode == Input.Keys.R) {
			// messagesTable.clear();
			isSpeaking = true;
			doCommand = false;
			speechRecognition.startingSpeechRecognition(duplex, mic);
			speechRecognition.getResponse(duplex);

		}
		if (keycode == Input.Keys.B) {
			activateSpeaker = !activateSpeaker;
		}
		if (keycode == Input.Keys.V) {
			activateFan = !activateFan;
		}
		if (keycode == Input.Keys.N) {
			nAccessButton = true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.W) {
			forward = false;
		}
		if (keycode == Input.Keys.A) {
			left = false;
		}
		if (keycode == Input.Keys.S) {
			back = false;
		}
		if (keycode == Input.Keys.D) {
			right = false;
		}
		if (keycode == Input.Keys.R) {
			isSpeaking = false;
			speechRecognition.stopSpeechRecognition(duplex, mic);
			doCommand = true;

		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		Vector3 direction = camera.direction.cpy();

		// rotating on the y axis
		float x = dragX - screenX;

		camera.rotate(Vector3.Y, x * rotateSpeed);

		// rotating on the x and z axis is different
		float y = (float) Math.sin((double) (dragY - screenY) / 180f);
		if (Math.abs(camera.direction.y + y * (rotateSpeed * 5.0f)) < 0.9) {
			camera.direction.y += y * (rotateSpeed * 5.0f);
		}

		camera.update();
		dragX = screenX;
		dragY = screenY;

		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
