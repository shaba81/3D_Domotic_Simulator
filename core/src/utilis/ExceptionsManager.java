package utilis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ExceptionsManager {

	private static ExceptionsManager exceptionsManager;

	private ExceptionsManager() {
	}

	public static ExceptionsManager getExceptionsManager() {
		if( exceptionsManager == null )
			exceptionsManager = new ExceptionsManager();

		return exceptionsManager;
	}

	public void manageException(Exception exception, Skin skin, Stage stage) {
		String message = "";
		exception.printStackTrace();
		if( exception instanceof SQLException ) {
			message = "SQL EXCEPTION-(MESSAGGIO MOMENTANEO)";
		} else if( exception instanceof FileNotFoundException ) {
			message = "FILE NOT FOUND-(MESSAGGIO MOMENTANEO)";
		} else if( exception instanceof IOException) {
			message = "IOEXCEPTION-(MESSAGGIO MOMENTANEO)";
		} else if( exception instanceof MessagingException ) {
			message = Utils.MESSAGINGE_MESSAGE;
		} else if( exception instanceof AddressException ) {
			message = Utils.ADDRESSE_INVALID_EMAIL_MESSAGE;
		} else {
			message = "ALTRA ECCEZIONE";
		}

		this.showMessage(message, skin, stage);
	}

	private void showMessage(String message, Skin skin, Stage stage) {
		//Utils.showMessageDialog(message, skin, stage);
		System.err.println("1: " + message);
	}
}
