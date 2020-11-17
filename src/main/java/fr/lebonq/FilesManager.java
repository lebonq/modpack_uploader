package fr.lebonq;

import java.io.File;
import java.io.IOException;

public class FilesManager {

    /**
     * Permet de savoir si le fichier est un .jar
     * 
     * @param pFile
     * @return boolean
     */
    public boolean isJar(File pFile) {
        try {
            return this.getExtension(pFile).equals("jar");
        } catch (Exception e) {
            System.out.println("C'est un dossier");
            return false;
        }
    }

    /**
     * Permet de connaitre l'extention d'un fichier
     * 
     * @param pFile exemple 'server.jar'
     * @return String exemple 'jar'
     */
    public String getExtension(File pFile) throws Exception {
        if (pFile.isDirectory()) {
            throw new IOException();
        }

        int vIndex = pFile.getName().lastIndexOf('.');
        String vExtension = null;
        if (vIndex > 0) {
            vExtension = pFile.getName().substring(vIndex + 1);
        }
        return vExtension;
    }

    /**
     * Permet de connaitre le nombre de fichier jar dans un dossier
     * 
     * @param pFile
     * @return
     */
    public int numberOfJar() throws Exception {
        File[] pFile = new File("modpack_client").listFiles();
        int res = 0;
        for (int i = 0; i < pFile.length; i++) {
            if (isJar(pFile[i])) {
                res++;
            }
        }
        return res;
    }

    /**
     * Creer la liste de tout les fichier jars du dossier mods
     * 
     * @return une liste de File
     * @throws Exception Si le dossier mods a un dossier
     */
    public File[] listJar() {
        File[] vJarFilesList = new File("modpack_client").listFiles(); // On met la liste de tout les fichiers du
                                                                       // dossier mods

        int vNumberOfMods;
        try {
            vNumberOfMods = this.numberOfJar();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        File[] vRerturnFiles = new File[vNumberOfMods];

        int j = 0;
        for(int i =0; i < vJarFilesList.length;i++){
            if(isJar(vJarFilesList[i])){
                vRerturnFiles[j] = vJarFilesList[i];//On stocke les fichiers .jar
                j++;
            }
        }
        return vRerturnFiles;
    }

}
