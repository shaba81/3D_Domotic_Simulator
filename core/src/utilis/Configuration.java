package utilis;

/**
 * Classe delegata ad avere Stringhe utilizzate dalle classi del codice
 * per avere i valori del file .json
 * @author Antonio
 *
 */
public interface Configuration {

//	/*
//	 * email
//	 */
//	public static final String userEmail;
//	public String userPassword;
//
//	/*
//	 * sms
//	 */
//	public String baseUrlSms;
//	public String messageQuality;
//	public String userNameSms;
//	public String userPasswordSms;
//	public String messageBody;
//	public String recipient;

	/*
	 * DB set
	 */
	public String jdbc = "postgresql";
	public String host = "ingsw.ddns.net";
	public String port = "5432";
	public String database = "ingsw";
	public String username = "ingswuser";
	public String password = "ingswuser112358";
	public String driver = "org.postgresql.Driver";

	/*
	 * DB query
	 */
	public String nextId =  "select nextval('ingswschema.sequence_id') AS id;";
	public String insertSupply = "insert into ingswschema.supply values(nextval('ingswschema.sequence_id'));";
	public String updateUserAdminPass =  "update ingswschema.users set password_registration=crypt(?, gen_salt('bf')) where id_user=?;";
	public String validateUserAdminPass = "select ingswschema.users.id_user, (password_registration= crypt(?, password_registration)) AS pswmatch from ingswschema.users where id_user=?;";
	public String updateUserAdmin = "update ingswschema.users set email=?, nick_name=?, telephone_number=?, path_image=?, is_administrator=? where id_user=?;";
	public String insertUser = "insert into ingswschema.users(id_user,email,nick_name,telephone_number,path_image,is_administrator,id_supply) values (nextval('ingswschema.sequence_id'),?,?,?,?,?,1);";
	public String findByEmail = "select * from ingswschema.users where email=?;";
	public String deleteUserById = "delete from ingswschema.users where id_user=?;";
}