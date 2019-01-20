package com.mygdx.controller.registration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.controller.database.model.User;
import com.mygdx.controller.database.persistence.PostgreDAOFactory;
import com.mygdx.controller.database.persistence.dao.UserDAO;
import com.mygdx.controller.face.FaceDetectionController;
import com.mygdx.game.ScreenManager;
import com.mygdx.interfaces.AbstractScreen;
import com.mygdx.simulator.factory_methos_screens.GameScreenCreator;
import com.mygdx.simulator.factory_methos_screens.RegistrationCredentialsScreenCreator;

import utilis.Utils;

public class FaceDetectionScreen extends AbstractScreen {

	private TextureRegion imgRegion;
	private Texture frameTexture;
	private Image img;
	private Table imageTable;
	private Stage imgStage;

	private TextButton registrationOrAccessButton;
	private boolean registrationOrAccess;

	private TextButton backButton;
	private boolean back;

	private TextButton redoButton;
	private boolean redo;

	private String user_telphone;

	private static FaceDetectionScreen faceDetectionScreen;
	// se l'utente vorrà accedere, sarà true; se l'amministratore dovrà registrarlo,
	// sarà false
	private FaceDetectionController faceController;

	public FaceDetectionScreen() {

		imgStage = new Stage();
		// Gdx.input.setInputProcessor(imgStage);
		imageTable = new Table();
		imageTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		imageTable.center();
		// imgStage.addActor(imageTable);
		imageTable.debug();

		frameTexture = new Texture(Gdx.files.internal("resources/frame1.jpg"));
		imgRegion = new TextureRegion(frameTexture);
		img = new Image(imgRegion);
		imageTable.add(img).center();

		this.back = false;
		this.registrationOrAccess = false;
		this.redo = false;
	}

	public static void setFaceDetectionScreen(FaceDetectionScreen faceDetectionScreen) {
		FaceDetectionScreen.faceDetectionScreen = faceDetectionScreen;
	}

