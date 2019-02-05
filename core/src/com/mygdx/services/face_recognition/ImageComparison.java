package com.mygdx.services.face_recognition;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import com.mygdx.application.exception_manager.ExceptionsManager;
import com.mygdx.domain.controller.Controller;
import com.mygdx.foundation.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Inferiore al 10% va bene... fin'ora. Stesso posto. stessa luce, stessa
 * telecamera. Stesso tutto
 * 
 * Il main da lanciare è quello della classe 'FaceDetection' del package
 * 'controller.face'
 * 
 * @author anton
 *
 */

public class ImageComparison {

	// Key: UserEmail; Value:path of UserFaceImage
	private HashMap<Long, File> users_faces_files;
	// Key: UserEmail; Value: user image
	private HashMap<Long, Image> users_faces_images;

	public ImageComparison() {
		try {
			this.users_faces_files = new HashMap<Long, File>();
			this.users_faces_images = new HashMap<Long, Image>();
			this.soloPERORA();
		} catch (Exception e) {
			System.out.println("Wrong path.");
		}
	}

	/**
	 * Questa funzione servirà fino a quando non avremo completato il login. Viene
	 * richiamata solo nel costruttore e prende tutte le immagini della cartella
	 * 'images' e le salva nelle rispettive 2 hashmap.
	 * 
	 * @throws IOException
	 */
	private void soloPERORA() throws IOException {
		File folder = new File("resources/images");
		File[] listOfFiles = folder.listFiles();
		long cont = 1;

		for (File file : listOfFiles) {
			if (file.isFile()) {
				this.users_faces_files.put(cont, file);
				this.users_faces_images.put(cont, ImageIO.read(file));
			}
			cont++;
		}

	}

	/**
	 * Compare l'immagine appena catturata con tutte le immagini degli utentei già
	 * registrati. Ritorna true se l'immagine non è già presente; false, altrimenti.
	 * 
	 * @param imageToCompare
	 * @return
	 */
	private boolean isAnewUser(Image imageToCompare, String imageFilePath) {
		for (Long key : this.users_faces_images.keySet()) {
			// se le due immagini sono le stesse, ritorno false, perché significa che non è
			// un nuovo utente
			if (this.compareTwoImages(this.users_faces_images.get(key), imageToCompare, key, imageFilePath))
				return false;
		}
		return true;
	}

	/**
	 * Ritorna true se le immagini sono le stesse; false, altrimenti.
	 * 
	 * @param imgA
	 * @param imgB
	 * @return
	 */
	private boolean compareTwoImages(Image imgA, Image imgB, Long key, String pathB) {

		Image im = this.resize((BufferedImage) imgA, 300, 300);
		imgA = im;
		Image im2 = this.resize((BufferedImage) imgB, 300, 300);
		imgB = im2;
		int width1 = imgA.getWidth(null);
		int height1 = imgA.getHeight(null);

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

		if (percentage < 9.4) {
			Utils.pathImageUser = this.users_faces_files.get(key).getAbsolutePath();
			return true;
		}

		return false;

	}

	/**
	 * Cancella un file da una cartella
	 * 
	 * @param folder_path -> path della cartella
	 * @param image_path  -> path del file da eliminare
	 */
	private void deleteAnElementInAfolder(String folder_path, String image_path) {
		File file_to_delete = new File(folder_path + "/" + image_path);

		if (file_to_delete.exists())
			file_to_delete.delete();
	}

	private BufferedImage resize(BufferedImage img, int height, int width) {
		Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = resized.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		return resized;
	}

	/**
	 * Contiene tutto l'algoritmo della compare tra l'immagine appena fatta dalla
	 * detection e tutte le immagini degli utenti già registrati: se l'immagine è
	 * già registrata, l'utente può accedere, altrimenti no.
	 * 
	 * @throws IOException
	 */
	public boolean compare() throws IOException {
		String base = "resources/temp_image/";
		File fileA = new File(base + "temp.jpg");

		Image im = ImageIO.read(fileA);

		if (!this.isAnewUser(im, fileA.getPath()))
			return true;

		// ELIMINO IL FILE DALLA CARTELLA
		this.deleteAnElementInAfolder("resources/temp_image", "temp.jpg");
		return false;
	}

	public boolean register() throws IOException {
		String base = "resources/temp_image";
		String base_image = "temp.jpg";
		File fileA = new File(base + "/" + base_image);

		Image im = ImageIO.read(fileA);

		return this.isAnewUser(im, fileA.getPath());
	}

	/**
	 * E' una funzione che viene richiamata ogni qual volta un utente vuole accedere
	 * alla casa.
	 * 
	 * @param user_email
	 * @param file_user_image_path
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private void addUserImage(long user_id, String file_user_image_path) throws IOException {
		String base = "resources/images/";
		File file = new File(base + file_user_image_path);
		Image user_image = ImageIO.read(file);
		this.users_faces_files.put(user_id, file);
		this.users_faces_images.put(user_id, user_image);

	}

	/**
	 * Sposta il file dalla cartella 'tmep_image' alla 'images'
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void moveNewUserToImageFolder(String toimage_path) throws IOException {
		// aggiungo il file nella cartella 'images'
		Files.move(Paths.get("resources/temp_image" + "/" + "temp.jpg"), Paths.get(toimage_path));
		// this.cont++;

	}

}
