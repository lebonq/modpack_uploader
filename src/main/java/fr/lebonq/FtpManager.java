package fr.lebonq;

import java.io.File;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.ChannelSftp.LsEntry;

public class FtpManager {
	/**
	 * Permet de mettre en lgine un fichier
	 * @param pHost
	 * @param pUsername
	 * @param pPassword
	 * @param pPort
	 * @param pLocalfile
	 * @param pRemotePath ATTENTION Toujours le finir par /
	 */
	public static void upload(String pHost, String pUsername, String pPassword, int pPort, File pLocalfile, String pRemotePath){
		JSch vJsch = new JSch();
		Session vSession = null;
		try {
			vSession = vJsch.getSession(pUsername, pHost, pPort);
			vSession.setConfig("StrictHostKeyChecking", "no");
			vSession.setPassword(pPassword);
			vSession.connect();

			Channel vChannel = vSession.openChannel("sftp");
			vChannel.connect();
			ChannelSftp vSftpChannel = (ChannelSftp) vChannel;

			if(!(pRemotePath == "")){
				try {
					vSftpChannel.lstat(pRemotePath);//On essaye de lister dans le dossier
					System.out.println(pRemotePath + " exists");
				} catch (SftpException e) {//Si erreur c'est quil n'existe pas donc on le cree
					System.out.println(pRemotePath+" not found");
					System.out.println("Creating dir "+pRemotePath);
					vSftpChannel.mkdir(pRemotePath);
				}
			}

			vSftpChannel.put(pLocalfile.getAbsolutePath(), pRemotePath + pLocalfile.getName());
			vSftpChannel.exit();

			vSession.disconnect();
		 } catch (JSchException e) {
			e.printStackTrace();  
		 } catch (SftpException e) {
			e.printStackTrace();
		 }
	}

	/**
	 * Permet de supprimer un repertoire distant
	 * @param pHost
	 * @param pUsername
	 * @param pPassword
	 * @param pPort
	 * @param pRemoteDir ATTENTION Toujours le finir par /
	 */
	public static void deleteDir(String pHost, String pUsername, String pPassword, int pPort, String pRemoteDir){
		JSch vJsch = new JSch();
		Session vSession = null;
		try {
			vSession = vJsch.getSession(pUsername, pHost, pPort);
			vSession.setConfig("StrictHostKeyChecking", "no");
			vSession.setPassword(pPassword);
			vSession.connect();
	
			Channel vChannel = vSession.openChannel("sftp");
			vChannel.connect();
			ChannelSftp vSftpChannel = (ChannelSftp) vChannel;

			Vector<LsEntry> vList = vSftpChannel.ls(pRemoteDir);//On recupere la liste des fichiers dans le dossier
			for(int i = 2; i< vList.size();i++){//On commence a 2 pour ne pas avoir les entry . et ..
				LsEntry vFile = vList.get(i);
				System.out.println("Suppresion de " + pRemoteDir + vFile.getFilename());
				vSftpChannel.rm(pRemoteDir + vFile.getFilename());
			}

			vSftpChannel.rmdir(pRemoteDir);
			vSftpChannel.exit();

			vSession.disconnect();
		} catch (JSchException e) {
			e.printStackTrace();  
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}
}
