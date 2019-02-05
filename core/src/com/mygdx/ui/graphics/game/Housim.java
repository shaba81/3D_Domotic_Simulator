package com.mygdx.ui.graphics.game;

import java.util.ArrayList;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.UBJsonReader;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.application.exception_manager.ExceptionsManager;
import com.mygdx.application.input_manager.InputManager;
import com.mygdx.application.screen_manager.ScreenManager;
import com.mygdx.domain.controller.Controller;
import com.mygdx.domain.models.User;
import com.mygdx.foundation.utils.Music;
import com.mygdx.foundation.utils.Utils;
import com.mygdx.services.factory_methos_screens.LoginScreenCreator;
import com.mygdx.services.factory_methos_screens.MainMenuScreenCreator;
import com.mygdx.services.proxy.AbstractCommand;
import com.mygdx.services.vocal_recognition.TextToSpeech;

public class Housim implements Screen {

	/*
	 * Come da caso d'uso
	 */
	private User user;

	public InputManager inputManager;

	private PerspectiveCamera camera;
	private ModelBatch modelBatch;
	private ModelInstance floorInstance;
	private Environment environment;
	private AnimationController entranceDoorAnimationController;
	private AnimationController bathDoorAnimationController;
	private AnimationController fanAnimationController;
	private ModelBuilder modelBuilder;
	private Material material; // used for the floor.
	private Material wallMaterial;
	private Material ceilingMaterial;
	private Texture floorTexture;
	private Texture wallTexture;
	private Texture ceilingTexture;
	private static GameEntity player;
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
	private static GameEntity bathRoom;
	private static GameEntity mainRoom1;
	private static GameEntity mainRoom2;
	private ArrayList<GameEntity> walls;

	private Model lampModel;
	private ModelInstance lampInstance;

	private Model tvModel;
	private ModelInstance tvInstance;

	private Model fanModel;
	private ModelInstance fanInstance;

	private Model safeModel;
	private Material safeMaterial;
	private Texture safeTexture;
	private ModelInstance safeInstance;

	private Model speakerModel;
	private ModelInstance speakerInstance;
	private ModelInstance speaker2Instance;
	private Sound song1;

	private char collidingWho;

	private Texture tvScreenTexture;
	private TextureRegion tvScreenRegion;
	private Decal tvScreen;

	private Texture lightTexture;
	private TextureRegion lightRegion;
	private Decal light;

	private Texture micTexture;
	private Image micImage;

	private ImageButton exit;

	private Label speakerMessage;
	private Label helpMessage;
	private Label tvMessage;
	private Label lightMessage;
	private Label bathRoomMessage;
	private Label safeBox;
	private Label mainRoomMessage;
	private Label vocalMessage;
	private String vocalCommand = "";
	private Label waitMessage;
	private boolean showWait = false;

	private DecalBatch decalBatch;

	private SpriteBatch spriteBatch;
	private Stage stage;
	private Table messagesTable;
	private Table vocalMessageTable;
	private Viewport viewport;

	private TextToSpeech textToSpeech;

	// Width and Height of the room's floor.
	private float floorWidth = 120;
	private float floorHeight = 120;

	
	private AbstractCommand command;

	private Music music;
	public boolean printHelpCommand = false;

	/*
	 * SINGLETON
	 */
	private static Housim housim;

	private Housim() {}

	public static Housim getHousim() {
		if (housim == null)
			housim = new Housim();
		return housim;
	}

	public void init() {

		// Create camera sized to screens width/height with Field of View of 75 degrees
		camera = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas"));
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);

		inputManager = new InputManager(camera);

		collidingWho = ' ';

