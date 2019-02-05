package com.mygdx.ui.graphics.screen;

import java.io.IOException;
import java.sql.SQLException;

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
import com.mygdx.application.exception_manager.ExceptionsManager;
import com.mygdx.application.screen_manager.ScreenManager;
import com.mygdx.domain.controller.Controller;
import com.mygdx.domain.models.User;
import com.mygdx.foundation.utils.Utils;
import com.mygdx.services.factory_methos_screens.HousimCreator;
import com.mygdx.services.factory_methos_screens.RegistrationCredentialsScreenCreator;
import com.mygdx.ui.graphics.game.Housim;

public class FaceDetectionScreen extends AbstractScreen {

	private TextureRegion imgRegion;
	private Texture frameTexture;
	private Image img;
	private Table imageTable;
	private Stage imgStage;

	private TextButton registrationOrAccessButton;
	private boolean registrationOrAccess;

	private TextButton backAccessButton;
	private boolean backAccess;

	private TextButton backButton;
	private boolean back;

	private TextButton redoButton;
	private boolean redo;

	private String user_telphone;

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

	@Override
	public void show() {
		super.show();
		this.mainTable.right();
		this.imageTable.center();

		Gdx.input.setInputProcessor(this.imgStage);

		String text = "FaceRegistration";

		if (!Utils.isFirstAccess) {

			text = "RegistrationAndAccess";
			if (Utils.isAccess)
				text = "Access";
		}

		if (!Utils.isAccess || Utils.isFirstAccess) {
			this.registrationOrAccessButton = new TextButton(text, this.skin);

			this.registrationOrAccessButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					registrationOrAccess = true;
				}
			});

			if (!Utils.isFirstAccess) {
				text = "Back to registrationScreen";
				this.backButton = new TextButton(text, this.skin);

				this.backButton.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						back = true;
					}
				});
			}
		} else {

			text = "Back to Main menu";
			this.backAccessButton = new TextButton(text, this.skin);

			this.backAccessButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					backAccess = true;
				}
			});
		}

		text = "Redo photo";
		this.redoButton = new TextButton(text, this.skin);

		this.redoButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				redo = true;
			}
		});

		if (!Utils.captured)
			Controller.getController().getFaceController().init();

		this.mainTable.add(this.registrationOrAccessButton);
		this.mainTable.row();
		this.mainTable.add(this.backButton);
		this.mainTable.row();
		this.mainTable.add(this.backAccessButton);
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
			if (com.mygdx.foundation.utils.Utils.capturing) {
				this.updateFrame();
				com.mygdx.foundation.utils.Utils.capturing = false;
			}

			if (Utils.isAccess && !Utils.isFirstAccess && Utils.captured && !Utils.backToRegistrationScreen
					&& !Utils.treeTimesAccessError) {

				if (Controller.getController().getFaceController().compare()) {
					Utils.captured = false;
					String pathOriginal = Utils.pathImageUser;
					Controller.getController().getFaceController().setUserAndCommandAccess(pathOriginal, false);

					ScreenManager.getInstance().showScreen(new HousimCreator());
					Utils.doorIsOpen = true;
					Housim.getHousim().inputManager.openDoorA = true;
					Housim.getHousim().entrata = true;
					Housim.getHousim().cont = 0;
					Housim.getHousim().cont1 = 0;
				} else {
					Utils.captured = false;

					// se l'utente non è stato identificato per 3 volte, gli viene mandata un'email
					// per permettergli di poter recuperare l'accesso
					if (Utils.treeTimesAccessError) {
						// mandiamo l'email all'utente per fargli recuperare l'accesso alla casa
						Utils.treeTimesAccessError = false;
						Utils.saveOnLog(Utils.ACCESS_FAILED_THREE_TIMES);
						this.showRecoveryAccessDialog(Utils.ACCESS_FAILED_THREE_TIMES, skin, imgStage,
								new TextField("", skin), true);
					} else {
						Utils.saveOnLog(Utils.ACCESS_FAILED_LOG);
						Utils.showMessageDialog(Utils.ACCESS_FAILED_POPUP, skin, imgStage);
					}
				}
			} else if (Utils.captured && Utils.backToRegistrationScreen && !Utils.treeTimesAccessError) {
				Utils.backToRegistrationScreen = false;
				Utils.showMessageDialog(Utils.ALREADY_CAPTURE_FACE_POPUP, skin, imgStage);
			}
			if (this.registrationOrAccess) {
				this.registrationOrAccess = false;
				if (Utils.captured) {
					// se l'utente deve registrarsi
					if (!Utils.isAccess || Utils.isFirstAccess) {
						if (Controller.getController().getFaceController().registerUser()) {
							// viene richiamato il 'facecaptureScreen' per far registrare l'utente
							Utils.backToRegistrationScreen = false;
							String pathImage = this.register();
							if( !Utils.isAccess )
								Controller.getController().getFaceController().setUserAndCommandRegistration(pathImage);
							else {
								Utils.isFirstAccess = false;
								Controller.getController().getFaceController().setUserAndCommandAccess(pathImage, false);
							}
							Utils.credentials.clear();
							
							ScreenManager.getInstance().showScreen(new HousimCreator());
							Utils.doorIsOpen = true;
							Housim.getHousim().inputManager.openDoorA = true;
							Housim.getHousim().entrata = true;
							Housim.getHousim().cont = 0;
							Housim.getHousim().cont1 = 0;
						} else {
							Utils.saveOnLog(Utils.FAILURE_USER_REGISTRATION_LOG);
							Utils.showMessageDialog(Utils.REGISTRATION_FAILED_POPUP, skin, imgStage);
						}
					}
				} else {
					Utils.showMessageDialog(Utils.CANT_REGISTER_WITHOUT_FACE_CAPTURE, skin, imgStage);
				}

			}

			if (this.redo) {
				this.redo = false;
				Utils.captured = false;
				Utils.backToRegistrationScreen = false;
				Controller.getController().getFaceController().setClosed();
				Controller.getController().getFaceController().init();
			}

			if (this.backAccess) {
				this.backAccess = false;
				if (Utils.captured)
					Utils.backToRegistrationScreen = true;
				Controller.getController().getFaceController().setClosed();
				Utils.showPopUp(Utils.ACCESS_BACK_TO_MAIN_SCREEN_POP, skin, imgStage, Utils.MAIN_SCREEN_POP);
			}

			if (this.back) {
				this.back = false;
				if (Utils.captured) {
					Utils.backToRegistrationScreen = true;
					ScreenManager.getInstance().showScreen(new RegistrationCredentialsScreenCreator());
				} else {
					Utils.showMessageDialog(Utils.CANT_COME_BACK_WITHOUT_FACE_CAPTURE, skin, imgStage);
				}
			}

			// this.buttons();
		} catch (Exception e) {
			ExceptionsManager.getExceptionsManager().manageException(e, skin, imgStage);
		}
	}

	private String register() throws SQLException, IOException {
		/*
		 * Decommentare le funzioni per il salvataggio. Ora se si preme il bottone +
		 * come se simulasse la registrazione. quindi la booleana la mette a false. Ma
		 * successivamente sarà messa all'interno del metodo così da settarla solo
		 * quando effettivamente il salvataggio è avvenuto con successo e questo
		 * controllo lo facciamo dentro la funzione.
		 */
		String logMessage = Utils.SUCCESS_ADMIN_REGISTRATION_LOG;
		if (Utils.isFirstAccess) {
			System.out.println("FIRST ACCESS");
		} else {
			System.out.println("NORMAL USER REGISTRATION");
			logMessage = Utils.SUCCESS_USER_REGISTRATION_LOG;
		}

		Utils.saveOnLog(logMessage);
		return this.registrationUser();
	}

	/**
	 * Save User into DB.
	 * 
	 * @param userDAO
	 * @param isAdministrator
	 * @throws SQLException
	 * @throws IOException
	 */
	@SuppressWarnings("finally")
	private String registrationUser() throws SQLException, IOException {
		User user = new User();
		/*
		 * TODO: fare distinzione di popup, quando fallisce va benisismo. Quando ha
		 * successo farlo accedere
		 */

		if (!Utils.isFirstAccess) {
			user.setEmail(Utils.credentials.get(0));
			user.setAdministrator(false);
			user.setNickName(Utils.credentials.get(2));
			user.setTelefonNumber(Utils.credentials.get(1));
			user.setIdUser(Controller.getController().getUserDAO().getIdUser());

		} else {
			user.setIdUser(Utils.ID_ADMIN_USER);
		}

		user.setPathImage("resources/images/" + user.getIdUser() + ".jpg");
		Controller.getController().getFaceController().moveImages(user.getPathImage());

		if (Controller.getController().getUserDAO().registration(user)) {
			Utils.isAccess = true;
		}
		return user.getPathImage();
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