	@Override
	public void show() {
		super.show();
		this.mainTable.right();
		this.imageTable.center();

		this.faceController = new FaceDetectionController();
		Gdx.input.setInputProcessor(this.imgStage);

		String text = "FaceRegistration";

		if (!Utils.isFirstAccess) {

			text = "RegistrationAndAccess";
			if (Utils.isAccess)
				text = "Access";
		}

		if (!Utils.isAccess) {
			this.registrationOrAccessButton = new TextButton(text, this.skin);

			this.registrationOrAccessButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					registrationOrAccess = true;
					System.out.println("OK");
				}
			});

			text = "Back to registrationScreen";
			this.backButton = new TextButton(text, this.skin);

			this.backButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					back = true;
					System.out.println("back");
				}
			});
		}

		text = "Redo photo";
		this.redoButton = new TextButton(text, this.skin);

		this.redoButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				redo = true;
				System.out.println("redo photo");
			}
		});

		FaceDetectionController faceController = new FaceDetectionController();
		faceController.init();

		this.mainTable.add(this.registrationOrAccessButton);
		this.mainTable.row();
		this.mainTable.add(this.backButton);
		this.mainTable.row();
		this.mainTable.add(this.redoButton);
		this.mainTable.row();

		imgStage.addActor(imageTable);
		// imageTable.addActor(this.mainTable);
		imgStage.addActor(mainTable);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.imgStage.act();
		this.imgStage.draw();
		if (utilis.Utils.capturing) {
			this.updateFrame();
			utilis.Utils.capturing = false;
		}

		if (Utils.isAccess && !Utils.isFirstAccess && Utils.captured && !Utils.backToRegistrationScreen
				&& !Utils.treeTimesAccessError) {

			if (faceController.compare()) {
				// viene richiamato il 'gameScreen'
				System.out.println("puoi accedere");
				Utils.captured = false;
				ScreenManager.getInstance().showScreen(new GameScreenCreator());
			} else {
				System.out.println("non puoi accedere. Riprova");
				Utils.captured = false;

				// se l'utente non è stato identificato per 3 volte, gli viene mandata un'email
				// per permettergli di poter recuperare l'accesso
				if (Utils.treeTimesAccessError) {
					// mandiamo l'email all'utente per fargli recuperare l'accesso alla casa
					Utils.treeTimesAccessError = false;
					System.out.println("TRE VOLTE");
					this.showRecoveryAccessDialog(Utils.ACCESS_FAILED_THREE_TIMES, skin, imgStage,
							new TextField("", skin), true);
					System.out.println("TRE VOLTE");
				} else {
					Utils.showMessageDialog(Utils.ACCESS_FAILED_POPUP, skin, imgStage);
				}
			}
		}
		// si vedrà solo quando gli screen saranno singleton
		else if (Utils.captured && !Utils.backToRegistrationScreen && !Utils.treeTimesAccessError)
			Utils.showMessageDialog(Utils.ALREADY_CAPTURE_FACE_POPUP, skin, imgStage);

		if (this.registrationOrAccess) {
			System.out.println("captured: " + Utils.captured);
			if (Utils.captured) {
				// se l'utente deve registrarsi
				if (!Utils.isAccess) {
					if (faceController.registerUser()) {
						// viene richiamato il 'facecaptureScreen' per far registrare l'utente
						System.out.println("puoi registrarti");
						Utils.backToRegistrationScreen = false;
						this.register();
						ScreenManager.getInstance().showScreen(new GameScreenCreator());
					} else {
						System.out.println("non puoi registrarti");
						Utils.showMessageDialog(Utils.REGISTRATION_FAILED_POPUP, skin, imgStage);
						// uscirà un popup e poi verrà richiamata la 'init' di faceDetection MASSIMO
						// ALTRE 2 VOLTE(mi pare)
					}
				}
			} else
				System.out.println("miao");

			this.registrationOrAccess = false;
		}

		if (this.redo) {
			Utils.captured = false;
			this.faceController.init();
			this.redo = false;
			Utils.backToRegistrationScreen = false;
		}

		if (this.back) {
			this.back = false;
			if (Utils.captured)
				Utils.backToRegistrationScreen = true;
			ScreenManager.getInstance().showScreen(new RegistrationCredentialsScreenCreator());
		}

		// this.buttons();
	}

	private void register() {
		PostgreDAOFactory postgreDAOFactory = new PostgreDAOFactory();
		UserDAO userDAO = postgreDAOFactory.getUtenteDAO();

		/*
		 * Decommentare le funzioni per il salvataggio. Ora se si preme il bottone +
		 * come se simulasse la registrazione. quindi la booleana la mette a false. Ma
		 * successivamente sarà messa all'interno del metodo così da settarla solo
		 * quando effettivamente il salvataggio è avvenuto con successo e questo
		 * controllo lo facciamo dentro la funzione.
		 */
		if (Utils.isFirstAccess) {
			System.out.println("FIRST ACCESS");
			this.registrationUser(userDAO);
		} else {
			System.out.println("NORMAL USER REGISTRATION");
			this.registrationUser(userDAO);
		}
	}

	/**
	 * Save User into DB.
	 * 
	 * @param userDAO
	 * @param isAdministrator
	 */
	private void registrationUser(UserDAO userDAO) {
		try {
			/*
			 * TODO: fare distinzione di popup, quando fallisce va benisismo. Quando ha
			 * successo farlo accedere
			 */
			User user = new User();
			System.out.println("1");
			if (!Utils.isFirstAccess) {
				System.out.println("2");
				user.setEmail(Utils.credentials.get(0));
				user.setAdministrator(false);
				user.setNickName(Utils.credentials.get(2));
				user.setTelefonNumber(Utils.credentials.get(1));
				user.setIdUser(userDAO.getIdUser());

			} else {
				System.out.println("3");
				user.setIdUser(Utils.ID_ADMIN_USER);
			}

			System.out.println("4");
			user.setPathImage("resources/images/" + user.getIdUser() + ".jpg");
			this.faceController.moveImages(user.getPathImage());

			if (userDAO.registration(user)) {
				System.out.println("5");
				Utils.isAccess = true;
			}
			System.out.println("7");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateFrame() {
		imageTable.clear();
		this.frameTexture = new Texture(Gdx.files.internal("resources/frame.jpg"));
		imgRegion.setRegion(frameTexture);
		img = new Image(imgRegion);
		imageTable.add(img).center();
	}

	public void dispose() {
		super.dispose();
		imgRegion.getTexture().dispose();

	}

	private void sendEmailToUser() {
//		EmailSender.sendMessage(emailTelephoneNumeberAdmin[0], Utils.OBJ_EMAIL_RECOVERY_PASS_ADMIN, bodyMessage);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	public String getUser_telphone() {
		return user_telphone;
	}

	public void setUser_telphone(String user_telphone) {
		this.user_telphone = user_telphone;
	}

}
