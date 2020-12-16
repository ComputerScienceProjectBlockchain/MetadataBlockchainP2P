package sample;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BlockTest {
    //When testing - please write a valid path from the testers computer
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
        assertEquals("11-12-2020 15:15:37",block.getFileCreatedTime());
    }

    @Test
    void getFileAccessedTime() throws IOException {
        Block block = new Block(new Metadata(path),"0","Tester");
        assertEquals("15-12-2020 22:59:35",block.getFileAccessedTime());
    }

    @Test
    void getFileModifiedTime() throws IOException {
        Block block = new Block(new Metadata(path),"0","Tester");
        assertEquals("11-12-2020 15:15:37",block.getFileModifiedTime());
    }

    @Test
    void getUserName() throws IOException {
        Block block = new Block(new Metadata(path),"0","Tester");
        assertEquals("Tester",block.getUserName());
    }
}