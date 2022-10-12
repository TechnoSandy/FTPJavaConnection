package com.ftpconnection.ftpdemo.utilities;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;

import java.io.IOException;

@Slf4j
public class ConnectToFTPServer {

    static String server = "galuwuxy.files.com"; //Server can be either host name or IP address.
    static int port = 21;
    static String user = "galuwuxy@ema-sofia.eu";
    static String pass = "Asdfg@12345";


    public static FTPSClient gimmeFactory() throws IOException {
        FTPSClient ftp = new FTPSClient(false);
        ftp.connect(server, port);
        showServerReply(ftp);
        int replyCode = ftp.getReplyCode();
        log.info("Reply Code {}", replyCode);
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            log.error("Operation failed. Server reply code: " + replyCode);
        }
        boolean success = ftp.login(user, pass);
        showServerReply(ftp);
        if (!success) {
            log.info("Failed to log into the server");
        } else {
            log.info("LOGGED IN SERVER SUCCESSFULLY");
            // Set protection buffer size
            ftp.execPBSZ(0);
            // Set data channel protection to private
            ftp.execPROT("P");
            // Enter local passive mode
            ftp.enterLocalPassiveMode();

//            ftp.logout();
//            ftp.disconnect();
            // DOWNLOAD ALL FILES

        }
        return ftp;
    }

    public static void showServerReply(FTPClient ftp) {
        String[] replies = ftp.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                log.info("SERVER: " + aReply);
            }
        }
    }
}
