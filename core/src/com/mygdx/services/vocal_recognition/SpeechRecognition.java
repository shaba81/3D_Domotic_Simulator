package com.mygdx.services.vocal_recognition;


import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;
import com.mygdx.foundation.utils.Utils;

public class SpeechRecognition implements GSpeechResponseListener {

	// funzione che fa iniziare il riconoscimento vocale
	public void startingSpeechRecognition(GSpeechDuplex duplex, Microphone mic) {

		new Thread(() -> {
			try {
				duplex.recognize(mic.getTargetDataLine(), mic.getAudioFormat());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}).start();
	}

	// funziona che stoppa il riconoscimento vocale
	public void stopSpeechRecognition(GSpeechDuplex duplex, Microphone mic) {
		mic.close();
		duplex.stopSpeechRecognition();
	}

	// con questo metodo ricaviamo il testo della cattura audio
	public void getResponse(GSpeechDuplex duplex) {

		duplex.addResponseListener(new GSpeechResponseListener() {

			public void onResponse(GoogleResponse gr) {
				String output = "";
				output = gr.getResponse();
				if (output.contains("(")) {
					output = output.substring(0, output.indexOf('('));
				}
				if (!gr.getOtherPossibleResponses().isEmpty()) {
					output = output + " (" + (String) gr.getOtherPossibleResponses().get(0) + ")";
				}

				Utils.resp = output;
			}
		});
	}

	@Override
	public void onResponse(GoogleResponse gr) {
	}
}