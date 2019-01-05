package com.mygdx.controller.registration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.mygdx.interfaces.AbstractScreen;

import controller.face.FaceDetectionController;

public class FaceDetectionScreen extends AbstractScreen {

	private TextureRegion imgRegion;
	private Texture frameTexture;
	private Image img;
	private Table imageTable;
	private Stage imgStage;

	public FaceDetectionScreen() {

		imgStage = new Stage();
		// Gdx.input.setInputProcessor(imgStage);
		imageTable = new Table();
		imageTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		imageTable.center();
		imgStage.addActor(imageTable);
		imageTable.debug();

		frameTexture = new Texture(Gdx.files.internal("frame.jpg"));
		imgRegion = new TextureRegion(frameTexture);
		img = new Image(imgRegion);
		imageTable.add(img).center();

	}

	@Override
	public void show() {
		super.show();

		FaceDetectionController faceController = new FaceDetectionController();
		faceController.init();

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.imgStage.act();
		this.imgStage.draw();
	}

	public void updateFrame() {

		frameTexture = new Texture(Gdx.files.internal("frame.jpg"));
		imgRegion.setRegion(frameTexture);

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

}
