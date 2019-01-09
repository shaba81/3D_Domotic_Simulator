package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationListener;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.UBJsonReader;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.mygdx.controller.database.persistence.PostgreDAOFactory;
import com.mygdx.controller.database.persistence.dao.UserDAO;
import com.mygdx.speechToText.SpeechRecognition;

import net.sourceforge.javaflacencoder.FLACFileWriter;
import utilis.Utils;

public class GameScreen implements Screen {

	// final Drop game;

	private PerspectiveCamera camera;
	private ModelBatch modelBatch;
	private ModelInstance floorInstance;
	private Environment environment;
	private AnimationController entranceDoorAnimationController;
	private AnimationController bathDoorAnimationController;
	private ModelBuilder modelBuilder;
	private Material material; // used for the floor.
	private Material wallMaterial;
	private Material ceilingMaterial;
	private Texture floorTexture;
	private Texture wallTexture;
	private Texture ceilingTexture;
	private GameEntity player;
	private TextureAtlas atlas;
	private Skin skin;

	private Model entranceDoorModel;
	private ModelInstance entranceDoorInstance;
	private ModelInstance bathDoorInstance; 

	private Model wall;
	private Model wallBath;
	private Model wallDoor;
	private Model wallBathDoor;
	private Model overDoorWall;
	private Model ceiling;
	private ModelInstance sxWallInstance;
	private ModelInstance dxWallInstance;
	private ModelInstance frontWallSxInstance;
	private ModelInstance backWallInstance;
	private ModelInstance frontWallDxInstance;
	private ModelInstance overFrontWallInstance;
	private ModelInstance sxBathWallInstance;
	private ModelInstance frontSxBathWallInstance;
	private ModelInstance frontDxBathWallInstance;
	private ModelInstance overFrontBathWallInstance;
	private ModelInstance ceilingInstance;

	private float wallThickness = 2f;
	private float wallHeight = 40f;
	private Vector3 sxWallPosition;
	private Vector3 dxWallPosition;
	private Vector3 frontWallSxPosition;
	private Vector3 backWallPosition;
	private Vector3 frontWallDxPosition;
	private Vector3 overFrontDoorWallPosition;
	private Vector3 sxBathWallPosition;
	private Vector3 frontSxBathWallPosition;
	private Vector3 frontDxBathWallPosition;
	private Vector3 overFrontBathWallPosition;
	private Vector3 ceilingPosition;

	private GameEntity sxWallEntity;
	private GameEntity dxWallEntity;
	private GameEntity frontWallEntity;
	private GameEntity backWallEntity;
	private GameEntity bathRoom;
	private ArrayList<GameEntity> walls;

	private Model lampModel;
	private ModelInstance lampInstance;

	private Model tvModel;
	private ModelInstance tvInstance;

	private Model sofaModel;
	private ModelInstance sofaInstance;

	private Model tableModel;
	private ModelInstance tableInstance;

	private Model speakerModel;
	private ModelInstance speakerInstance;
	private ModelInstance speaker2Instance;
	private Music song1;
	private boolean activateSpeaker = false;

	private Texture tvScreenTexture;
	private TextureRegion tvScreenRegion;
	private Decal tvScreen;
	private boolean isTvOn = false;

	private Texture lightTexture;
	private TextureRegion lightRegion;
	private Decal light;
	private boolean isLightOn = false;

	private Texture micTexture;
	private Image micImage;
	
	private Label speakerMessage;
	private Label tvMessage;
	private Label lightMessage;
	private Label bathRoomMessage;
	private Label mainRoomMessage;

	private DecalBatch decalBatch;

	private SpriteBatch spriteBatch;
	private Stage stage;
	private Table messagesTable;
	private Viewport viewport;

	private float movementSpeed = 25f;
	private boolean forward = false;
	private boolean back = false;
	private boolean left = false;
	private boolean right = false;
	private boolean isSpeaking = false;
	private boolean nAccessButton;

	private SpeechRecognition speechRecognition;
	final Microphone mic = new Microphone(FLACFileWriter.FLAC);
	GSpeechDuplex duplex = new GSpeechDuplex("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");

	// Width and Height of the room's floor.
	private float floorWidth = 120;
	private float floorHeight = 120;

	public GameScreen() {
		// this.game = game;

		// Create camera sized to screens width/height with Field of View of 75 degrees
		camera = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas"));
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);

