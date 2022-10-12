package com.ftpconnection.ftpdemo;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Slf4j
@SpringBootApplication
public class FtpdemoApplication {

    public static void main(String[] args) throws IOException {

//		https://stackoverflow.com/questions/36302985/how-to-connect-to-ftp-over-tls-ssl-ftps-server-in-java
//		https://www.youtube.com/watch?v=8w-vilHEoj4
//		https://www.baeldung.com/java-ftp-client
//		https://riptutorial.com/java/example/18536/connecting-and-logging-into-a-ftp-server#:~:text=To%20start%20using%20FTP%20with,String%20username%2C%20String%20password)%20.

        String server = "galuwuxy.files.com"; //Server can be either host name or IP address.
        int port = 21;
        String user = "galuwuxy@ema-sofia.eu";
        String pass = "Asdfg@12345";


//		String server = "ftp.dlptest.com"; //Server can be either host name or IP address.
//		int port = 21;
//		String user = "dlpuser";
//		String pass = "rNrKYTX9g7z3RgJRmxWuGHbeu";


        FTPSClient ftp = new FTPSClient();

        try {
            ftp.connect(server, port);
            showServerReply(ftp);
            int replyCode = ftp.getReplyCode();
            log.info("Reply Code {}", replyCode);
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                log.error("Operation failed. Server reply code: " + replyCode);
                return;
            }

            boolean success = ftp.login(user, pass);
            showServerReply(ftp);
            log.info(String.valueOf(success));
            if (!success) {
                log.info("Failed to log into the server");
                return;
            } else {
                log.info("LOGGED IN SERVER SUCCESSFULLY");
                // LIST ALL FILES AND FOLDER IN FTP SERVER
				// NEED TO WORK ON THIS LOGIC
//                String remoteDirPath = "/Test";
//                String saveDirPath = "C:/Test/FTP";

//                downloadDirectory(ftp, remoteDirPath, "", saveDirPath);

                ftp.logout();
                ftp.disconnect();
                // DOWNLOAD ALL FILES

            }
        } catch (Exception ex) {
            log.error("Oops! Something went wrong.");
            ex.printStackTrace();
        }


//        SpringApplication.run(FtpdemoApplication.class, args);
    }


    private static void showServerReply(FTPClient ftp) {
        String[] replies = ftp.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                log.info("SERVER: " + aReply);
            }
        }
    }

    public static void downloadDirectory(FTPClient ftpClient, String parentDir,
                                         String currentDir, String saveDir) throws IOException {
        String dirToList = parentDir;
        if (!currentDir.equals("")) {
            dirToList += "/" + currentDir;
        }

        FTPFile[] subFiles = ftpClient.listFiles(dirToList);

        if (subFiles != null && subFiles.length > 0) {
            for (FTPFile aFile : subFiles) {
                String currentFileName = aFile.getName();
                if (currentFileName.equals(".") || currentFileName.equals("..")) {
                    // skip parent directory and the directory itself
                    continue;
                }
                String filePath = parentDir + "/" + currentDir + "/"
                        + currentFileName;
                if (currentDir.equals("")) {
                    filePath = parentDir + "/" + currentFileName;
                }

                String newDirPath = saveDir + parentDir + File.separator
                        + currentDir + File.separator + currentFileName;
                if (currentDir.equals("")) {
                    newDirPath = saveDir + parentDir + File.separator
                            + currentFileName;
                }

                if (aFile.isDirectory()) {
                    // create the directory in saveDir
                    File newDir = new File(newDirPath);
                    boolean created = newDir.mkdirs();
                    if (created) {
                        System.out.println("CREATED the directory: " + newDirPath);
                    } else {
                        System.out.println("COULD NOT create the directory: " + newDirPath);
                    }

                    // download the sub directory
                    downloadDirectory(ftpClient, dirToList, currentFileName,
                            saveDir);
                } else {
                    // download the file
                    boolean success = downloadSingleFile(ftpClient, filePath,
                            newDirPath);
                    if (success) {
                        System.out.println("DOWNLOADED the file: " + filePath);
                    } else {
                        System.out.println("COULD NOT download the file: "
                                + filePath);
                    }
                }
            }
        }
    }

    public static boolean downloadSingleFile(FTPClient ftpClient,
                                             String remoteFilePath, String savePath) throws IOException {
        File downloadFile = new File(savePath);

        File parentDir = downloadFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdir();
        }

        OutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream(downloadFile));
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            return ftpClient.retrieveFile(remoteFilePath, outputStream);
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

}
