package com.mygdx.controller.registration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.controller.face.FaceDetectionController;
import com.mygdx.game.ScreenEnum;
import com.mygdx.game.ScreenManager;
import com.mygdx.interfaces.AbstractScreen;

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
	
	private boolean click;
	
	private String user_telphone;
	
	//se l'utente vorrà accedere, sarà true; se l'amministratore dovrà registrarlo, sarà false
	private boolean accesso;
	
	private FaceDetectionController faceController;

	public FaceDetectionScreen() {

		imgStage = new Stage();
		// Gdx.input.setInputProcessor(imgStage);
		imageTable = new Table();
		imageTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		imageTable.center();
//		imgStage.addActor(imageTable);
		imageTable.debug();

		frameTexture = new Texture(Gdx.files.internal("resources/frame1.jpg"));
		imgRegion = new TextureRegion(frameTexture);
		img = new Image(imgRegion);
		imageTable.add(img).center();
		
		this.back = false;
		this.registrationOrAccess = false;
		this.click = false;
		this.redo = false;
		
		//di default sarà un utente che vorrà accedere
		this.accesso = false;
		this.user_telphone = "befana";

	}

	@Override
	public void show() {
		super.show();
		this.mainTable.center();
		this.imageTable.center();
		
		this.faceController = new FaceDetectionController();
		faceController.init();
		Gdx.input.setInputProcessor(this.imgStage);
		
		String text = "RegistrationOrAccess";
		this.registrationOrAccessButton = new TextButton(text, this.skin);
		
		this.registrationOrAccessButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				registrationOrAccess = true;
//				if(((FaceDetectionController)faceController).isCaptured())
//				{
//					//se l'utente deve accedere
//					if(accesso)
//					{
//						if(((FaceDetectionController)faceController).compare())
//						{
//							//viene richiamato il 'gameScreen'
//							System.out.println("puoi accedere");
//							ScreenManager.getInstance().showScreen(ScreenEnum.GAME_SCREEN);
//						}
//						else
//						{
//							System.out.println("non puoi accedere. Riprova");
//							//uscirà un popup e poi verrà richiamata la 'init' di faceDetection MASSIMO ALTRE 2 VOLTE(mi pare)
//						}
//					}
//					//se l'utente deve registrarsi
//					else if(!accesso){
//						if(((FaceDetectionController)faceController).registerUser(user_telphone))
//						{
//							//viene richiamato il 'facecaptureScreen' per far registrare l'utente 
//							System.out.println("puoi registrarti");
//							ScreenManager.getInstance().showScreen(ScreenEnum.FACE_CAPTURE_SCREEN);
//						}
//						else
//						{
//							System.out.println("non puoi registrarti");
//							//uscirà un popup e poi verrà richiamata la 'init' di faceDetection MASSIMO ALTRE 2 VOLTE(mi pare)
//						}
//					}
//				}
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
		
		text = "Redo photo";
		this.redoButton = new TextButton(text, this.skin);
		
		this.redoButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				redo = true;
				System.out.println("redo photo");
			}
		});
		
//		FaceDetectionController faceController = new FaceDetectionController();
//		faceController.init();
		
		
		this.imageTable.add(this.registrationOrAccessButton);
		this.imageTable.row();
		this.imageTable.add(this.backButton);
		this.imageTable.row();
		this.imageTable.add(this.redoButton);
		this.imageTable.row();
		
		imgStage.addActor(imageTable);
		imageTable.addActor(this.mainTable);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		

		this.imgStage.act();
		this.imgStage.draw();
//		if (utilis.Utils.capturing) {
//			this.updateFrame();
//			utilis.Utils.capturing = false;
//		}
		
		if(this.registrationOrAccess)
		{
			if(((FaceDetectionController)faceController).isCaptured())
			{
				//se l'utente deve accedere
				if(this.accesso)
				{
					if(((FaceDetectionController)faceController).compare())
					{
						//viene richiamato il 'gameScreen'
						System.out.println("puoi accedere");
						ScreenManager.getInstance().showScreen(ScreenEnum.GAME_SCREEN);
					}
					else
					{
						System.out.println("non puoi accedere. Riprova");
						//uscirà un popup e poi verrà richiamata la 'init' di faceDetection MASSIMO ALTRE 2 VOLTE(mi pare)
					}
				}
				//se l'utente deve registrarsi
				else if(!this.accesso){
					if(((FaceDetectionController)faceController).registerUser(this.user_telphone))
					{
						//viene richiamato il 'facecaptureScreen' per far registrare l'utente 
						System.out.println("puoi registrarti");
						ScreenManager.getInstance().showScreen(ScreenEnum.GAME_SCREEN);
					}
					else
					{
						System.out.println("non puoi registrarti");
						//uscirà un popup e poi verrà richiamata la 'init' di faceDetection MASSIMO ALTRE 2 VOLTE(mi pare)
					}
				}
			}
			
			this.registrationOrAccess = false;
		}
		
		if(this.redo)
		{
			this.faceController.setCaptured(false);
			this.faceController.init();
			this.redo = false;
		}
		
		if(this.back)
		{
			this.back = false;
			ScreenManager.getInstance().showScreen(ScreenEnum.REGISTRATION_CREDENTIALS_SCREEN);
		}
		  
//		this.buttons();
	}

	public void updateFrame() {
		imageTable.clear();
		this.frameTexture = new Texture(Gdx.files.internal("resources/temp_image/temp.jpg"));
		this.imgRegion = new TextureRegion(frameTexture);
		img = new Image(imgRegion);
		imageTable.add(img).center();
	}
	
	public void buttons()
	{
		
      this.registrationOrAccessButton.setVisible(true);
      
//		this.okButton.addListener(new ClickListener() {
//			@Override
//			public void clicked(InputEvent event, float x, float y) {
//				ok = true;
//				System.out.println("OK");
//			}
//		});
//		this.backButton.addListener(new ClickListener() {
//			@Override
//			public void clicked(InputEvent event, float x, float y) {
//				back = true;
//				System.out.println("back");
//			}
//		});
//		
//		this.registerButton.addListener(new ClickListener() {
//			@Override
//			public void clicked(InputEvent event, float x, float y) {
//				register = true;
//				System.out.println("registration");
//			}
//		});
//		
		this.imageTable.add(this.registrationOrAccessButton);
//		this.imageTable.row();
		this.imageTable.add(this.backButton);
//		this.imageTable.row();
//		
		imgStage.addActor(imageTable);
		imageTable.addActor(this.mainTable);
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
