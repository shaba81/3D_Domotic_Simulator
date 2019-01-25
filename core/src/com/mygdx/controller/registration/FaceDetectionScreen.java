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
import com.mygdx.controller.Controller;
import com.mygdx.controller.database.model.User;
import com.mygdx.controller.face.FaceDetectionController;
import com.mygdx.controller.proxy.UserAdministratorCommand;
import com.mygdx.controller.proxy.UserCommand;
import com.mygdx.game.GameScreen;
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
	// se l'utente vorr� accedere, sar� true; se l'amministratore dovr� registrarlo,
	// sar� false
	private FaceDetectionController faceController;

	public FaceDetectionScreen() {

		imgStage = new Stage();
		// Gdx.input.setInputProcessor(imgStage);
		imageTable = new Table();
		imageTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		imageTable.center();
		// imgStage.addActor(imageTable);
		imageTable.debug();
		if (Utils.backToRegistrationScreen)
			this.frameTexture = new Texture(Gdx.files.internal("resources/frame.jpg"));
		else
			this.frameTexture = new Texture(Gdx.files.internal("resources/frame1.jpg"));
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
		try {
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
//				Utils.resp = Utils.ACCESS_SUCCESS_LOG;

					String pathOriginal = Utils.pathImageUser;
					String idUser = Utils.getIdUserFromImage(pathOriginal);
					User user = Controller.getController().getUserDAO().getUserByPathImage("resources/images/" +idUser +".jpg");
					user.setIdUser(idUser);
					Utils.userLogged = idUser;
					Utils.saveOnLog(Utils.ACCESS_SUCCESS_LOG);
					/*
					 * Proxy o non proxy decision
					 */
					GameScreen.getGameScreen().setUser(user);
					if( user.isAdministrator() )
						GameScreen.getGameScreen().setCommand(new UserAdministratorCommand());
					else
						GameScreen.getGameScreen().setCommand(new UserCommand());

					ScreenManager.getInstance().showScreen(new GameScreenCreator());
				} else {
					System.out.println("non puoi accedere. Riprova");
					Utils.captured = false;

					// se l'utente non � stato identificato per 3 volte, gli viene mandata un'email
					// per permettergli di poter recuperare l'accesso
					if (Utils.treeTimesAccessError) {
						// mandiamo l'email all'utente per fargli recuperare l'accesso alla casa
						Utils.treeTimesAccessError = false;
						System.out.println("TRE VOLTE");
//					Utils.resp = Utils.ACCESS_FAILED_THREE_TIMES;
						Utils.saveOnLog(Utils.ACCESS_FAILED_THREE_TIMES_LOG);
						this.showRecoveryAccessDialog(Utils.ACCESS_FAILED_THREE_TIMES, skin, imgStage,
								new TextField("", skin), true);
						System.out.println("TRE VOLTE");
					} else {
//					Utils.resp = Utils.ACCESS_FAILED_LOG;
						Utils.saveOnLog(Utils.ACCESS_FAILED_LOG);
						Utils.showMessageDialog(Utils.ACCESS_FAILED_POPUP, skin, imgStage);
					}
				}
			} else if (Utils.captured && Utils.backToRegistrationScreen && !Utils.treeTimesAccessError) {
				Utils.backToRegistrationScreen = false;
				Utils.showMessageDialog(Utils.ALREADY_CAPTURE_FACE_POPUP, skin, imgStage);
			}
			if (this.registrationOrAccess) {
				System.out.println("captured: " + Utils.captured);
				if (Utils.captured) {
					// se l'utente deve registrarsi
					if (!Utils.isAccess) {
						if (faceController.registerUser()) {
							// viene richiamato il 'facecaptureScreen' per far registrare l'utente
							System.out.println("puoi registrarti");
							Utils.backToRegistrationScreen = false;
							String pathImage = this.register();
							String idUser = Utils.getIdUserFromImage(pathImage);
							User user = new User(Utils.credentials.get(0), Utils.credentials.get(2), Utils.credentials.get(1), pathImage, false);
							user.setIdUser(idUser);
							Utils.credentials.clear();
							GameScreen.getGameScreen().setUser(user);
							GameScreen.getGameScreen().setCommand(new UserCommand());
							ScreenManager.getInstance().showScreen(new GameScreenCreator());
						} else {
							System.out.println("non puoi registrarti");
//						Utils.resp = Utils.FAILURE_USER_REGISTRATION_LOG;
							Utils.saveOnLog(Utils.FAILURE_USER_REGISTRATION_LOG);
							Utils.showMessageDialog(Utils.REGISTRATION_FAILED_POPUP, skin, imgStage);
							// uscir� un popup e poi verr� richiamata la 'init' di faceDetection MASSIMO
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String register() {

		String logMessage = Utils.SUCCESS_ADMIN_REGISTRATION_LOG;
		if (Utils.isFirstAccess) {
			System.out.println("FIRST ACCESS");
		} else {
			System.out.println("NORMAL USER REGISTRATION");
			logMessage = Utils.SUCCESS_USER_REGISTRATION_LOG;
		}

		String path = this.registrationUser();
		Utils.saveOnLog(logMessage);
		return path;
	}

	/**
	 * Save User into DB.
	 * 
	 * @param userDAO
	 * @param isAdministrator
	 */
	@SuppressWarnings("finally")
	private String registrationUser() {
		String path = "";
		try {
			/*
			 * TODO: fare distinzione di popup, quando fallisce va benisismo. Quando ha
			 * successo farlo accedere
			 */
			User user = new User();
			if (!Utils.isFirstAccess) {
				System.out.println("2");
				user.setEmail(Utils.credentials.get(0));
				user.setAdministrator(false);
				user.setNickName(Utils.credentials.get(2));
				user.setTelefonNumber(Utils.credentials.get(1));
				user.setIdUser(Controller.getController().getUserDAO().getIdUser());

			} else {
				user.setIdUser(Utils.ID_ADMIN_USER);
			}

			user.setPathImage("resources/images/" + user.getIdUser() + ".jpg");
			this.faceController.moveImages(user.getPathImage());

			if (Controller.getController().getUserDAO().registration(user)) {
				System.out.println("5");
				Utils.isAccess = true;
			}
			path = user.getPathImage();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally {
			return path;
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
		System.out.println("FACE");
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