		// Move the camera 5 units back along the z-axis and look at the origin
		camera.position.set(0f, 15f, 50f);
		camera.lookAt(0f, 0f, 0f);

		// Near and Far (plane) represent the minimum and maximum ranges of the camera
		// in, um, units
		camera.near = 0.1f;
		camera.far = 300.0f;

		// A ModelBatch is like a SpriteBatch, just for models. Use it to batch up
		// geometry for OpenGL

		modelBuilder = new ModelBuilder();

		// Model loader needs a binary json reader to decode
		UBJsonReader jsonReader = new UBJsonReader();
		// Create a model loader passing in our json reader
		G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
		// Now load the model by name
		// Note, the model (g3db file ) and textures need to be added to the assets
		// folder of the Android proj
		entranceDoorModel = modelLoader.loadModel(Gdx.files.getFileHandle("Door_Component_BI3.g3db", FileType.Internal));
		lampModel = modelLoader.loadModel(Gdx.files.getFileHandle("lamp.g3db", FileType.Internal));
		tvModel = modelLoader.loadModel(Gdx.files.getFileHandle("TV.g3db", FileType.Internal));
//		sofaModel = modelLoader.loadModel(Gdx.files.getFileHandle("sofa.g3db", FileType.Internal));
//		tableModel = modelLoader.loadModel(Gdx.files.getFileHandle("table.g3db", FileType.Internal));
		speakerModel = modelLoader.loadModel(Gdx.files.getFileHandle("speaker.g3db", FileType.Internal));

		tvScreenTexture = new Texture(Gdx.files.internal("tvScreen.jpg"));
		tvScreenRegion = new TextureRegion(tvScreenTexture);
		tvScreen = Decal.newDecal(tvScreenRegion);
		tvScreen.setDimensions(10, 10);
		tvScreen.setPosition(-51, 15, -60);
		tvScreen.setRotationY(90);

		lightTexture = new Texture(Gdx.files.internal("light.png"));
		lightRegion = new TextureRegion(lightTexture);
		light = Decal.newDecal(lightRegion, true);
		light.lookAt(camera.position, camera.up);
		light.setDimensions(40, 40);
		light.setPosition(0, 26, -60);
		light.setRotationY(90);

		micTexture = new Texture(Gdx.files.internal("mic.png"));
		micImage = new Image(micTexture);
		micImage.setSize(120, 120);
		micImage.setPosition(Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() - 30));

		createRoom();

		player = new GameEntity(camera.position.x, camera.position.z, 5f, 5f);
		sxWallEntity = new GameEntity(sxWallPosition.x, 0, wallThickness, floorHeight);
		dxWallEntity = new GameEntity(dxWallPosition.x, 0, wallThickness, floorHeight);
		frontWallEntity = new GameEntity(10, 120, floorWidth / 2, wallThickness);
		backWallEntity = new GameEntity(0, 0, floorWidth / 2, wallThickness);
		bathRoom = new GameEntity(0,-135,70,60);

		walls = new ArrayList<GameEntity>();
		walls.add(sxWallEntity);
		walls.add(dxWallEntity);
		walls.add(frontWallEntity);
		walls.add(backWallEntity);

		// Now create an instance. Instance holds the positioning data, etc of an
		// instance of your model
		entranceDoorInstance = new ModelInstance(entranceDoorModel);
		entranceDoorInstance.transform.scale(0.1f, 0.1f, 0.1f);
		entranceDoorInstance.transform.translate(0, 0, 0);

		bathDoorInstance = new ModelInstance(entranceDoorModel);
		bathDoorInstance.transform.scale(0.1f, 0.1f, 0.1f);
		bathDoorInstance.transform.translate(30 * 10, 0, -80 * 10);

		lampInstance = new ModelInstance(lampModel);
		lampInstance.transform.scale(0.02f, 0.02f, 0.02f);
		lampInstance.transform.translate(0, 180 * 10, -floorHeight * 25);
		lampInstance.transform.rotate(Vector3.X, 180);

		tvInstance = new ModelInstance(tvModel);
		tvInstance.transform.scale(0.06f, 0.06f, 0.06f);
		tvInstance.transform.translate(-95 * 10, 25 * 10, -100 * 10);
		
