package com.mygdx.controller.registration;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.controller.Controller;
import com.mygdx.controller.face.FaceDetectionController;
import com.mygdx.game.ScreenManager;
import com.mygdx.interfaces.AbstractScreen;
import com.mygdx.simulator.factory_methos_screens.AdministrationScreenCreator;
import com.mygdx.simulator.factory_methos_screens.RegistrationCredentialsScreenCreator;

import utilis.Utils;

public class ChangeFaceScreen extends AbstractScreen{
	
	private TextureRegion imgRegion;
	private Texture frameTexture;
	private Image img;
	private Table imageTable;
	private Stage imgStage;
	
	private TextButton backButton;
	private boolean back;
	
	private TextButton changeCredentialsButton;
	private boolean changeCredentials;
	
	private TextButton redoButton;
	private boolean redo;
	
	
	public ChangeFaceScreen() {
		// TODO Auto-generated constructor stub
		this.imgStage = new Stage();
		// Gdx.input.setInputProcessor(imgStage);
		this.imageTable = new Table();
		this.imageTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.imageTable.center();
		// imgStage.addActor(imageTable);
		this.imageTable.debug();
		if (Utils.backToRegistrationScreen)
			this.frameTexture = new Texture(Gdx.files.internal("resources/frame.jpg"));
		else
			this.frameTexture = new Texture(Gdx.files.internal("resources/frame1.jpg"));
		this.imgRegion = new TextureRegion(frameTexture);
		this.img = new Image(imgRegion);
		this.imageTable.add(this.img).center();
		
		this.redo = this.changeCredentials = this.back = false;
	}

	@Override
	public void show()
	{
		super.show();
		this.mainTable.right();
		this.imageTable.center();

		Gdx.input.setInputProcessor(this.imgStage);
		
		String text = "Change credentials";
		this.changeCredentialsButton = new TextButton(text, this.skin);

		this.changeCredentialsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changeCredentials = true;
				System.out.println("CHANGE CREDENTIALS");
			}
		});
		
		text = "Redo";
		this.redoButton = new TextButton(text, this.skin);

		this.redoButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				redo = true;
				System.out.println("REDO");
			}
		});
		
		text = "Back";
		this.backButton = new TextButton(text, this.skin);

		this.backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				back = true;
				System.out.println("BACK");
			}
		});
		
		FaceDetectionController faceController = new FaceDetectionController();
		faceController.init();

		
		this.mainTable.add(this.changeCredentialsButton);
		this.mainTable.row();
		this.mainTable.add(this.redoButton);
		this.mainTable.row();
		this.mainTable.add(this.backButton);
		this.mainTable.row();

		this.imgStage.addActor(this.imageTable);
		// imageTable.addActor(this.mainTable);
		this.imgStage.addActor(this.mainTable);
		
	}
	
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		try {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.imgStage.act();
		this.imgStage.draw();
		if (utilis.Utils.capturing) {
			this.updateFrame(this.imageTable, this.frameTexture, this.imgRegion, this.img);
			utilis.Utils.capturing = false;
		}
	
//		if(Utils.captured && Utils.changeUserCredentials)
//		{
//		    Controller.getController().getUserDAO().updateUserCredentials(Utils.credentials.get(0), Utils.credentials.get(1), Utils.credentials.get(2), "resources/images/" + Utils.credentials.get(4) + ".jpg", Controller.getController().getUserDAO().getIdUser());
//		    Utils.changeUserCredentials = false;
//		    Utils.showMessageDialog(Utils.CHANGE_USER_CREDENTIALS_SUCCESSFULLY_MESSAGE, this.skin, this.imgStage);
//		}
//		
		 if (Utils.captured && Utils.backToRegistrationScreen) {
				Utils.backToRegistrationScreen = false;
				Utils.showMessageDialog(Utils.ALREADY_CAPTURE_FACE_POPUP, skin, imgStage);
			}
		
		if(Utils.captured && this.changeCredentials)
		{
			File file = new File("resources/images/"+Utils.credentials.get(4)+".jpg");
			Utils.removeAfileInAFolder(file);
			
			Utils.moveNewUserToImageFolder("resources/temp_image", "temp.jpg", "resources/images", Utils.credentials.get(4)+".jpg");
			System.out.println("PRIMA 0: "+Utils.credentials.get(0)+" 1: "+Utils.credentials.get(1)+" 2: "+Utils.credentials.get(2)+" 3: "+" resources/images/" + Utils.credentials.get(4) + ".jpg"+" 4: "+Utils.credentials.get(4));
			 Controller.getController().getUserDAO().updateUserCredentials(Utils.credentials.get(0), Utils.credentials.get(1), Utils.credentials.get(2), "resources/images/" + Utils.credentials.get(4) + ".jpg", Utils.credentials.get(4));
			    Utils.changeUserCredentials = false;
			    System.out.println("DOPO 0: "+Utils.credentials.get(0)+" 1: "+Utils.credentials.get(1)+" 2: "+Utils.credentials.get(2)+" 3: "+" resources/images/" + Utils.credentials.get(4) + ".jpg"+" 4: "+Utils.credentials.get(4));
			    Utils.showPopUp(Utils.CHANGE_USER_CREDENTIALS_SUCCESSFULLY_MESSAGE, skin, imgStage, Utils.ADMIN_SCREEN_POP);
	    	    ScreenManager.getInstance().showScreen(new AdministrationScreenCreator());
		}
		
		if (this.redo) {
			Utils.captured = false;
			Controller.getController().getFaceController().init();
			this.redo = false;
			Utils.backToRegistrationScreen = false;
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void dispose() {
		super.dispose();
		imgRegion.getTexture().dispose();
		System.out.println("DISPOSE CHANGE FACE SCREEN");
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

}
