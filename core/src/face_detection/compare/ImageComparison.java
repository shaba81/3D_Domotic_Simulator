package face_detection.compare;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Inferiore al 10% va bene... fin'ora. Stesso posto. stessa luce, stessa
 * telecamera. Stesso tutto
 * 
 * Il main da lanciare � quello della classe 'FaceDetection' del package
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
	private int cont;

	public ImageComparison() {
		// TODO Auto-generated constructor stub
		this.users_faces_files = new HashMap<Long, File>();
		this.users_faces_images = new HashMap<Long, Image>();
		this.cont = 8;
		this.soloPERORA();
	}

	/**
	 * Questa funzione servir� fino a quando non avremo completato il login. Viene
	 * richiamata solo nel costruttore e prende tutte le immagini della cartella
	 * 'images' e le salva nelle rispettive 2 hashmap.
	 */
	private void soloPERORA() {
		try {
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Compare l'immagine appena catturata con tutte le immagini degli utentei gi�
	 * registrati. Ritorna true se l'immagine non � gi� presente; false, altrimenti.
	 * 
	 * @param imageToCompare
	 * @return
	 */
	private boolean isAnewUser(Image imageToCompare, String path) {
		for (Long key : this.users_faces_images.keySet()) {
			// se le due immagini sono le stesse, ritorno false, perch� significa che non �
			// un nuovo utente
			if (this.compareTwoImages(this.users_faces_images.get(key), imageToCompare,key, path))
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

		System.out.println("prima wa: "+imgA.getWidth(null)+" wb: "+imgB.getWidth(null));
		
		Image im = this.resize((BufferedImage)imgA, 300, 300);
		imgA = im;
		Image im2 = this.resize((BufferedImage)imgB, 300, 300);
		imgB = im2;
		System.out.println("dopo wa: "+imgA.getWidth(null)+" wb: "+imgB.getWidth(null));
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
		System.out.println("Difference Percentage-->  "+percentage);
//		System.out.println("keyimA: "+key+", path imgA: "+this.users_faces_files.get(key).getPath()+", path imgB: "+pathB+", ");
		if (percentage < 9.1) {
			System.out.println("I due volti SONO gli stessi.");
			return true;
		}

		System.err.println("I due volti NON SONO gli stessi.");
		return false;

	}

	/**
	 * Cancella un file da una cartella
	 * 
	 * @param folder_path
	 *            -> path della cartella
	 * @param image_path
	 *            -> path del file da eliminare
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
	 * detection e tutte le immagini degli utenti gi� registrati: se l'immagine �
	 * gi� registrata, l'utente pu� accedere, altrimenti no.
	 */
	public void compare() {
		try {
			String base = "resources/temp_image/";
			File fileA = new File(base + "temp.jpg");

			Image im = ImageIO.read(fileA);
			System.out.println("prima w: "+im.getWidth(null)+" h: "+im.getHeight(null));
			
			System.out.println("dopo w: "+im.getWidth(null)+" h: "+im.getHeight(null));

			if (!this.isAnewUser(im,fileA.getPath()))
				System.out.println("ACCESSO PERMESSO");
			else
				System.out.println("ACCESSO NEGATO");

			// ELIMINO IL FILE DALLA CARTELLA
			this.deleteAnElementInAfolder("resources/temp_image", "temp.jpg");
			// this.moveNewUserToImageFolder();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void register() {
		try {
			String base = "resources/temp_image";
			String base_image = "temp.jpg";
			File fileA = new File(base +"/"+base_image);

			Image im = ImageIO.read(fileA);

			if (this.isAnewUser(im,fileA.getPath()))
			{
				System.out.println("REGISTRAZIONE AVVENUTA CON SUCCESSO!");
				this.moveNewUserToImageFolder(base,base_image,"resources/images","a"+this.cont+".jpg");
				this.deleteAnElementInAfolder("resources/images", "a"+this.cont+".jpg");
				this.cont++;
			}
			else
				System.out.println("REGISTRAZIONE NON RIUSCITA");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	///////////////////////// NON
	///////////////////////// USATE//////////////////////////////////////////////////////////////
	/**
	 * E' una funzione che viene richiamata ogni qual volta un utente vuole accedere
	 * alla casa.
	 * 
	 * @param user_email
	 * @param file_user_image_path
	 */
	private void addUserImage(long user_id, String file_user_image_path) {
		try {
			String base = "resources/images/";
			File file = new File(base + file_user_image_path);
			Image user_image = ImageIO.read(file);
			this.users_faces_files.put(user_id, file);
			this.users_faces_images.put(user_id, user_image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Sposta il file dalla cartella 'tmep_image' alla 'images'
	 * 
	 * @param file
	 */
	private void moveNewUserToImageFolder(String fromfolder_path, String fromimage_path, String tofolder_path, String toimage_path) {
		// aggiungo il file nella cartella 'images'
          System.out.println("fromfolder_path: "+fromfolder_path+" fromimage_path: "+fromimage_path+" tofolder_path: "+tofolder_path+" toimage_path: "+toimage_path);
		try {
			Files.move(Paths.get(fromfolder_path+"/"+fromimage_path), Paths.get(tofolder_path+"/"+toimage_path));
			// this.cont++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public static void main(String[] args) {
	//
	// ImageComparison imageComparision = new ImageComparison();
	// imageComparision.compare();
	//
	// }

}
