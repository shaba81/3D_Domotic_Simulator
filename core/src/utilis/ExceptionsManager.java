package utilis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javax.mail.MessagingException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ExceptionsManager {

	private Stage stage;
	private Skin skin;
	private SpriteBatch batch;
	protected OrthographicCamera camera;
	private Viewport viewport;
	private TextureAtlas atlas;

	private static ExceptionsManager exceptionsManager;

	private ExceptionsManager() {
		this.atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas"));
		this.skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);

		this.camera = new OrthographicCamera();
		this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
		this.batch = new SpriteBatch();
		this.stage = new Stage(this.viewport, this.batch);
	}

	public static ExceptionsManager getExceptionsManager() {
		if( exceptionsManager == null )
			exceptionsManager = new ExceptionsManager();

		return exceptionsManager;
	}

	public void manageException(Exception exception) {
		if( exception instanceof SQLException ) {
			Utils.showMessageDialog(exception.getMessage(), this.skin, this.stage);
			System.out.println("1: " + exception.getMessage());
		} else if( exception instanceof IOException) {
			Utils.showMessageDialog(exception.getMessage(), this.skin, this.stage);
			System.out.println("2: " + exception.getMessage());
		} else if( exception instanceof FileNotFoundException ) {
			Utils.showMessageDialog(exception.getMessage(), this.skin, this.stage);
			System.out.println("3: " + exception.getMessage());
		} else if( exception instanceof MessagingException ) {
			Utils.showMessageDialog(exception.getMessage(), this.skin, this.stage);
			System.out.println("4: " + exception.getMessage());
		}
	}

	public void manageException(Exception exception, Skin skin, Stage stage) {
		if( exception instanceof SQLException ) {
			Utils.showMessageDialog("", skin, stage);
			System.out.println("1: " + exception.getMessage());
		} else if( exception instanceof IOException) {
			Utils.showMessageDialog("", skin, stage);
			System.out.println("2: " + exception.getMessage());
		} else if( exception instanceof FileNotFoundException ) {
			Utils.showMessageDialog("", skin, stage);
			System.out.println("3: " + exception.getMessage());
		} else if( exception instanceof MessagingException ) {
			Utils.showMessageDialog("", skin, stage);
			System.out.println("4: " + exception.getMessage());
		}
	}

}
