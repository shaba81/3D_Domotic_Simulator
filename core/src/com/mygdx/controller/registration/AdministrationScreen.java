package com.mygdx.controller.registration;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.controller.Controller;
import com.mygdx.controller.database.model.User;
import com.mygdx.game.ScreenManager;
import com.mygdx.interfaces.AbstractScreen;
import com.mygdx.simulator.factory_methos_screens.LogScreenCreator;
import com.mygdx.simulator.factory_methos_screens.RegistrationCredentialsScreenCreator;

import utilis.ExceptionsManager;
import utilis.Utils;

public class AdministrationScreen extends AbstractScreen {

	private Table rightTable;

	private Table centerTable;

	private TextButton registrationButton;
	private TextButton showHouseInteractionButton;
	private TextButton backButton;

	private boolean registration;
	private boolean showHouseInteraction;
	private boolean back;

	private String[] labelName, pathLabelName;
	private int index;

	private Label nameLabel, nameLabel2, nameLabel3, nameLabel4, nameLabel5, nameLabel6;

	private ImageButton user1, user2, user3, user4, user5, user6;

	public AdministrationScreen() {

		this.registration = false;
		this.showHouseInteraction = false;
		this.back = false;

		rightTable = new Table();
		rightTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		rightTable.center();

		centerTable = new Table();
		centerTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		centerTable.center();

		labelName = new String[6];
		pathLabelName = new String[6];
		index = 0;

	}

