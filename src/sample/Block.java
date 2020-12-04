package sample;
import java.io.Serializable;
import java.util.Date;
import java.util.Random;

public class Block implements Serializable {

    //at a later point, we might need to add a merkle tree
    //tree building the hash of the block based on hashes of the transactions inside the block

    private String hash;
    private final String previousHash;

    private final String fileTitle;
    private final String fileCreatedTime;
    private final String fileAccessed;
    private final String userName;
    private final String fileModified;
    private final long timeStamp;           //as number of milliseconds since 1/1/1970.
    private static int height;              //counting how many blocks there are in the chain
    private int miningNonce;                //nonce used for the mining, starts at 0

    //Block Constructor.
    public Block(Metadata metadata,String previousHash, String userName) {
        this.fileTitle = metadata.title;
        this.fileCreatedTime = metadata.creationTime;
        this.fileAccessed = metadata.lastAccessTime;
        this.fileModified = metadata.lastModifiedTime;
        this.userName = userName;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        int nonce = new Random().nextInt(Integer.MAX_VALUE);    // random number used once; nonce = "number used once"
        this.hash = calculateHash(nonce);   //Making sure we do this after we set the other values.
    }

    //Calculate new hash based on blocks contents
    public String calculateHash(int nonce) {    //nonce as input so that we can use same method for miningNonce and nonce
        return StringUtil.applySha256(
                previousHash +
                        Long.toString(timeStamp) +
                        Integer.toString(nonce) +
                        fileTitle + fileCreatedTime + fileAccessed + userName
        );
    }

    //Increases nonce value until hash target is reached.
    public void mineBlock(int difficulty) {
        String target = StringUtil.getDifficultyString(difficulty); //Create a string with difficulty * "0"

        while(!this.getHash().substring( 0, difficulty).equals(target)) {   //while substring of hash not equals "0"*difficulty                                                                    //while the condition not fulfilled
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

    public String getFileAccessed() {
        return fileAccessed;
    }

    public String getFileModified() {
        return fileModified;
    }

    public String getUserName() {
        return userName;
    }
}