		// Move the camera 5 units back along the z-axis and look at the origin
		camera.position.set(Utils.positionVector);
		camera.lookAt(Utils.positionVector);

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
		entranceDoorModel = modelLoader
				.loadModel(Gdx.files.getFileHandle("Door_Component_BI3.g3db", FileType.Internal));
		lampModel = modelLoader.loadModel(Gdx.files.getFileHandle("lamp.g3db", FileType.Internal));
		tvModel = modelLoader.loadModel(Gdx.files.getFileHandle("TV.g3db", FileType.Internal));
		speakerModel = modelLoader.loadModel(Gdx.files.getFileHandle("speaker.g3db", FileType.Internal));
		fanModel = modelLoader.loadModel(Gdx.files.getFileHandle("Fan_Done5_Rigged.g3db", FileType.Internal));
		safeMaterial = new Material();
		safeTexture = new Texture(Gdx.files.internal("safeTexture.png"));
		safeMaterial.set(new TextureAttribute(TextureAttribute.Diffuse, safeTexture));
		safeModel = modelBuilder.createBox(20, 20, 20, safeMaterial,
				Usage.Position | Usage.Normal | Usage.TextureCoordinates);

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
		bathRoom = new GameEntity(0, -135, 70, 60);
		mainRoom1 = new GameEntity(0, -80, 70, 85);
		mainRoom2 = new GameEntity(-60, -135, 70, 140);

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

		safeInstance = new ModelInstance(safeModel);
		safeInstance.transform.scale(0.6f, 0.6f, 0.6f);
		safeInstance.transform.translate(10, 10, -170);

		speakerInstance = new ModelInstance(speakerModel);
		speakerInstance.transform.scale(0.02f, 0.02f, 0.02f);
		speakerInstance.transform.rotate(Vector3.Y, 340);
		speakerInstance.transform.translate(-290 * 14, 40, -260 * 16);

		speaker2Instance = new ModelInstance(speakerModel);
		speaker2Instance.transform.scale(0.02f, 0.02f, 0.02f);
		speaker2Instance.transform.rotate(Vector3.Y, 10);
		speaker2Instance.transform.translate(-230 * 10, 40, -90 * 10);

		fanInstance = new ModelInstance(fanModel);
		fanInstance.transform.scale(0.01f, 0.01f, 0.01f);
		fanInstance.transform.translate(140 * 30, 25 * 40, -370 * 30);
		fanInstance.transform.rotate(Vector3.Y, 90);

		entranceDoorAnimationController = new AnimationController(entranceDoorInstance);
		entranceDoorAnimationController.allowSameAnimation = true;

		bathDoorAnimationController = new AnimationController(bathDoorInstance);
		bathDoorAnimationController.allowSameAnimation = true;

		fanAnimationController = new AnimationController(fanInstance);
		fanAnimationController.allowSameAnimation = true;

