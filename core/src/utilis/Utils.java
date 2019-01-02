package utilis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.gson.Gson;
import com.mygdx.game.MainMenuScreen;
import com.mygdx.game.ScreenEnum;
import com.mygdx.game.ScreenManager;
import com.mygdx.registration.AdministrationScreen;
import com.mygdx.registration.FaceCaptureScreen;
import com.mygdx.registration.LoginScreen;
import com.mygdx.registration.RegistrationCredentialsScreen;

/**
 * Classe di utilità per la lettura del file .json
 * 
 * @author Antonio
 *
 */
public class Utils {

	public static final Long ID_SUPPLY = (long) 1;
	/*
	 * PATH per i file di configurazione
	 */
	public static final String CONFIG_PATH_EMAIL = "resources/config_email.json";
	public static final String CONFIG_PATH_SMS = "resources/config_sms.json";
	public static final String CONFIG_PATH_DB = "resources/config_db.json";
	public static final String DB_PATH_QUERY = "resources/query.json";

	/*
	 * PopUp string
	 */
	public static final String MAIN_MENU_BACK_POPUP = "Are you sure do you want to exit from simulation?";
	public static final String LOGIN_SCREEN_WRONG_CREDENTIAL_POPUP = "Wrong credentials, please click on ENTER or OK to reinsert the credentials.";
	public static final String LOGIN_SCREEN_BACK_POPUP = "Are you sure you want to qui from the Login Screen?";
	public static final String ADMINISTRATION_SCREEN_BACK_POPUP = "Are you sure you want to quit from the Administration Screen?";
	public static final String FACE_CAPTURE_SCREEN_BACK_POPUP = "Are you sure you want to quit from Face Capture screen?";
	public static final String REGISTRATION_CREDENTIALS_SCREEN_BACK_POPUP = "Are you sure you want to quit from Registration screen?";
	public static final String REGISTRATION_CREDENTIALS_SCREEN_MISSING_CRED_POPUP = "Missing one or more of the three credentials.";
	public static final String REGISTRATION_CREDENTIALS_SCREEN_BAD_EMAIL_FORMAT_POPUP = "Bad format of email field.\n Example: ciao@caro.it";
	public static final String REGISTRATION_CREDENTIALS_SCREEN_BAD_NUMBER_FORMAT_POPUP = "Bad format of telphone number field. Must contain 12 numbers and the '+' character at the beginning of number and mustn't contain letter.\n Example: +390123456789";
	public static final String REGISTRATION_CREDENTIALS_SCREEN_EMAIL_EXIST_POPUP = "Email already exists, , please click on ENTER or OK to continue.";
	public static final String REGISTRATION_CREDENTIALS_SCREEN_TELEPHONE_EXIST_POPUP = "Telephone number already exists, please click on ENTER or OK to continue.";
	public static final String REGISTRATION_CREDENTIALS_SCREEN_NICKNAME_EXIST_POPUP = "Nickname already exists, please click on ENTER or OK to continue.";

	/**
	 * Metodo che prende come parametri Il Tipo di classe che ha all'interno i dati
	 * che saranno nel file .json {@link Configuration}, e il path dove si trova il file.
	 * @param type
	 * @param fileHandle
	 * @return
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("unchecked")

	public static Object getJsonFile(@SuppressWarnings("rawtypes") Class type, String fileHandle)
			throws FileNotFoundException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(fileHandle));
		Gson gson = new Gson();
		return gson.fromJson(bufferedReader, type);
	}

	public static void showPopUp(String text, Skin skin, Stage stage, final Screen screen) {
		Dialog dialog = new Dialog("", skin, "dialog") {
			public void result(Object obj) {
				System.out.println(obj);
				if (obj.equals("true")) {
					if (screen instanceof LoginScreen || screen instanceof AdministrationScreen)
						ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
					else if (screen instanceof RegistrationCredentialsScreen)
						ScreenManager.getInstance().showScreen(ScreenEnum.ADMINISTRATION_SCREEN);
					else if (screen instanceof FaceCaptureScreen)
						ScreenManager.getInstance().showScreen(ScreenEnum.REGISTRATION_CREDENTIALS_SCREEN);
					else if (screen instanceof MainMenuScreen)
						Gdx.app.exit();
				}
			}
		};
		dialog.text(text);
		dialog.button("Yes", "true"); // sends "true" as the result
		dialog.button("No", "false"); // sends "false" as the result
		dialog.key(Keys.ENTER, true); // sends "true" when the ENTER key is pressed
		dialog.show(stage);
		stage.addActor(dialog);
	}

	public static void showMessageDialog(String text, Skin skin, Stage stage) {
		Dialog dialog = new Dialog("", skin);
		dialog.text(text);
		dialog.button("Ok", "true");
		dialog.key(Keys.ENTER, true);
		dialog.show(stage);
		stage.addActor(dialog);
	}

}
