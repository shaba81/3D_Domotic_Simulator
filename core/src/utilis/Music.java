package utilis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.sound.sampled.FloatControl;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Music {
//	public static void main(String[] args) {
//		new Music();
//		
//	}
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

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JavaLayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		// non facciamo terminare l'app prima del thread
		thread.setDaemon(false);

		// avviamo thread
		thread.start();
	}

//	public void start() throws JavaLayerException {
//
//		player.play();
//
//		System.out.println("Song is playing...");
//
//	}

	public void close() {
		System.err.println("MA");
		player.close();
		Utils.songPlay = false;
	}
}