		speakerMessage = new Label("Speakers are ON", skin);
		speakerMessage.setFontScale(2);
		speakerMessage.setColor(Color.RED);
		tvMessage = new Label("TV is ON", skin);
		tvMessage.setFontScale(2);
		tvMessage.setColor(Color.GREEN);
		lightMessage = new Label("Light is ON", skin);
		lightMessage.setFontScale(2);
		lightMessage.setColor(Color.YELLOW);
		bathRoomMessage = new Label("User is in ROOM B", skin);
		bathRoomMessage.setFontScale(3);
		bathRoomMessage.setColor(Color.BLUE);
		mainRoomMessage = new Label("User is in ROOM A", skin);
		mainRoomMessage.setFontScale(3);
		mainRoomMessage.setColor(Color.BLUE);
		vocalMessage = new Label(vocalCommand, skin);
		vocalMessage.setScale(3);
		vocalMessage.setColor(Color.RED);
		waitMessage = new Label("Wait . . .", skin);
		waitMessage.setScale(3);
		waitMessage.setColor(Color.RED);
		helpMessage = new Label("HELP COMMAND", skin);
		helpMessage.setFontScale(2);
		helpMessage.setColor(Color.MAGENTA);
		safeBox = new Label("Safe box is open", skin);
		safeBox.setFontScale(3);
		safeBox.setColor(Color.DARK_GRAY);

		
		Utils.doorIsOpen = false;
		// Finally we want some light, or we wont see our color. The environment gets
		// passed in during
		// the rendering process. Create one, then create an Ambient ( non-positioned,
		// non-directional ) light.
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1.0f));

		inputManager.nAccessButton = false;

		if (user == null & command == null & checkRoom().equals("")) {
			user = new User();
			user.setIdUser("-1");
		}

		Utils.userLogged = user.getIdUser();
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
		sxBathWallPosition = new Vector3(0, 20, -100);

		frontSxBathWallPosition = new Vector3(12, 20, -80);
		frontDxBathWallPosition = new Vector3(48, 20, -80);
		overFrontBathWallPosition = new Vector3(30, 30, -80);

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

	}

	@Override
	public void show() {

		init();

		modelBatch = new ModelBatch();
		decalBatch = new DecalBatch(new CameraGroupStrategy(camera));

		spriteBatch = new SpriteBatch();
		viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage = new Stage(viewport, spriteBatch);
		messagesTable = new Table();
		messagesTable.setFillParent(true);
		messagesTable.bottom();
		vocalMessageTable = new Table();
		vocalMessageTable.setFillParent(true);
		vocalMessageTable.top();

		// da provare java
		Gdx.input.setCursorCatched(true);
		Gdx.input.setInputProcessor(inputManager);

	}

	public void OpenDoorA() {

		if (inputManager.openDoorA) {

			entranceDoorAnimationController.setAnimation("Plane.001|Door", 1, new AnimationListener() {
				@Override
				public void onEnd(AnimationController.AnimationDesc animation) {
					inputManager.openDoorA = false;
					Utils.doorIsOpen = false;
				}

				@Override
				public void onLoop(AnimationController.AnimationDesc animation) {
				}
			});
			inputManager.openDoorA = false;

		}

	}

	public void OpenDoorB() {

		if (inputManager.openDoorB) {
			bathDoorAnimationController.setAnimation("Plane.001|Door", 1, new AnimationListener() {
				@Override
				public void onEnd(AnimationController.AnimationDesc animation) {
					inputManager.openDoorB = false;
					Utils.doorIsOpen = false;
				}

				@Override
				public void onLoop(AnimationController.AnimationDesc animation) {
				}
			});
			inputManager.openDoorB = false;
		}

	}

	public void activateFan() {
		if (inputManager.activateFan) {
			fanAnimationController.setAnimation("Armature|ArmatureAction", 1, new AnimationListener() {
				@Override
				public void onEnd(AnimationController.AnimationDesc animation) {
					inputManager.activateFan = false;
				}

				@Override
				public void onLoop(AnimationController.AnimationDesc animation) {
				}
			});
			inputManager.activateFan = false;
		}
	}

	public int contWrongCommand = 0;

	public void executeCommand() {
		if (checkRoom().equals("mainRoom") || checkRoom().equals("bathroom")) {
			// vocalCommand = readFromFile();
			try {
				vocalCommand = Utils.resp;
				vocalMessage.setText(vocalCommand);
				vocalMessageTable.add(vocalMessage);
				vocalMessageTable.row();

				String commandTypeOpen1 = "Apri";
				String commandTypeOpen2 = "Accendi";
				String commandTypeClose1 = "Spegni";
				String commandTypeClose2 = "Chiudi";
				String commadExit = "Esci";
				String helpCommand = "aiuto";
				String commandTypeRaise = "Alza";

				String objectToActivate = vocalCommand.substring(vocalCommand.lastIndexOf(" ") + 1);
				objectToActivate = objectToActivate.toLowerCase();

				if (vocalCommand.toLowerCase().contains(commandTypeOpen1.toLowerCase())
						|| vocalCommand.toLowerCase().contains(commandTypeOpen2.toLowerCase())) {
					printHelpCommand = false;

					if (objectToActivate.equals("luce") || objectToActivate.equals("lampada")) {
						contWrongCommand = 0;
						if (checkRoom().equals("mainRoom")) {

							command.lightOn();
							return;
						} else {
							wrongCommandLocation("accendere la luce");
							wcl = false;
							inputManager.doCommand = false;

						}
						return;
					} else if (objectToActivate.equals("tv") || objectToActivate.equals("televisione")) {
						contWrongCommand = 0;
						if (checkRoom().equals("mainRoom")) {

							command.tvOn();
						} else {
							wrongCommandLocation("accendere la televisione");
							wcl = false;
							inputManager.doCommand = false;

						}
						return;
					} else if (objectToActivate.equals("stereo") || objectToActivate.equals("radio")) {
						contWrongCommand = 0;
						if (checkRoom().equals("mainRoom")) {

							command.speakerOn();
						} else {
							wrongCommandLocation("accendere lo stereo");
							wcl = false;
							inputManager.doCommand = false;

						}
						return;
					} else if (objectToActivate.equals("porta")) {
						contWrongCommand = 0;
						if (checkRoom().equals("mainRoom") || checkRoom().equals("bathroom")) {
							
							command.openDoorB();
							Utils.doorIsOpen= true;
							Utils.resp = ""; // DA METTERE NEL LOG
						} else {
							wcl = false;
							inputManager.doCommand = false;

						}
						return;
					} else if (objectToActivate.equals("cassaforte")) {
						contWrongCommand = 0;
						if (checkRoom().equals("bathroom")) {
							command.safeBoxOn();
							return;
						} else {
							wrongCommandLocation("aprire la cassaforte");
							wcl = false;
							inputManager.doCommand = false;
						}
						return;
					} else if (objectToActivate.equals("ventilatore") || objectToActivate.contains("aria")) {
						contWrongCommand = 0;
						if (checkRoom().equals("bathroom")) {

							command.fanOn();
						} else {
							wrongCommandLocation("accendere il ventilatore");
							wcl = false;
							inputManager.doCommand = false;
						}
						return;
					} else {
						contWrongCommand += 1;// new TextToSpeech("Comando non riconosciuto. Ritenta.");
						return;
					}

				}

				else if (vocalCommand.toLowerCase().contains(commandTypeClose1.toLowerCase())
						|| vocalCommand.toLowerCase().contains(commandTypeClose2.toLowerCase())) {
					printHelpCommand = false;
					contWrongCommand = 0;
					if (objectToActivate.equals("luce") || objectToActivate.equals("lampada")) {
						if (checkRoom().equals("mainRoom")) {
							command.lightOff();
						} else {
							wrongCommandLocation("spegnere la luce");
							wcl = false;
							inputManager.doCommand = false;

						}
						return;
					} else if (objectToActivate.equals("tv") || objectToActivate.equals("televisione")) {
						if (checkRoom().equals("mainRoom")) {
							command.tvOff();
						} else {
							wrongCommandLocation("spegnere la televisione");
							wcl = false;
							inputManager.doCommand = false;

						}
						return;
					} else if (objectToActivate.equals("cassaforte")) {
						if (checkRoom().equals("bathroom")) {
							// CHIUSURA CASSAFORTE
							command.safeBoxClose();
							return;
						} else {
							wrongCommandLocation("chiudere la cassaforte");
							wcl = false;
							inputManager.doCommand = false;

						}
						return;

					} else if (objectToActivate.equals("stereo") || objectToActivate.equals("radio")) {
						if (checkRoom().equals("mainRoom")) {
							command.speakerOff();
						} else {
							wrongCommandLocation("spegnere lo stereo");
							wcl = false;
							inputManager.doCommand = false;

						}

						return;
					} else {
						return;
					}
				}

				else if (vocalCommand.toLowerCase().equals(helpCommand.toLowerCase())) {
					contWrongCommand = 0;
					Utils.resp = "";

					command.help();

					return;

				} else if (vocalCommand.toLowerCase().contains(commandTypeRaise.toLowerCase())) {
					printHelpCommand = false;
					contWrongCommand = 0;
					if (objectToActivate.equals("volume")) {
						if (checkRoom().equals("mainRoom") && inputManager.activateSpeaker) {
							return;
						} else {
							Utils.resp = "";
							wcl = false;
							inputManager.doCommand = false;
							return;
						}

					}

					return;

				} else if (vocalCommand.toLowerCase().contains(commadExit.toLowerCase())) {
					printHelpCommand = false;
					contWrongCommand = 0;
					if (objectToActivate.equals("casa")) {
						if (checkRoom().equals("mainRoom")) {
							Utils.logout();
							command.openDoorA();
							Utils.doorIsOpen= true;
							Utils.resp = "";
							inputManager.doCommand = false;
							return;
						} else {
							wrongCommandLocation("uscire dalla casa da questa stanza");
							Utils.resp = "";
							wcl = false;
							inputManager.doCommand = false;
							return;
						}

					}
					return;

				} else {
					// se sbaglio il comando 3 volte parte il comando di aiuto
					contWrongCommand += 1;
					if (contWrongCommand == 4) {
						printHelpCommand = true;
						contWrongCommand = 0;
						Utils.resp = "";
						command.help();
						return;

					}

				}

				Utils.resp = "";
				inputManager.doCommand = false;
			} catch (Exception e) {
				ExceptionsManager.getExceptionsManager().manageException(e, skin, stage);
			}
		}
	}

	public boolean wcl = false;

	public void wrongCommandLocation(String commandType) {
		if (!wcl) {
			new TextToSpeech("Ti trovi nella stanza sbagliata, non puoi " + commandType);
			wcl = true;
			Utils.resp = "";
		}
	}

	public boolean hc = false;

	public void helpCommand() {
		try {
			if (inputManager.help) {
				if (hc == false) {
					if (checkRoom().equals("mainRoom")) {
						showAvailableCommandsRoomA();

					} else if (checkRoom().equals("bathroom")) {
						showAvailableCommandsRoomB();

					}
					hc = true;
					inputManager.help = false;
				}

			}

		} catch (Exception e) {
			ExceptionsManager.getExceptionsManager().manageException(e, skin, stage);
		}
	}

	public void showMessages() {
		if (showWait) {
			vocalMessageTable.add(waitMessage);
			vocalMessageTable.row();
		}

		if (inputManager.doCommand)
			executeCommand();

		if (checkRoom().equals("bathroom")) {
			messagesTable.add(bathRoomMessage);
			messagesTable.row();
		}
		if (checkRoom().equals("mainRoom")) {
			messagesTable.add(mainRoomMessage);
			messagesTable.row();
		}
		if (inputManager.isTvOn) {
			messagesTable.add(tvMessage);
			messagesTable.row();
		}
		if (inputManager.isLightOn) {
			messagesTable.add(lightMessage);
			messagesTable.row();
		}

		if (inputManager.safeBox) {
			messagesTable.add(safeBox);
			messagesTable.row();
		}

		if (inputManager.activateSpeaker) {
			messagesTable.add(speakerMessage);
			messagesTable.row();
		}
		if (printHelpCommand) {
			messagesTable.add(helpMessage);
			messagesTable.row();
		}

		stage.addActor(messagesTable);
		stage.addActor(vocalMessageTable);
		stage.act();

		stage.draw();
		messagesTable.clear();
		vocalMessageTable.clear();
	}

	public void drawMic() {
		stage.addActor(micImage);
		stage.act();
		stage.draw();
		stage.clear();
	}

	public void startTV() {
		try {
			if (inputManager.isTvOn) {
				decalBatch.add(tvScreen);
				decalBatch.flush();
			}
		} catch (Exception e) {
			ExceptionsManager.getExceptionsManager().manageException(e, skin, stage);
		}
	}

	public void turnLights() {
		try {
			if (inputManager.isLightOn) {
				decalBatch.add(light);
				light.lookAt(camera.position, camera.up);
				decalBatch.flush();

			}
		} catch (Exception e) {
			ExceptionsManager.getExceptionsManager().manageException(e, skin, stage);
		}
	}

	public boolean primo = true;

	public void startSpeakers() {

		try {
			if (inputManager.activateSpeaker) {
				if (primo) {
					music = new Music();
					primo = false;
					Utils.songPlay = true;
				}
			} else {
				if (Utils.songPlay) {
					music.close();
					primo = true;
				}
			}
		} catch (Exception e) {
			ExceptionsManager.getExceptionsManager().manageException(e, skin, stage);
		}

	}

	// variabile per quando entra in casa
	public boolean entrata = true;

	public void showAvailableCommandsRoomA() {

		String nome = user.getNickName();
		if (entrata) {
			if(nome.contains("ricca") || nome.contains("Ricca") || nome.contains("cuteri") || nome.contains("Cuteri") ||
					nome.contains("bernardo") || nome.contains("Bernardo")) {
				nome = "Professore " + nome;
			}
			
			new TextToSpeech("Benvenuto " + nome
					+ ", in questa stanza potrai interagire con: la televisione, lo stereo, la luce o impartire il comando di aiuto");
			entrata = false;
		} else {

			new TextToSpeech("In questa stanza potrai interagire con: la televisione, lo stereo, la luce o impartire il comando di aiuto");
		}

	}

	public void showAvailableCommandsRoomB() {

		new TextToSpeech("In questa stanza potrai interagire con la cassaforte e il ventilatore");

	}

	public static String checkRoom() {
		if (bathRoom.contains(player))
			return new String("bathroom");

		if (mainRoom1.contains(player))
			return new String("mainRoom");

		if (mainRoom2.contains(player))
			return new String("mainRoom");

		return "";
	}

	public void walk(float timeElapsed) {

		float speed = inputManager.movementSpeed;
		if ((inputManager.forward | inputManager.back) & (inputManager.right | inputManager.left)) {

			speed /= Math.sqrt(2);
		}

		if (inputManager.forward) {

			Vector3 v = camera.direction.cpy();
			v.y = 0f;
			v.x *= speed * timeElapsed;
			v.z *= speed * timeElapsed;

			GameEntity playerTemp = new GameEntity(camera.position.x, camera.position.z, 5f, 5f);
			playerTemp.updatePosition(camera.position.x, camera.position.z);
			if (Utils.doorIsOpen) {
				camera.translate(v);
				camera.update();
				player.updatePosition(camera.position.x, camera.position.z);
			} else {
				if (this.collidingWho != ' ' && this.collidingWho != 'f') {
					Vector3 v1 = camera.direction.cpy();
					v1.y = 0f;
					v1.x *= (speed * timeElapsed) * 2;
					v1.z *= (speed * timeElapsed) * 2;

					camera.translate(v1);
					camera.update();
					this.collidingWho = ' ';
				}

				if (!playerIsColliding(playerTemp, 'f')) {

					camera.translate(v);
					camera.update();
					player.updatePosition(camera.position.x, camera.position.z);
				}
			}

		}

		if (inputManager.back) {
			Vector3 v = camera.direction.cpy();
			v.y = 0f;
			v.x = -v.x;
			v.z = -v.z;
			v.x *= (speed * timeElapsed);
			v.z *= (speed * timeElapsed);

			if (Utils.doorIsOpen) {
				camera.translate(v);
				camera.update();
				player.updatePosition(camera.position.x, camera.position.z);
			} else {
				GameEntity playerTemp = new GameEntity(camera.position.x, camera.position.z, 5f, 5f);
				if (this.collidingWho != 'b') {
					camera.translate(v);
					camera.update();
				}
				playerTemp.updatePosition(camera.position.x, camera.position.z);

				if (!playerIsColliding(playerTemp, 'b')) {
					if (this.collidingWho == 'b') {
						camera.translate(v);
						camera.update();
						this.collidingWho = ' ';
					}

					player.updatePosition(camera.position.x, camera.position.z);
				}
			}
		}
		if (inputManager.left) {
			Vector3 v = camera.direction.cpy();
			v.y = 0f;
			v.rotate(Vector3.Y, 90);
			v.x *= speed * timeElapsed;
			v.z *= speed * timeElapsed;

			if (Utils.doorIsOpen) {
				camera.translate(v);
				camera.update();
				player.updatePosition(camera.position.x, camera.position.z);
			} else {
				GameEntity playerTemp = new GameEntity(camera.position.x, camera.position.z, 5f, 5f);
				playerTemp.updatePosition(camera.position.x, camera.position.z);

				if (this.collidingWho != ' ' && this.collidingWho != 'l') {
					Vector3 v1 = camera.direction.cpy();
					v1.y = 0f;
					v1.rotate(Vector3.Y, 90);
					v1.x *= (speed * timeElapsed) * 2;
					v1.z *= (speed * timeElapsed) * 2;

					camera.translate(v1);
					camera.update();
					this.collidingWho = ' ';
				}

				if (!playerIsColliding(playerTemp, 'l')) {
					camera.translate(v);
					camera.update();
					player.updatePosition(camera.position.x, camera.position.z);

				}
			}

		}
		if (inputManager.right) {

			Vector3 v = camera.direction.cpy();
			v.y = 0f;
			v.rotate(Vector3.Y, -90);
			v.x *= speed * timeElapsed;
			v.z *= speed * timeElapsed;

			if (Utils.doorIsOpen) {
				camera.translate(v);
				camera.update();
				player.updatePosition(camera.position.x, camera.position.z);
			} else {
				GameEntity playerTemp = new GameEntity(camera.position.x, camera.position.z, 5f, 5f);

				if (this.collidingWho != ' ' & this.collidingWho != 'r') {
					camera.translate(v);
					camera.update();
				}
				playerTemp.updatePosition(camera.position.x, camera.position.z);

				if (!playerIsColliding(playerTemp, 'r')) {
					camera.translate(v);
					camera.update();
					player.updatePosition(camera.position.x, camera.position.z);

				}

			}
		}
	}

	public boolean playerIsColliding(GameEntity playerTemp, char col) {

		for (GameEntity w : walls) {
			if (playerTemp.isColliding(w)) {
				this.collidingWho = col;
				return true;
			}

		}
		return false;

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

	public int cont = 0;
	public int cont1 = 0;

	public static Vector3 playerPosition;

	@Override
	public void render(float delta) {
		try {
			// You've seen all this before, just be sure to clear the GL_DEPTH_BUFFER_BIT
			// when working in 3D
			Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			Gdx.gl.glClearColor(1, 1, 1, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
			spriteBatch.setProjectionMatrix(camera.combined);

			playerPosition = player.getPosition();

			checkRoom();

			if (checkRoom().equals("mainRoom")) {
				if (cont == 0) {
					showAvailableCommandsRoomA();
					cont += 1;
					cont1 = 0;
				}
			}
			if (checkRoom().equals("bathroom")) {
				if (cont1 == 0) {
					showAvailableCommandsRoomB();
					cont1 += 1;
					cont = 0;
				}

			}

			walk(Gdx.graphics.getDeltaTime());

			entranceDoorAnimationController.update(Gdx.graphics.getDeltaTime());
			bathDoorAnimationController.update(Gdx.graphics.getDeltaTime());
			fanAnimationController.update(Gdx.graphics.getDeltaTime());
			modelBatch.begin(camera);

			modelBatch.render(entranceDoorInstance, environment);
			modelBatch.render(bathDoorInstance, environment);
			modelBatch.render(lampInstance, environment);
			modelBatch.render(tvInstance, environment);
			modelBatch.render(speakerInstance, environment);
			modelBatch.render(speaker2Instance, environment);
			modelBatch.render(safeInstance, environment);
			modelBatch.render(fanInstance, environment);

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

			if (inputManager.isSpeaking)
				drawMic();

			OpenDoorA();
			OpenDoorB();
			startTV();
			turnLights();
			startSpeakers();
			activateFan();

			showMessages();

			helpCommand();

			if (inputManager.nAccessButton) {
				inputManager.nAccessButton = false;
				Utils.positionVector = camera.position;
				if (Controller.getController().getUserDAO().isFirstRegistrationForThisForniture(Utils.ID_SUPPLY,
						Utils.ID_ADMIN_USER)) {
					System.out.println("SONO QUI");
					Utils.isFirstAccess = true;
					ScreenManager.getInstance().showScreen(new LoginScreenCreator());
				} else {
					// command.goToMainMenuScreen();
					if (Utils.userLogged.equals("-1") & checkRoom().equals(""))
						ScreenManager.getInstance().showScreen(new MainMenuScreenCreator());
					else if (checkRoom().equals("bathroom") || checkRoom().equals("mainRoom"))
						command.goToMainMenuScreen();
				}

			}

			// Use this to change Screen
			if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
				Gdx.app.exit();
			}
		} catch (Exception e) {
			ExceptionsManager.getExceptionsManager().manageException(e, skin, stage);
		}
	}

	@Override
	public void dispose() {
		this.modelBatch.dispose();
		this.entranceDoorModel.dispose();
		this.lampModel.dispose();
		this.tvModel.dispose();
		this.speakerModel.dispose();
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public AbstractCommand getCommand() {
		return command;
	}

	public void setCommand(AbstractCommand command) {
		this.command = command;
	}

}
