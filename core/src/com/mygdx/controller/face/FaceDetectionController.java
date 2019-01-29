package com.mygdx.controller.face;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import com.mygdx.controller.Controller;
import com.mygdx.controller.database.model.User;
import com.mygdx.controller.face.compare.ImageComparison;
import com.mygdx.controller.proxy.UserAdministratorCommand;
import com.mygdx.controller.proxy.UserCommand;
import com.mygdx.game.GameScreen;

import utilis.ExceptionsManager;
import utilis.Utils;

public class FaceDetectionController {
	// a timer for acquiring the video stream
	private ScheduledExecutorService timer;
	// the OpenCV object that performs the video capture
	private VideoCapture capture;
	// a flag to change the button behavior
	@SuppressWarnings("unused")
	private boolean cameraActive;

	// face cascade classifier
	private CascadeClassifier faceCascade;
	private int absoluteFaceSize;

	private ImageComparison image_comparison;

	public FaceDetectionController() {
		this.image_comparison = new ImageComparison();
	}

	/**
	 * 
	 * @param emailOneTime
	 * @throws Exception
	 */
	public void setUserAndCommandAccess(String parameter, boolean threeTimes) throws Exception {
		String idUser = "";
		User user;

		if (threeTimes) {
			user = Controller.getController().getUserDAO().getUserByEmail(parameter);
			idUser = Utils.getIdUserFromImage(user.getPathImage());
		} else {
			idUser = Utils.getIdUserFromImage(parameter);
			System.out.println("Id user: " + idUser);
			user = Controller.getController().getUserDAO().getUserByPathImage("resources/images/" + idUser + ".jpg");
		}
		user.setIdUser(idUser);

		Utils.userLogged = idUser;
		Utils.saveOnLog(Utils.ACCESS_SUCCESS_LOG);

		/*
		 * Proxy o non proxy decision
		 */
		GameScreen.getGameScreen().setUser(user);

		if (user.isAdministrator())
			GameScreen.getGameScreen().setCommand(new UserAdministratorCommand());
		else
			GameScreen.getGameScreen().setCommand(new UserCommand());
	}

	/**
	 * 
	 * @param pathImage
	 */
	public void setUserAndCommandRegistration(String pathImage) {
		String idUser = Utils.getIdUserFromImage(pathImage);
		User user = new User(Utils.credentials.get(0), Utils.credentials.get(2), Utils.credentials.get(1), pathImage,
				false);
		user.setIdUser(idUser);
		Utils.credentials.clear();
		GameScreen.getGameScreen().setUser(user);
		GameScreen.getGameScreen().setCommand(new UserCommand());
	}

	/**
	 * Init the controller, at start time
	 */
	public void init() {
		this.capture = new VideoCapture();
		this.faceCascade = new CascadeClassifier();
		this.absoluteFaceSize = 0;
		this.faceCascade.load("resources/haarcascades/haarcascade_frontalface_alt.xml");
		this.startCamera();
	}

	public void startCamera() {
		// start the video capture
		this.capture.open(0);

		// is the video stream available?
		if (this.capture.isOpened()) {
			this.cameraActive = true;

			// grab a frame every 33 ms (30 frames/sec)
			Runnable frameGrabber = new Runnable() {

				@Override
				public void run() {
					while (!Utils.captured) {
						// effectively grab and process a single frame
						try {
							Mat frame = grabFrame();
							// QUI BISOGNA SALVARE L'IMMAGINE SU FILE, CHE SARA POI PRESA DA GDX PER FARLA A
							// VIDEO
							if (!utilis.Utils.capturing) {
								BufferedImage buffImg = Utils.matToBufferedImage(frame);
								File outputfile = new File("resources/frame.jpg");

								Utils.isSave = false;

								ImageIO.write(buffImg, "jpg", outputfile);

								utilis.Utils.capturing = true;
								Utils.isSave = true;
							}
						} catch (IOException e) {
							//ExceptionsManager.getExceptionsManager().manageException(e);
						}
					}
				}
			};

			this.timer = Executors.newSingleThreadScheduledExecutor();
			this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

			// update the button content
		} else {
			// log the error
			System.err.println("Failed to open the camera connection...");
		}
	}

	/**
	 * Get a frame from the opened video stream (if any)
	 * 
	 * @return the {@link Image} to show
	 */
	private Mat grabFrame() {
		Mat frame = new Mat();

		// check if the capture is open
		if (this.capture.isOpened()) {
				// read the current frame
				this.capture.read(frame);

				// if the frame is not empty, process it
				if (!frame.empty()) {
					// face detection
					this.detectAndDisplay(frame);
				}
		}

		return frame;
	}

	/**
	 * Method for face detection and tracking
	 * 
	 * @param frame it looks for faces in this frame
	 */
	private void detectAndDisplay(Mat frame) {
		MatOfRect faces = new MatOfRect();
		Mat grayFrame = new Mat();

		// convert the frame in gray scale
		Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
		// equalize the frame histogram to improve the result
		Imgproc.equalizeHist(grayFrame, grayFrame);

		// compute minimum face size (20% of the frame height, in our case)
		if (this.absoluteFaceSize == 0) {
			int height = grayFrame.rows();
			if (Math.round(height * 0.2f) > 0) {
				this.absoluteFaceSize = Math.round(height * 0.2f);
			}
		}

		// detect faces
		this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
				new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());

		// each rectangle in faces is a face: draw them!
		Rect[] facesArray = faces.toArray();

		for (int i = 0; i < facesArray.length; i++) {

			if (facesArray[i].width >= 300 && facesArray[i].height >= 300) {
				Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);
				this.captureAndCrop(faces, frame, "resources/temp_image/temp.jpg");
				this.setClosed();
				// this.compare();
//				this.registerUser();
				break;
			}
			// else
			Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 0, 255), 3);
		}

	}

	public boolean compare() {

		if (this.image_comparison.compare())
			return true;

		Utils.countErrorTimes++;

		if (Utils.countErrorTimes == 3) {
			this.direAlSimulatoreDiMandareAllUtenteLemail();
			Utils.countErrorTimes = 0;
		}
		return false;
	}

	/**
	 * Sets true the 'TreeTimesAccessError' boolean to notify the Simulator to send
	 * to user an email to recover his housim access.
	 */
	private void direAlSimulatoreDiMandareAllUtenteLemail() {
		Utils.treeTimesAccessError = true;
	}

	public boolean registerUser() {

		if (this.image_comparison.register())
			return true;
		return false;
	}

	public void moveImages(String toimage_path) {
		this.image_comparison.moveNewUserToImageFolder(toimage_path);
	}

	private void captureAndCrop(MatOfRect faces, Mat frame, String path) {
		System.out.println("cia");
		Utils.captured = true;
		FaceController faceController = new FaceController();
		faceController.crop(faces, frame, path);
	}

	/**
	 * Stop the acquisition from the camera and release all the resources
	 */
	private void stopAcquisition() {
		if (this.timer != null && !this.timer.isShutdown()) {
			try {
				// stop the timer
				this.timer.shutdown();
				this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				//ExceptionsManager.getExceptionsManager().manageException(e);
			}
		}

		if (this.capture.isOpened()) {
			// release the camera
			this.capture.release();
		}
	}

	/**
	 * On application close, stop the acquisition from the camera
	 */
	public void setClosed() {
		this.stopAcquisition();
	}

//	public boolean isCaptured() {
//		return captured;
//	}
//
//	public void setCaptured(boolean captured) {
//		this.captured = captured;
//	}

}
