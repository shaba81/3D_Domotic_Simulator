package controller.face;

import java.awt.Image;
import java.awt.image.BufferedImage;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 * 
 * @author anton
 *
 */
public class FaceController {
	


	public void crop(MatOfRect faces, Mat image, String cropImagePath) {
		int x = 0, y = 0, height = 0, width = 0;

		System.out.println(String.format("Detected %s faces", faces.toArray().length));
		Rect rect_Crop = null;
		for (Rect rect : faces.toArray()) {
				Imgproc.rectangle(image, new Point(rect.x, rect.y),
						new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));

				rect_Crop = new Rect(rect.x, rect.y, rect.width, rect.height);
		}

		Mat image_roi = new Mat(image, rect_Crop);
		Imgcodecs.imwrite(cropImagePath, image_roi);

	}

	public void compare(Image imgA, Image imgB) {

		/**
		 * TODO SCALE of image
		 */
		int width1 = imgA.getWidth(null);
		int width2 = imgB.getWidth(null);
		int height1 = imgA.getHeight(null);
		int height2 = imgB.getHeight(null);

		if ((width1 != width2) || (height1 != height2))
			System.out.println("Error: Images dimensions" + " mismatch");
		else {
			long difference = 0;
			for (int y = 0; y < height1; y++) {
				for (int x = 0; x < width1; x++) {
					int rgbA = ((BufferedImage) imgA).getRGB(x, y);
					int rgbB = ((BufferedImage) imgB).getRGB(x, y);
					int redA = (rgbA >> 16) & 0xff;
					int greenA = (rgbA >> 8) & 0xff;
					int blueA = (rgbA) & 0xff;
					int redB = (rgbB >> 16) & 0xff;
					int greenB = (rgbB >> 8) & 0xff;
					int blueB = (rgbB) & 0xff;
					difference += Math.abs(redA - redB);
					difference += Math.abs(greenA - greenB);
					difference += Math.abs(blueA - blueB);
				}
			}

			// Total number of red pixels = width * height
			// Total number of blue pixels = width * height
			// Total number of green pixels = width * height
			// So total number of pixels = width * height * 3
			double total_pixels = width1 * height1 * 3;

			// Normalizing the value of different pixels
			// for accuracy(average pixels per color
			// component)
			double avg_different_pixels = difference / total_pixels;

			// There are 255 values of pixels in total
			double percentage = (avg_different_pixels / 255) * 100;

			if( percentage < 6.1 )
				System.out.println("I due volti SONO gli stessi.");
			else 
				System.err.println("I due volti NON SONO gli stessi.");
			System.out.println("Difference Percentage-->" + percentage);
		}
}

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

}
