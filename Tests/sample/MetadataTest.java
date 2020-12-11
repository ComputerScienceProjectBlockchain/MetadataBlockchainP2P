package sample;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MetadataTest {

    String path = "C:\\Users\\WinSa\\OneDrive\\Dokumenter\\RUC\\Fifth semester\\CS project\\MetadataBlockchainP2P\\TEST.docx";
    @Test
    void getTitle() throws IOException {
        Metadata metadata = new Metadata(path);
        assertEquals("TEST.docx",metadata.title);
    }

    @Test
    void getCreationTime() throws IOException {
        Metadata metadata = new Metadata(path);
        assertEquals("2020-12-11T13:36:39.0173889Z",metadata.creationTime);
    }

    @Test
    void getLastAccessTime() throws IOException {
        Metadata metadata = new Metadata(path);
        assertEquals("2020-12-11T13:36:55.6135743Z",metadata.lastAccessTime);
    }

    @Test
    void getLastModifiedTime() throws IOException {
        Metadata metadata = new Metadata(path);
        assertEquals("2020-12-11T13:36:45Z",metadata.lastModifiedTime);
    }
}