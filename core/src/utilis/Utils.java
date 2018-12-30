package utilis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;

/**
 * Classe di utilità per la lettura del file .json
 * @author Antonio
 *
 */
public class Utils {

	/*
	 * PATH per i file di configurazione
	 */
	public static final String CONFIG_PATH_EMAIL = "resources/config_email.json";
	public static final String CONFIG_PATH_SMS = "resources/config_sms.json";
	public static final String CONFIG_PATH_DB = "resources/config_db.json";
	public static final String DB_PATH_QUERY = "resources/query.json";
	

	/**
	 * Metodo che prende come parametri Il Tipo di classe che ha all'interno i dati
	 * che saranno nel file .json {@link Configuration}, e il path dove si trova il file.
	 * @param type
	 * @param fileHandle
	 * @return
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static Object getJsonFile(@SuppressWarnings("rawtypes") Class type, String fileHandle) throws FileNotFoundException
	{
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileHandle));
        Gson gson = new Gson();
        return gson.fromJson(bufferedReader, type);
	}

}