	@Override
	public void show() {
		try {

			super.show();
			this.mainTable.center();
			this.rightTable.right();

			this.registrationButton = new TextButton("Registration", skin);
			this.showHouseInteractionButton = new TextButton("Show house interaction", skin);
			this.backButton = new TextButton("Back to Menu", skin);

			// In ascolto di eventi
			this.registrationButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					registration = true;
				}
			});

			this.showHouseInteractionButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					showHouseInteraction = true;
				}
			});

			this.backButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					back = true;
				}
			});

			HashMap<Integer, User> users = Controller.getController().getUserDAO().getAllUser();

			for (Integer key : users.keySet()) {
				labelName[index] = users.get(key).getNickName();
				pathLabelName[index] = users.get(key).getPathImage();
				index++;
			}

			if (labelName[0] != null) {
				nameLabel = new Label("User: " + labelName[0], skin);
				Image img_user_unclicked = new Image(new Texture(pathLabelName[0]));
				Image img_user_clicked = new Image(new Texture(pathLabelName[0]));
				user1 = new ImageButton(img_user_unclicked.getDrawable(), img_user_clicked.getDrawable());
				user1.addListener(new ClickListener() {
					public void clicked(InputEvent event, float x, float y) {
						doAction(users, 0);
					}

				});
			}

			if (labelName[1] != null) {
				nameLabel2 = new Label("User: " + labelName[1], skin);
				Image img_user_unclicked2 = new Image(new Texture(pathLabelName[1]));
				Image img_user_clicked2 = new Image(new Texture(pathLabelName[1]));
				user2 = new ImageButton(img_user_unclicked2.getDrawable(), img_user_clicked2.getDrawable());
				user2.addListener(new ClickListener() {
					public void clicked(InputEvent event, float x, float y) {
						doAction(users, 1);
					}
				});
			}

			if (labelName[2] != null) {
				nameLabel3 = new Label("User: " + labelName[2], skin);
				Image img_user_unclicked3 = new Image(new Texture(pathLabelName[2]));
				Image img_user_clicked3 = new Image(new Texture(pathLabelName[2]));
				user3 = new ImageButton(img_user_unclicked3.getDrawable(), img_user_clicked3.getDrawable());
				user3.addListener(new ClickListener() {
					public void clicked(InputEvent event, float x, float y) {
						doAction(users, 2);
					}

				});
			}

			if (labelName[3] != null) {
				nameLabel4 = new Label("User: " + labelName[3], skin);
				Image img_user_unclicked4 = new Image(new Texture(pathLabelName[3]));
				Image img_user_clicked4 = new Image(new Texture(pathLabelName[3]));
				user4 = new ImageButton(img_user_unclicked4.getDrawable(), img_user_clicked4.getDrawable());
				user4.addListener(new ClickListener() {
					public void clicked(InputEvent event, float x, float y) {
						doAction(users, 3);
					}

				});
			}

			if (labelName[4] != null) {
				nameLabel5 = new Label("User: " + labelName[4], skin);
				Image img_user_unclicked5 = new Image(new Texture(pathLabelName[4]));
				Image img_user_clicked5 = new Image(new Texture(pathLabelName[4]));
				user5 = new ImageButton(img_user_unclicked5.getDrawable(), img_user_clicked5.getDrawable());
				user5.addListener(new ClickListener() {
					public void clicked(InputEvent event, float x, float y) {
						doAction(users, 4);
					}

				});
			}

			if (labelName[5] != null) {
				nameLabel6 = new Label("User: " + labelName[5], skin);
				Image img_user_unclicked6 = new Image(new Texture(pathLabelName[5]));
				Image img_user_clicked6 = new Image(new Texture(pathLabelName[5]));
				user6 = new ImageButton(img_user_unclicked6.getDrawable(), img_user_clicked6.getDrawable());
				user6.addListener(new ClickListener() {
					public void clicked(InputEvent event, float x, float y) {
						doAction(users, 5);
					}

				});
			}

			// label per distanziare lol
			Label dist = new Label("", skin);

			centerTable.add(nameLabel);
			centerTable.add(dist).width(62);
			centerTable.add(nameLabel2);

			// 1-1
			centerTable.row();
			centerTable.add(user1).size(120, 120);

			centerTable.add(dist).width(62);
			// 1-2
			centerTable.add(user2).size(120, 120).width(120);
			centerTable.row();

			centerTable.add(nameLabel3);
			centerTable.add(dist).width(62);
			centerTable.add(nameLabel4);
			// 2-1
			centerTable.row();
			centerTable.add(user3).size(120, 120);

			centerTable.add(dist).width(62);
			// 2-2
			centerTable.add(user4).size(120, 120).width(120);
			centerTable.row();

			centerTable.add(nameLabel5);
			centerTable.add(dist).width(62);
			centerTable.add(nameLabel6);

			// 3-1
			centerTable.row();
			centerTable.add(user5).size(120, 120);

			centerTable.add(dist).width(62);
			// 3-2
			centerTable.add(user6).size(120, 120).width(120);
			centerTable.row();

			this.rightTable.add(this.registrationButton);
			this.rightTable.row();
			this.rightTable.add(this.showHouseInteractionButton);
			this.rightTable.row();
			this.rightTable.add(this.backButton);
			this.rightTable.row();

			mainTable.setFillParent(true);
			// Aggiungo table allo stage
			stage.addActor(mainTable);
			stage.addActor(this.rightTable);
			stage.addActor(centerTable);
		} catch (Exception e) {
			ExceptionsManager.getExceptionsManager().manageException(e, skin, stage);
		}
	}

	protected void doAction(HashMap<Integer, User> users, int index) {
		System.out.println(pathLabelName[index]);
		Utils.credentials.clear();
		Utils.credentials.add(0, users.get(index).getEmail());

		Utils.credentials.add(1, users.get(index).getTelefonNumber());
		Utils.credentials.add(2, users.get(index).getNickName());
		Utils.credentials.add(3, pathLabelName[index]);
		Utils.credentials.add(4, users.get(index).getIdUser());
		Utils.changeUserCredentials = true;
		Utils.lenghtUserNameForShowPopUp = labelName[index].length() + 1; // il +1 è per '?'
		System.out.println("doAction 0: " + users.get(index).getEmail() + " 1: " + users.get(index).getTelefonNumber()
				+ " 2: " + users.get(index).getNickName() + " 3: " + pathLabelName[index] + " 4: "
				+ users.get(index).getIdUser());
		Utils.showPopUp(Utils.ADMIN_REG_CRED_CHANGE_POPUP + labelName[index] + "?", skin, stage,
				Utils.REGISTRATION_SCREEN_POP);
	}

	@Override
	public void render(float delta) {
		try {
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			this.batch.begin();

			this.batch.end();

			this.stage.act();
			this.stage.draw();

			if (this.registration) {
				this.registration = false;
//			Utils.resp = Utils.ADMIN_REGISTERS_A_NEW_USER;
				if (Controller.getController().getUserDAO().registrationIsAvailable()) {
					Utils.saveOnLog(Utils.ADMIN_REGISTERS_A_NEW_USER);
					ScreenManager.getInstance().showScreen(new RegistrationCredentialsScreenCreator());
				} else {
					Utils.showMessageDialog(Utils.REGISTRATION_NOT_PERMITTED, skin, stage);
				}

			}

			if (this.showHouseInteraction) {
				this.showHouseInteraction = false;
				/*
				 * TODO SCREEN PER IL LOG
				 */
				ScreenManager.getInstance().showScreen(new LogScreenCreator());
			}

			if (this.back) {
				this.back = false;
				Utils.showPopUp(Utils.ADMINISTRATION_SCREEN_BACK_POPUP, skin, stage, Utils.MAIN_SCREEN_POP);
			}
		} catch (Exception e) {
			ExceptionsManager.getExceptionsManager().manageException(e, skin, stage);
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
