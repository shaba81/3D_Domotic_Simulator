package com.mygdx.speechToText;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;

import net.sourceforge.javaflacencoder.FLACFileWriter;

public class GoogleSpeechRecognition implements GSpeechResponseListener {

	public static void main(String[] args) {

		// microfono
		final Microphone mic = new Microphone(FLACFileWriter.FLAC);
		// creiamo il duplex con la relativa chiave dei servizi Google Cloud
		GSpeechDuplex duplex = new GSpeechDuplex("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");

		// settiamo la lingua
		duplex.setLanguage("it"); // funziona anche in inglese

		new Thread(() -> {
			startingSpeechRecognition(duplex, mic);
		}).start();
		getResponse(duplex);

	}

	// funzione che fa iniziare il riconoscimento vocale
	public void startingSpeechRecognition(GSpeechDuplex duplex, Microphone mic) {
		try {
			duplex.recognize(mic.getTargetDataLine(), mic.getAudioFormat());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// funziona che stoppa il riconoscimento vocale
	public void stopSpeechRecognition(GSpeechDuplex duplex, Microphone mic) {
		mic.close();
		duplex.stopSpeechRecognition();
	}

	// con questo metodo ricaviamo il testo della cattura audio
	public static void getResponse(GSpeechDuplex duplex) {
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

				// stampo in console quello che ha capito il riconoscitore
				System.out.println(output);
			}

		});

	}

	// gia' implementata
	@Override
	public void onResponse(GoogleResponse gr) {
	}
}