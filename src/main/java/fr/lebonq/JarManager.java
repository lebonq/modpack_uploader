package fr.lebonq;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Permet de faire des operations sur les fichiers .jar necessaire pour notre liste de mods
 */
public class JarManager {

	/**
	 *  Methode qui permet d'extraire le fichier JSON des mods fabrics
	 * @param pFile l'objet File de notre fichier .jar
	 * @return l'objet File de notre fichier fabric.mod.json
	 */
	public static File extractJson(File pFile){
		JarFile vJarFile = null;
		File vJsonFile = null;
		try {
			vJarFile = new JarFile(pFile);
			try {
				String vName = "fabric.mod.json";
				//On récupère l'objet ZipEntry correspondant
				ZipEntry vEntry = vJarFile.getEntry(vName);
				// On crée un File représentant le fichier  :
				
				vJsonFile = new File("mods_json/" + pFile.getName() + ".json"); //On cree un fichier temporaire
				// On récupère l'InputStream du fichier à l'intérieur du ZIP/JAR
				InputStream vInput = vJarFile.getInputStream(vEntry);
				try {
					// On crée l'OutputStream vers la sortie
					OutputStream vOutput = new FileOutputStream(vJsonFile);
					try {
					   // On utilise une lecture bufférisé
						byte[] vBuf = new byte[4096];
						int vLen;
						while ( (vLen=vInput.read(vBuf)) > 0 ) {
							vOutput.write(vBuf, 0, vLen);
						}
					} finally {
							// Fermeture du fichier de sortie
							vOutput.close();
						}
				} finally {
					// Fermeture de l'inputStream en entrée
					vInput.close();
				}
			} finally {
				// Fermeture du JarFile
				vJarFile.close();
			}
		} catch (NullPointerException pE) {
			System.out.println("Fichier " + pFile.getName() + " non reconnu");
			return pFile;//Si erreur car Optifine ou pas de fichier Json dans le mod on retourne le fichier lui meme
		}catch(Exception pE){
			pE.printStackTrace();
			return null;
		}
		return vJsonFile;
	}
}
