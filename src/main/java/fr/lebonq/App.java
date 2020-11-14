package fr.lebonq;

import java.io.File;

import org.aeonbits.owner.ConfigFactory;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {

        ConfigFtp vCfg = ConfigFactory.create(ConfigFtp.class);

        FilesManager vFilesManager = new FilesManager();
        File[] vJarFiles = null;
        try {
            vJarFiles = vFilesManager.listJar();// On liste tout les JARS
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        File vFolder = new File("mods_json");
        boolean vJsonFolder = vFolder.mkdir();// On cree le dossier mods_json
        if(!(vJsonFolder) && vFolder.exists()){//Si la creation du dossier a echoue et que le dossier existe alors on le vide de ses fichiers
            File[] vList = vFolder.listFiles();
            for(int i = 0; i < vList.length;i++){
                vList[i].delete();
            }
        }

        File[] vJsonFiles = new File[vJarFiles.length];
        for (int i = 0; i < vJarFiles.length; i++) {
            vJsonFiles[i] = JarManager.extractJson(vJarFiles[i]); // On extrait tout les .json
            System.out.println("" + (i + 1) + "/" + vJarFiles.length + " done");
        }

        try {
            FtpManager.deleteDir(vCfg.host(),vCfg.username(), vCfg.password(),vCfg.port(), vCfg.modsJsonRemoteDir());//On vide le dossier des JSONs
            FtpManager.deleteDir(vCfg.host(),vCfg.username(), vCfg.password(),vCfg.port(), vCfg.modsJarDir());//Pareil pour les jars

            for(int i = 0; i < vJarFiles.length;i++){//On upload le jar et son JSON correspondant
                FtpManager.upload(vCfg.host(),vCfg.username(), vCfg.password(),vCfg.port(),vJarFiles[i], vCfg.modsJarDir());
                FtpManager.upload(vCfg.host(),vCfg.username(), vCfg.password(),vCfg.port(),vJsonFiles[i], vCfg.modsJsonRemoteDir());
                System.out.println("Mise en ligne des fichiers pour le mod " + vJarFiles[i].getName() +"\n Merci de patienter.");
            }

            FtpManager.upload(vCfg.host(),vCfg.username(), vCfg.password(),vCfg.port(),new File("php/mods_json/index.php"), vCfg.modsJsonRemoteDir());//Et on push les deux codes php pour crer le XML
            FtpManager.upload(vCfg.host(),vCfg.username(), vCfg.password(),vCfg.port(),new File("php/modpack_client/index.php"), vCfg.modsJarDir());


        } catch (Exception e) {
            e.printStackTrace();
        } 


        System.out.println("Finish"); 
    }
}