//		sofaInstance = new ModelInstance(sofaModel);
//		sofaInstance.transform.scale(0.3f, 0.3f, 0.3f);
//		sofaInstance.transform.translate(0,30,40);
//		
//		tableInstance = new ModelInstance(tableModel);
//		tableInstance.transform.scale(0.2f, 0.2f, 0.2f);
//		tableInstance.transform.translate(0,20,60);
		
		speakerInstance = new ModelInstance(speakerModel);
		speakerInstance.transform.scale(0.02f, 0.02f, 0.02f);
		speakerInstance.transform.rotate(Vector3.Y, 340);
		speakerInstance.transform.translate(-290 * 14,40,-260 * 16);
		
		speaker2Instance = new ModelInstance(speakerModel);
		speaker2Instance.transform.scale(0.02f, 0.02f, 0.02f);
		speaker2Instance.transform.rotate(Vector3.Y, 10);
		speaker2Instance.transform.translate(-230 * 10,40,-90 * 10);

		entranceDoorAnimationController = new AnimationController(entranceDoorInstance);
		entranceDoorAnimationController.allowSameAnimation = true;
		
		bathDoorAnimationController = new AnimationController(bathDoorInstance);
		bathDoorAnimationController.allowSameAnimation = true;
		
		speakerMessage = new Label("Speakers are ON", skin);
		speakerMessage.setFontScale(2);
		speakerMessage.setColor(Color.RED);
		tvMessage = new Label("TV is ON", skin);
		tvMessage.setFontScale(2);
		tvMessage.setColor(Color.GREEN);
		lightMessage = new Label("Light is ON", skin);
		lightMessage.setFontScale(2);
		lightMessage.setColor(Color.YELLOW);
		bathRoomMessage = new Label("User is in the BATHROOM", skin);
		bathRoomMessage.setFontScale(3);
		bathRoomMessage.setColor(Color.BLUE);
		mainRoomMessage = new Label("User is in the MAIN ROOM", skin);
		mainRoomMessage.setFontScale(3);
		mainRoomMessage.setColor(Color.BLUE);

		// Finally we want some light, or we wont see our color. The environment gets
		// passed in during
		// the rendering process. Create one, then create an Ambient ( non-positioned,
		// non-directional ) light.
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1.0f));

		this.nAccessButton = false;
	}

	public void createRoom() {
		// Floor Initialization
		material = new Material();
		wallMaterial = new Material();
		ceilingMaterial = new Material();
		floorTexture = new Texture(Gdx.files.internal("floorTexture.jpg"));
		wallTexture = new Texture(Gdx.files.internal("walltexture.jpg"));
		ceilingTexture = new Texture(Gdx.files.internal("ceilingtexture.jpg"));
		material.set(new TextureAttribute(TextureAttribute.Diffuse, floorTexture));
		wallMaterial.set(new TextureAttribute(TextureAttribute.Diffuse, wallTexture));
		ceilingMaterial.set(new TextureAttribute(TextureAttribute.Diffuse, ceilingTexture));
		floorInstance = new ModelInstance(createPlaneModel(floorWidth, floorHeight, material, 0, 0, 1, 1));
		floorInstance.transform.translate(0, 0, -floorHeight / 2);
		floorInstance.transform.rotate(Vector3.X, 270);

		// Walls initialization
		sxWallPosition = new Vector3(-60, 20, -60);
		dxWallPosition = new Vector3(60, 20, -60);
		frontWallSxPosition = new Vector3(-33, 20, 0);
		backWallPosition = new Vector3(0, 20, -120);
		frontWallDxPosition = new Vector3(33, 20, 0);
		sxBathWallPosition = new Vector3(0,20, -100);
		
		frontSxBathWallPosition = new Vector3(12,20,-80);
		frontDxBathWallPosition = new Vector3(48,20,-80);
		overFrontBathWallPosition = new Vector3(30,30,-80);

		overFrontDoorWallPosition = new Vector3(0, 30, 0);
		ceilingPosition = new Vector3(0, wallHeight, -floorHeight / 2);

		wall = modelBuilder.createBox(wallThickness, wallHeight, floorWidth, wallMaterial,
				Usage.Position | Usage.Normal | Usage.TextureCoordinates);
		
		wallBath = modelBuilder.createBox(wallThickness, wallHeight, 40, wallMaterial,
				Usage.Position | Usage.Normal | Usage.TextureCoordinates);

		wallDoor = modelBuilder.createBox(wallThickness, wallHeight, floorWidth / 2 - 4f, wallMaterial,
				Usage.Position | Usage.Normal | Usage.TextureCoordinates);
		
		wallBathDoor = modelBuilder.createBox(wallThickness, wallHeight, 26, wallMaterial,
				Usage.Position | Usage.Normal | Usage.TextureCoordinates);

		overDoorWall = modelBuilder.createBox(wallThickness, wallHeight / 2, 10f, wallMaterial,
				Usage.Position | Usage.Normal | Usage.TextureCoordinates);

		ceiling = modelBuilder.createBox(wallThickness, floorWidth, floorHeight, ceilingMaterial,
				Usage.Position | Usage.Normal | Usage.TextureCoordinates);

		ceilingInstance = new ModelInstance(ceiling);
		ceilingInstance.transform.translate(ceilingPosition);
		ceilingInstance.transform.rotate(Vector3.Z, 90);

		sxWallInstance = new ModelInstance(wall);
		sxWallInstance.transform.translate(sxWallPosition);

		dxWallInstance = new ModelInstance(wall);
		dxWallInstance.transform.translate(dxWallPosition);

		frontWallSxInstance = new ModelInstance(wallDoor);
		frontWallSxInstance.transform.translate(frontWallSxPosition);
		frontWallSxInstance.transform.rotate(Vector3.Y, 270);

		backWallInstance = new ModelInstance(wall);
		backWallInstance.transform.translate(backWallPosition);
		backWallInstance.transform.rotate(Vector3.Y, 270);

		frontWallDxInstance = new ModelInstance(wallDoor);
		frontWallDxInstance.transform.translate(frontWallDxPosition);
		frontWallDxInstance.transform.rotate(Vector3.Y, 270);

		overFrontWallInstance = new ModelInstance(overDoorWall);
		overFrontWallInstance.transform.translate(overFrontDoorWallPosition);
		overFrontWallInstance.transform.rotate(Vector3.Y, 270);
		
		sxBathWallInstance = new ModelInstance(wallBath);
		sxBathWallInstance.transform.translate(sxBathWallPosition);
		
		overFrontBathWallInstance = new ModelInstance(overDoorWall);
		overFrontBathWallInstance.transform.translate(overFrontBathWallPosition);
		overFrontBathWallInstance.transform.rotate(Vector3.Y, 270);
		
		frontSxBathWallInstance = new ModelInstance(wallBathDoor);
		frontSxBathWallInstance.transform.translate(frontSxBathWallPosition);
		frontSxBathWallInstance.transform.rotate(Vector3.Y, 270);
		
		frontDxBathWallInstance = new ModelInstance(wallBathDoor);
		frontDxBathWallInstance.transform.translate(frontDxBathWallPosition);
		frontDxBathWallInstance.transform.rotate(Vector3.Y, 270);
		

		// inizializzazione e settaggio della lingua italiana (funziona anche in
		// inglese)
		speechRecognition = new SpeechRecognition();
		duplex.setLanguage("it");
	}

	@Override
	public void show() {

		modelBatch = new ModelBatch();
		decalBatch = new DecalBatch(new CameraGroupStrategy(camera));

		spriteBatch = new SpriteBatch();
		viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage = new Stage(viewport, spriteBatch);
		messagesTable = new Table();
		messagesTable.setFillParent(true);
		messagesTable.bottom();
		
		song1 = Gdx.audio.newMusic(Gdx.files.internal("song1.mp3"));

		Gdx.input.setCursorCatched(true);
		Gdx.input.setInputProcessor(new InputProcessor() {
			private int dragX, dragY;
			float rotateSpeed = 0.05f;

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				Vector3 direction = camera.direction.cpy();

				// rotating on the y axis
				float x = dragX - screenX;

				camera.rotate(Vector3.Y, x * rotateSpeed);

				// rotating on the x and z axis is different
				float y = (float) Math.sin((double) (dragY - screenY) / 180f);
				if (Math.abs(camera.direction.y + y * (rotateSpeed * 5.0f)) < 0.9) {
					camera.direction.y += y * (rotateSpeed * 5.0f);
				}

				camera.update();
				dragX = screenX;
				dragY = screenY;

				return true;
			}

			@Override
			public boolean keyDown(int keycode) {
				if (keycode == Input.Keys.W) {
					forward = true;
				}
				if (keycode == Input.Keys.A) {
					left = true;
				}
				if (keycode == Input.Keys.S) {
					back = true;
				}
				if (keycode == Input.Keys.D) {
					right = true;
				}
				if (keycode == Input.Keys.T) {
					isTvOn = !isTvOn;
				}
				if (keycode == Input.Keys.L) {
					isLightOn = !isLightOn;
				}
				if (keycode == Input.Keys.R) {
					isSpeaking = true;
					speechRecognition.startingSpeechRecognition(duplex, mic);
					speechRecognition.getResponse(duplex);

				}
				if (keycode == Input.Keys.B) {
					activateSpeaker = !activateSpeaker;
				}
				if (keycode == Input.Keys.N) {
					nAccessButton = true;
				}
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				if (keycode == Input.Keys.W) {
					forward = false;
				}
				if (keycode == Input.Keys.A) {
					left = false;
				}
				if (keycode == Input.Keys.S) {
					back = false;
				}
				if (keycode == Input.Keys.D) {
					right = false;
				}
				if (keycode == Input.Keys.R) {
					isSpeaking = false;
					speechRecognition.stopSpeechRecognition(duplex, mic);
				}
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				// TODO Auto-generated method stub
				return false;
			}

		});
	}

	public void OpenDoor() {

		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

			entranceDoorAnimationController.setAnimation("Plane.001|Door", 1, new AnimationListener() {
				@Override
				public void onEnd(AnimationController.AnimationDesc animation) {
					// controller.queue("other anim",-1,1f,null,0f);
				}

				@Override
				public void onLoop(AnimationController.AnimationDesc animation) {
				}
			});
			
			bathDoorAnimationController.setAnimation("Plane.001|Door", 1, new AnimationListener() {
				@Override
				public void onEnd(AnimationController.AnimationDesc animation) {
					// controller.queue("other anim",-1,1f,null,0f);
				}

				@Override
				public void onLoop(AnimationController.AnimationDesc animation) {
				}
			});
		}
	}
	
	public void showMessages() {
		if(checkRoom().equals("bathroom")) {
			messagesTable.add(bathRoomMessage);
			messagesTable.row();
		}
		if(isTvOn) {
			messagesTable.add(tvMessage);
			messagesTable.row();
		}
		if(isLightOn) {
			messagesTable.add(lightMessage);
			messagesTable.row();
		}
			
		if(activateSpeaker) {
			messagesTable.add(speakerMessage);
			messagesTable.row();
		}
			
		stage.addActor(messagesTable);
		stage.act();
		stage.draw();
		messagesTable.clear();
	}

	public void drawMic() {
		stage.addActor(micImage);
		stage.act();
		stage.draw();
		stage.clear();
	}

	public void startTV() {
		if (isTvOn) {
			decalBatch.add(tvScreen);
			decalBatch.flush();
		}
	}

	public void turnLights() {
		if (isLightOn) {
			decalBatch.add(light);
			light.lookAt(camera.position, camera.up);
			decalBatch.flush();
		}
	}
	
	public void startSpeakers() {
		if(activateSpeaker) {
			song1.play();
		} else {
			song1.pause();
		}
		
	}
	
	public String checkRoom() {
		if(bathRoom.contains(player))
			return new String("bathroom");
		
		return new String("mainRoom");
	}

	public void walk(float timeElapsed) {
		float speed = movementSpeed;
		if ((forward | back) & (right | left)) {
			speed /= Math.sqrt(2);
		}

		if (forward) {
			Vector3 v = camera.direction.cpy();
			v.y = 0f;
			v.x *= speed * timeElapsed;
			v.z *= speed * timeElapsed;
			camera.translate(v);
			camera.update();
			player.updatePosition(camera.position.x, camera.position.z);
		}
		if (back) {
			Vector3 v = camera.direction.cpy();
			v.y = 0f;
			v.x = -v.x;
			v.z = -v.z;
			v.x *= speed * timeElapsed;
			v.z *= speed * timeElapsed;
			camera.translate(v);
			camera.update();
			player.updatePosition(camera.position.x, camera.position.z);
		}
		if (left) {
			Vector3 v = camera.direction.cpy();
			v.y = 0f;
			v.rotate(Vector3.Y, 90);
			v.x *= speed * timeElapsed;
			v.z *= speed * timeElapsed;
			camera.translate(v);
			camera.update();
			player.updatePosition(camera.position.x, camera.position.z);
		}
		if (right) {
			Vector3 v = camera.direction.cpy();
			v.y = 0f;
			v.rotate(Vector3.Y, -90);
			v.x *= speed * timeElapsed;
			v.z *= speed * timeElapsed;
			camera.translate(v);
			camera.update();
			player.updatePosition(camera.position.x, camera.position.z);

		}

		for (GameEntity w : walls) {
			// System.out.println(player.isColliding(w));
		}

	}

	private Model createPlaneModel(final float width, final float height, final Material material, final float u1,
			final float v1, final float u2, final float v2) {

		modelBuilder.begin();
		MeshPartBuilder bPartBuilder = modelBuilder.part("rect", GL20.GL_TRIANGLES,
				Usage.Position | Usage.Normal | Usage.TextureCoordinates, material);
		// NOTE ON TEXTURE REGION, MAY FILL OTHER REGIONS, USE GET region.getU() and so
		// on
		bPartBuilder.setUVRange(u1, v1, u2, v2);
		bPartBuilder.rect(-(width * 0.5f), -(height * 0.5f), 0, (width * 0.5f), -(height * 0.5f), 0, (width * 0.5f),
				(height * 0.5f), 0, -(width * 0.5f), (height * 0.5f), 0, 0, 0, -1);

		return (modelBuilder.end());
	}

	@Override
	public void render(float delta) {
		try {
			// You've seen all this before, just be sure to clear the GL_DEPTH_BUFFER_BIT
			// when working in 3D
			Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			Gdx.gl.glClearColor(1, 1, 1, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
			spriteBatch.setProjectionMatrix(camera.combined);
			
			checkRoom();

			walk(Gdx.graphics.getDeltaTime());
			OpenDoor();

			entranceDoorAnimationController.update(Gdx.graphics.getDeltaTime());
			bathDoorAnimationController.update(Gdx.graphics.getDeltaTime());
			modelBatch.begin(camera);

			modelBatch.render(entranceDoorInstance, environment);
			modelBatch.render(bathDoorInstance, environment);
			modelBatch.render(lampInstance, environment);
			modelBatch.render(tvInstance, environment);
//			modelBatch.render(sofaInstance, environment);
//			modelBatch.render(tableInstance, environment);
			modelBatch.render(speakerInstance, environment);
			modelBatch.render(speaker2Instance, environment);

			modelBatch.render(sxWallInstance, environment);
			modelBatch.render(dxWallInstance, environment);
			modelBatch.render(frontWallSxInstance, environment);
			modelBatch.render(backWallInstance, environment);
			modelBatch.render(frontWallDxInstance, environment);
			modelBatch.render(overFrontWallInstance, environment);
			modelBatch.render(sxBathWallInstance, environment);
			modelBatch.render(overFrontBathWallInstance, environment);
			modelBatch.render(frontSxBathWallInstance, environment);
			modelBatch.render(frontDxBathWallInstance, environment);
			modelBatch.render(floorInstance, environment);
			modelBatch.render(ceilingInstance, environment);

			modelBatch.end();

			if (isSpeaking)
				drawMic();
			

			startTV();
			turnLights();
			startSpeakers();
			
			showMessages();

			if (this.nAccessButton) {
				this.nAccessButton = false;
				PostgreDAOFactory postgreDAOFactory = new PostgreDAOFactory();
				UserDAO utenteDAO = postgreDAOFactory.getUtenteDAO();

				if (utenteDAO.isFirstRegistrationForThisForniture(Utils.ID_SUPPLY, Utils.ID_USER)) {
					Utils.isFirstAccess = true;
					ScreenManager.getInstance().showScreen(ScreenEnum.LOGIN_SCREEN);
				} else {
					ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
				}

			}

			// Use this to change Screen
			if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {

				// gestire i vari casi in cui si pu� volere uscire
				Gdx.app.exit();
				// ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
				// game.setScreen(game.menu);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void dispose() {
		this.modelBatch.dispose();
		this.entranceDoorModel.dispose();
		this.lampModel.dispose();
		this.tvModel.dispose();
		this.speakerModel.dispose();
		this.tableModel.dispose();
		this.sofaModel.dispose();
		this.spriteBatch.dispose();
		this.stage.dispose();
		this.song1.dispose();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}
}
