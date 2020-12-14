package sample;
import java.io.Serializable;
import java.util.Date;
import java.util.Random;
//Based on tutorial found at
//https://medium.com/programmers-blockchain/create-simple-blockchain-java-tutorial-from-scratch-6eeed3cb03fa
public class Block implements Serializable {
        //class needs to implement the serializable interface
        //so that we are able to use input and output streams

    private String hash;
    private final String previousHash;
    private final String fileTitle;
    private final String fileCreatedTime;
    private final String fileAccessedTime;
    private final String fileModifiedTime;
    private final String userName;
    private final long timeStamp;           //as number of milliseconds since 1/1/1970
    private static int height;              //counting how many blocks there are in the chain
    private int miningNonce;                //nonce used for the mining, starts at 0

        //Block Constructor.
        //instead of the string data, we should use an arrayList containing
        //the data the should be included in the block
    public Block(Metadata metadata,String previousHash, String userName) {
            //extract meta data information from the file, which is inserted in the block
        this.fileTitle = metadata.getTitle();
        this.fileCreatedTime = metadata.getCreationTime();
        this.fileAccessedTime = metadata.getLastAccessTime();
        this.fileModifiedTime = metadata.getLastModifiedTime();
        this.userName = userName;
            //get the hash of the previous block
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
            // random number used once; nonce = "number used once"
        int nonce = new Random().nextInt(Integer.MAX_VALUE);
            //Making sure we do this after we set the other values
        this.hash = calculateHash(nonce);
    }

        //Calculate new hash based on blocks contents
        //nonce as input so that we can use same method for miningNonce and nonce
    public String calculateHash(int nonce) {
        return StringUtil.applySha256(
                previousHash +
                        Long.toString(timeStamp) +
                        Integer.toString(nonce) +
                        fileTitle + fileCreatedTime + fileAccessedTime + fileModifiedTime + userName
        );
    }

        //Increases nonce value until hash target is reached.
    public void mineBlock(int difficulty) {
            //Create a string with difficulty * "0"
        String target = StringUtil.getDifficultyString(difficulty);
            //while substring of hash not equals "0"*difficulty
        while(!this.getHash().substring( 0, difficulty).equals(target)) {                                                                       //while the condition not fulfilled
                //add one to nonce and calculate new hash with nonce
            miningNonce ++;
            hash = calculateHash(miningNonce); //
        }
        System.out.println("Block Mined!!! : " + this.getHash());
    }

    public String getHash() {
        return this.hash;
    }
    public String getPreviousHash() {
        return this.previousHash;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        Block.height = height;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public String getFileCreatedTime() {
        return fileCreatedTime;
    }

    public String getFileAccessedTime() {
        return fileAccessedTime;
    }

    public String getFileModifiedTime() {
        return fileModifiedTime;
    }

    public String getUserName() {
        return userName;
    }
}