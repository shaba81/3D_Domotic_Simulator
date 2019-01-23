package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.interfaces.AbstractScreen;

public class LogScreen extends AbstractScreen {

	public LogScreen() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void show() {
		super.show();

		/*
		 * Tabella dell' header
		 */
		Table t1 = new Table(skin);
		t1.setClip(true);
		t1.add(new Label("Nome", skin)).width(500);
		t1.add(new Label("Data", skin)).width(500);
		t1.add(new Label("Comando", skin)).width(500);
		t1.row();
		ScrollPane s1Pane = new ScrollPane(t1, skin);

		/*
		 * Tabella con i dati
		 */
		Table t2 = new Table(skin);
		for (int row = 0; row < 50; row++) { // with small number of rows you can see header
			t2.add(new Label("Giacomo," + row, skin)).width(500);
			t2.add(new Label("12/10/2018," + row, skin)).width(500);
			t2.add(new Label("Fare la cacca," + row, skin)).width(500);
			t2.row();
		}
		ScrollPane s2Pane = new ScrollPane(t2, skin);

		/*
		 * Tabella che racchiude tutto
		 */
		Table t = new Table(skin);
		t.setFillParent(true);
		t.add(s1Pane).expandX();
		t.row();
		t.add(s2Pane).top().expand();
		t.row();

		backButton = new TextButton("Back", skin);
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				back = true;
			}
		});
		backButton.bottom();
		t.add(backButton);

		stage.addActor(t);
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.stage.act();
		this.stage.draw();
		
		if(back) {
			//Dove deve andare se preme il pulsante 
		}

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
