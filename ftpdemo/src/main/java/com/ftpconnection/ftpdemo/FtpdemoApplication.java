package com.ftpconnection.ftpdemo;

import com.ftpconnection.ftpdemo.utilities.ConnectToFTPServer;
import com.ftpconnection.ftpdemo.utilities.DownloadFTPFilesAndDirectories;
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

//        https://stackoverflow.com/questions/39922534/ftpsclient-listfiles-not-working-for-nonstop-tandem-system
//		https://stackoverflow.com/questions/36302985/how-to-connect-to-ftp-over-tls-ssl-ftps-server-in-java
//		https://www.youtube.com/watch?v=8w-vilHEoj4
//		https://www.baeldung.com/java-ftp-client
//		https://riptutorial.com/java/example/18536/connecting-and-logging-into-a-ftp-server#:~:text=To%20start%20using%20FTP%20with,String%20username%2C%20String%20password)%20.
        FTPSClient client = ConnectToFTPServer.gimmeFactory();
        try {
            // Get Current Working Directory
//            client.printWorkingDirectory();
//            ConnectToFTPServer.showServerReply(client);

            // This will List the files
            FTPFile[] allFilesAndDirectories = client.listFiles();

            DownloadFTPFilesAndDirectories.printFileAndDirectoryDetails(allFilesAndDirectories);
            DownloadFTPFilesAndDirectories.downloadDirectory(client, "/", "/", "ftpdemo/src/main/resources/FTP");
            ConnectToFTPServer.showServerReply(client);
        } catch (IOException ex) {
            log.error("Connection issue with FTP server");
            ex.printStackTrace();
        } finally {
            try {
                if (client.isConnected()) {
                    client.logout();
                    ConnectToFTPServer.showServerReply(client);
                    client.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }


//        SpringApplication.run(FtpdemoApplication.class, args);
    }

}
