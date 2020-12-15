package sample;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MetadataTest {
    //When testing - please write a valid path from the testers computer
    String path = "C:\\Users\\WinSa\\OneDrive\\Dokumenter\\RUC\\Fifth semester\\CS project\\MetadataBlockchainP2P\\TEST.docx";
    @Test
    void getTitle() throws IOException {
        Metadata metadata = new Metadata(path);
        assertEquals("TEST.docx",metadata.getTitle());
    }

    @Test
    void getCreationTime() throws IOException {
        Metadata metadata = new Metadata(path);
        assertEquals("2020-12-11T15:15:37.3720663Z",metadata.getCreationTime());
    }

    @Test
    void getLastAccessTime() throws IOException {
        Metadata metadata = new Metadata(path);
        assertEquals("2020-12-11T15:15:42.6610821Z",metadata.getLastAccessTime());
    }

    @Test
    void getLastModifiedTime() throws IOException {
        Metadata metadata = new Metadata(path);
        assertEquals("2020-12-11T15:15:37.3720663Z",metadata.getLastModifiedTime());
    }
}