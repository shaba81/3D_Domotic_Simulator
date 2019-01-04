package face_detection.take_snapshot;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class CropImage {
	private static Mat cropImage;

	public static void main(String[] args) throws Exception {
		int x = 0, y = 0, height = 0, width = 0;
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		MatOfRect faces = new MatOfRect();

		System.out.println("\nRunning FaceDetector");

		CascadeClassifier faceDetector = new CascadeClassifier();
		faceDetector.load("resources/haarcascades/haarcascade_frontalface_alt.xml");

		Mat image = Imgcodecs.imread("C:\\Users\\anton\\Desktop\\3.jpg");

		faceDetector.detectMultiScale(image, faces);

		System.out.println(String.format("Detected %s faces", faces.toArray().length));
		Rect rect_Crop = null;
		for (Rect rect : faces.toArray()) {
			//if (rect.width >= 400 && rect.height >= 400) {
				Imgproc.rectangle(image, new Point(rect.x, rect.y),
						new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));

				rect_Crop = new Rect(rect.x, rect.y, rect.width, rect.height);
			//}
		}

		Mat image_roi = new Mat(image, rect_Crop);
		String imagePath = "C:\\Users\\anton\\Desktop\\crop8.jpg";
		Imgcodecs.imwrite(imagePath, image_roi);

	}

}