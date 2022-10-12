package com.ftpconnection.ftpdemo.download;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DownloadMultipleFilesTest {

    @Test
    void downloadMultipleFiles() throws IOException {
        new DownloadMultipleFiles().downloadMultipleFiles();
    }
}