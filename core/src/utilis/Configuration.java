package utilis;

/**
 * Classe delegata ad avere Stringhe utilizzate dalle classi del codice
 * per avere i valori del file .json
 * @author Antonio
 *
 */
public interface Configuration {

	/*
	 * email
	 */
	public static final String userEmail = "fantaleague.siw18@gmail.com";
	public String userPassword = "112358.siw18";

	/*
	 * sms
	 */
	public String baseUrlSms = "https://api.skebby.it/API/v1.0/REST/";
	public String messageQuality = "SI";
	public String userNameSms = "tagete";
	public String userPasswordSms = "pr08qwe03";

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
	public String selectUserByPathImage = "select u.email, u.telephone_number, u.nick_name, u.path_image, u.is_administrator from ingswschema.users u where u.path_image=?;";

	public String selectFunction = "select * from ingswschema.expire_value();";
	public String validateUserOneTimePAss = "select ingswschema.users.id_user, (one_time_pass= crypt(?, one_time_pass)) AS pswmatch from ingswschema.users where email=?;";
	public String deleteOneTimaPAss = "UPDATE ingswschema.users SET timestamp_one_time_pass=NULL, one_time_pass='' WHERE email=?;";
	public String updateOneTimePass = "UPDATE ingswschema.users SET timestamp_one_time_pass=NOW(), one_time_pass=crypt(?, gen_salt('bf')) WHERE email=?;";

	public String nextId =  "select nextval('ingswschema.sequence_id') AS id;";
	public String insertSupply = "insert into ingswschema.supply values(nextval('ingswschema.sequence_id'));";
	public String updateUserAdminPass =  "update ingswschema.users set password_registration=crypt(?, gen_salt('bf')) where id_user=? and id_supply=?;";
	public String validateUserAdminPass = "select ingswschema.users.id_user, (password_registration= crypt(?, password_registration)) AS pswmatch from ingswschema.users where id_user=?;";
	public String insertUserNormal = "insert into ingswschema.users(id_user,email,nick_name,telephone_number,path_image,is_administrator,id_supply) values (?,?,?,?,?,?,1);";
	public String updateUserAdmin = "update ingswschema.users set path_image=? where id_user=?;";
	public String findById = "select email, telephone_number from ingswschema.users where id_user=?;";
	public String findByEmail = "select email from ingswschema.users where email=?;";

	public String checkIfEmailExist = "select u.id_user from ingswschema.users u where u.email=?;";
	public String checkIfTelephoneNumberExist = "select u.id_user from ingswschema.users u where u.telephone_number=?;";
	public String checkIfNickNameExist = "select u.id_user from ingswschema.users u where u.nick_name=?;";

	public String isFirstRegistrationForThisForniture = "select u.email from ingswschema.users u where u.path_image!='' and u.id_supply=? and u.id_user=?;";

	public String deleteUserById = "delete from ingswschema.users where id_user=?;";

	public String insertCommandLog = "insert into ingswschema.interaction_user_home values(nextval('ingswschema.sequence_id'),?,18,NOW(),?);";
	public String selectCommandLog = "select u.nick_name, iuh.time_request, iuh.command from ingswschema.interaction_user_home iuh, ingswschema.users u where iuh.id_user=u.id_user;";
	public String currentlyUserIsAdministrator = "select * from ingswschema.users where id_user=?;";

}