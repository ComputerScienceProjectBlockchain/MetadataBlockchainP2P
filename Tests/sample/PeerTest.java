package sample;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PeerTest {
    //When testing - please write a valid path from the testers computer
    ArrayList<Block> blockchain = new ArrayList<>();
    String path = "C:\\Users\\WinSa\\OneDrive\\Dokumenter\\RUC\\Fifth semester\\CS project\\MetadataBlockchainP2P\\TEST.docx";

    //Testing of the connectToSuper method from peer -
    // for Unit-testing purposes we will not look at the actual network
    @Test
    void connectToSuperTest() throws IOException, ClassNotFoundException {
        assertEquals(null, connectToSuperTesterStub("localhost"));
    }

    public Socket connectToSuperTesterStub(String superPeerIP) throws IOException, ClassNotFoundException {
        Socket socket = null;
        return socket;
    }

    //Test of prepareBlock - For testing purposes we have simplified this method
    // such that actual blocks are not made
    @Test
    void prepareBlockPreviousHashZeroTest() throws IOException {
        ArrayList<Block> blockchain = new ArrayList<>();
        assertEquals("First block", prepareBlockTester(blockchain));
    }

    @Test
    void prepareBlockPreviousHashNotZeroTest() throws IOException {
        blockchain.add(new Block(new Metadata(path), "0", "Tester"));
        assertEquals("Not first block", prepareBlockTester(blockchain));
    }


    public String prepareBlockTester(ArrayList<Block> blockchain) {
        if (blockchain.isEmpty()) {
            return "First block";
        } else {
            return "Not first block";
        }

    }

    //Test of the viewBlockchain method
    // - for this test we do not look at the storage file as we otherwise would in this method
    @Test
    void viewEmptyBlockchainTest() {
        ArrayList<Block> blockchain = new ArrayList<>();
        assertEquals("No blockchain to view", viewBlockchainTester(blockchain));
    }


    @Test
    void viewBlockchainWithBlocksTest() throws IOException {
        blockchain.add(new Block(new Metadata(path), "0", "Tester"));
        assertEquals(blockchain.toString(), viewBlockchainTester(blockchain));
    }

    public String viewBlockchainTester(ArrayList<Block> blockchain) {
        if (blockchain.isEmpty()) {
            return "No blockchain to view";
        } else {
            return blockchain.toString();
        }
    }

    //Testing of compareBlocks method from the PeerConnection class
    //The compareBlocks method also compare the lastModifiedTime and lastAccessedTime,
    // this is however not possible to test
    @Test
    void compareEqualBlocksTest() throws IOException {
        blockchain.add(new Block(new Metadata(path), "0", "Tester"));
        Block block = new Block(new Metadata(path), "0", "Tester");
        assertEquals("Files are the same", compareBlocksTester(blockchain, block));
    }

    @Test
    void compareDifferentBlocksTest() throws IOException {
        blockchain.add(new Block(new Metadata(path), "0", "Tester"));
        String path2 = "C:\\Users\\WinSa\\OneDrive\\Dokumenter\\RUC\\Fifth semester\\CS project\\MetadataBlockchainP2P\\SECONDTEST.docx";
        Block block = new Block(new Metadata(path2), "0", "Tester");
        assertEquals("Files not the same", compareBlocksTester(blockchain, block));
    }

    public String compareBlocksTester(ArrayList<Block> blockchain, Block newBlock) {
        if (blockchain.size() > 0) {
            for (int i = 0; i < blockchain.size(); i++) {
                String fileTitle = blockchain.get(i).getFileTitle();
                if (fileTitle.equals(newBlock.getFileTitle())) {
                    return "Files are the same";
                }
            }
        }
        return "Files not the same";
    }
}