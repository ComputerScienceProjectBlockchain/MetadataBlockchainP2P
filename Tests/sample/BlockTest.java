package sample;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BlockTest {

    String path = "C:\\Users\\WinSa\\OneDrive\\Dokumenter\\RUC\\Fifth semester\\CS project\\MetadataBlockchainP2P\\TEST.docx";

    @Test
    void getPreviousHash() throws IOException {
        Block block = new Block(new Metadata(path),"0","Tester");
        assertEquals("0",block.getPreviousHash());
    }

    @Test
    void getHeight() throws IOException {
        Block block = new Block(new Metadata(path),"0","Tester");
        assertEquals(0,block.getHeight());
    }

    @Test
    void getFileTitle() throws IOException {
        Block block = new Block(new Metadata(path),"0","Tester");
        assertEquals("TEST.docx",block.getFileTitle());
    }

    @Test
    void getFileCreatedTime() throws IOException {
        Block block = new Block(new Metadata(path),"0","Tester");
        assertEquals("2020-12-11T13:36:39.0173889Z",block.getFileCreatedTime());
    }

    @Test
    void getFileAccessedTime() throws IOException {
        Block block = new Block(new Metadata(path),"0","Tester");
        assertEquals("2020-12-11T13:36:55.6135743Z",block.getFileAccessedTime());
    }

    @Test
    void getFileModifiedTime() throws IOException {
        Block block = new Block(new Metadata(path),"0","Tester");
        assertEquals("2020-12-11T13:36:45Z",block.getFileModifiedTime());
    }

    @Test
    void getUserName() throws IOException {
        Block block = new Block(new Metadata(path),"0","Tester");
        assertEquals("Tester",block.getUserName());
    }
}