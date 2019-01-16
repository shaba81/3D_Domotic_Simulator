package utilis;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;

import org.opencv.core.Mat;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.gson.Gson;
import com.mygdx.game.ScreenEnum;
import com.mygdx.game.ScreenManager;

/**
 * Classe di utilità per la lettura del file .json
 * 
 * @author Antonio
 *
 */
public class Utils {

	public static boolean isFirstAccess = false;
	public static boolean capturing = false;

	public static final Long ID_SUPPLY = (long) 1;
	public static final String ID_USER = "5";

	public static final String OBJ_EMAIL_RECOVERY_PASS_ADMIN = "Recovery administrator credentials.";
	public static final String MESSAGE_RECOVERY_PASS_ADMIN_EMAIL = "Dear customer, \nhere are the new login credentials. The mode is always the same as the ID can enter the same assigned to it during purchase and as a password the new password mentioned at the bottom of the message.";
	public static final String MESSAGE_RECOVERY_PASS_ADMIN_SMS = "Dear customer, \nhere are the new login credentials.";
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
	public static final String SCREEN_BACK_GAME_POPUP = "Are you sure you want to return simulation without registration?";
	public static final String MAIN_MENU_BACK_POPUP = "Are you sure do you want to exit from simulation?";
	public static final String LOGIN_SCREEN_WRONG_CREDENTIAL_POPUP = "Wrong credentials, please click on ENTER or OK to reinsert the credentials.";
	public static final String LOGIN_SCREEN_BACK_POPUP = "Are you sure you want to quit from the Login Screen?";
	public static final String ADMINISTRATION_SCREEN_BACK_POPUP = "Are you sure you want to quit from the Administration Screen?";
	public static final String FACE_CAPTURE_SCREEN_BACK_POPUP = "Are you sure you want to quit from Face Capture screen?";
	public static final String REGISTRATION_CREDENTIALS_SCREEN_BACK_POPUP = "Are you sure you want to quit from Registration screen?";
	public static final String REGISTRATION_CREDENTIALS_SCREEN_MISSING_CRED_POPUP = "Missing one or more of the three credentials.";
	public static final String REGISTRATION_CREDENTIALS_SCREEN_EMAIL_EXIST_POPUP = "Email already exists, , please click on ENTER or OK to continue.";
	public static final String REGISTRATION_CREDENTIALS_SCREEN_TELEPHONE_EXIST_POPUP = "Telephone number already exists, please click on ENTER or OK to continue.";
	public static final String REGISTRATION_CREDENTIALS_SCREEN_NICKNAME_EXIST_POPUP = "Nickname already exists, please click on ENTER or OK to continue.";
	public static final String REGISTRATION_CREDENTIALS_SCREEN_MISSING_AT_EMAIL_POPUP = "Wrong email format. This field must contain's '@' caharacter.";
	public static final String REGISTRATION_CREDENTIALS_SCREEN_MISSING_POINT_EMAIL_POPUP = "Wrong email format. This field must contain's '.' character.";
	public static final String REGISTRATION_CREDENTIALS_SCREEN_NUMBER_CONTAINS_LETTER_POPUP = "Wrong telephone numebr format. This field mustn't contain's any letter or other character.";
	public static final String REGISTRATION_CREDENTIALS_SCREEN_NUMBER_TOO_SHORT_POPUP = "Wrong telephone numebr format. The field must be composed of the '+' character at the beginning, \nthe next two numbers represent the area code and the next ten numbers the telephone number.";
	public static final String REGISTRATION_CREDENTIALS_SCREEN_MISSIN_PLUS_NUMBER_POPUP = "Wrong telephone numebr format. Missing the '+' character at the beginning of the number.";
	public static final String LOGIN_SCREEN_FIRST_ACCESS_POPUP = "Hi, this is the first request for access to the house. \nYou will be registered as an administrator of this simulation. Please insert your administration credentials to login.\nClick OK or press ENTER to continue recording.";
	public static final String REGISTRATION_FAILED_POPUP = "Sorry registration was not successful, please check the data entered.";
	public static final String REGISTRATION_SUCCESS_POPUP = "The registration of the user has happened successfully.";
	public static final String LOGIN_SCREEN_TOO_MANY_FAILED_ATTEMPTS_POPUP = "Sorry, you've reached the maximum attempt limit for entering your credentials. \nYou will receive an email and a text message with the new credentials to log in. The old ones will no longer be valid. \nPlease check your email and mobile phone in order to enter your new credentials.";
	public static final String LOGIN_SCREEN_NO_ID_INSERT_POPUP = "The ID's field is empty. Please enter your ID for log in.";
	public static final String LOGIN_SCREEN_NO_PASSWORD_INSERT_POPUP = "The Password's field is empty. Please enter your Password for log in.";
	
	/**
	 * Vocal recognition variables
	 */
	public static String resp = "";
	
	/**
	 * 
	 * @param original
	 * @return
	 */
	public static BufferedImage matToBufferedImage(Mat original)
	{
		// init
		BufferedImage image = null;
		int width = original.width(), height = original.height(), channels = original.channels();
		byte[] sourcePixels = new byte[width * height * channels];
		original.get(0, 0, sourcePixels);

		if (original.channels() > 1)
		{
			image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		}
		else
		{
			image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		}
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

		return image;
	}
	
	/**
	 * Metodo che prende come parametri Il Tipo di classe che ha all'interno i dati
	 * che saranno nel file .json {@link Configuration}, e il path dove si trova il
	 * file
	 * 
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

	public static void showPopUp(String text, Skin skin, Stage stage, final String screenCall) {
		Dialog dialog = new Dialog("", skin, "dialog") {
			public void result(Object obj) {
				if (obj.equals("true")) {
					if (screenCall.equals("login_back_adm") || screenCall.equals("admin_screen"))
						ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
					else if (screenCall.equals("login_back_game")
							|| screenCall.equals("registration_credentials_screen_first_acc"))
						ScreenManager.getInstance().showScreen(ScreenEnum.GAME_SCREEN);
					else if (screenCall.equals("game_screen"))
						ScreenManager.getInstance().showScreen(ScreenEnum.LOGIN_SCREEN);
					else if (screenCall.equals("registration_credentials_screen"))
						ScreenManager.getInstance().showScreen(ScreenEnum.ADMINISTRATION_SCREEN);
					else if (screenCall.equals("face_capture_screen"))
						ScreenManager.getInstance().showScreen(ScreenEnum.REGISTRATION_CREDENTIALS_SCREEN);
					else if (screenCall.equals("main_menu_screen"))
						Gdx.app.exit();
				}
			}
		};
		dialog.text(text);
		dialog.button("Yes", "true"); // sends "true" as the result
		dialog.button("No", "false"); // sends "false" as the result
		dialog.key(Keys.ENTER, "true"); // sends "true" when the ENTER key is pressed
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

	public static String generatePasswordAdministrator() {
		String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		String chars = CHARS.toLowerCase();
		String string;
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();

		while (salt.length() < 8) {

			Random r = new Random();
			boolean choice = r.nextBoolean();
			string = chars;
			if (choice)
				string = CHARS;

			int index = (int) (rnd.nextFloat() * string.length());
			salt.append(string.charAt(index));
		}
		return salt.toString();
	}

}
