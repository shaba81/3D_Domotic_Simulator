package com.mygdx.controller.face;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import com.mygdx.controller.face.compare.ImageComparison;

public class FaceDetectionController {
	private boolean captured = false;
	private boolean green = false;

	// a timer for acquiring the video stream
	private ScheduledExecutorService timer;
	// the OpenCV object that performs the video capture
	private VideoCapture capture;
	// a flag to change the button behavior
	private boolean cameraActive;

	// face cascade classifier
	private CascadeClassifier faceCascade;
	private int absoluteFaceSize;

	private ImageComparison image_comparison;

	public FaceDetectionController() {
		// TODO Auto-generated constructor stub
		this.image_comparison = new ImageComparison();

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
					while (!captured) {
						try {
							// effectively grab and process a single frame
							Mat frame = grabFrame();

							// QUI BISOGNA SALVARE L'IMMAGINE SU FILE, CHE SARA POI PRESA DA GDX PER FARLA A
							// VIDEO
							if (!utilis.Utils.capturing) {
								BufferedImage buffImg = Utils.matToBufferedImage(frame);
								File outputfile = new File("resources/temp_image/temp.jpg");

								ImageIO.write(buffImg, "jpg", outputfile);
								utilis.Utils.capturing = true;

							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
			try {
				// read the current frame
				this.capture.read(frame);

				// if the frame is not empty, process it
				if (!frame.empty()) {
					// face detection
					this.detectAndDisplay(frame);
				}

			} catch (Exception e) {
				// log the (full) error
				System.err.println("Exception during the image elaboration: " + e);
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
		
		if(this.image_comparison.compare())
			return true;
		return false;
	}

	public boolean registerUser() {
		
		if(this.image_comparison.register())
			return true;
		return false;
	}

	private void captureAndCrop(MatOfRect faces, Mat frame, String path) {
		System.out.println("cia");
		this.green = true;
		this.captured = true;
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
				// log any exception
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
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
	protected void setClosed() {
		this.stopAcquisition();
	}

	public boolean isCaptured() {
		return captured;
	}

	public void setCaptured(boolean captured) {
		this.captured = captured;
	}
	
	

}
