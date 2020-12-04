package sample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class Metadata {
    String title;
    String path;
    String creationTime;
    String lastAccessTime;
    String lastModifiedTime;
    String size;

    public Metadata(String path) throws IOException {
        this.path = path;
        //In order to access the metadata of the file you have to use the classes
        // Path and BasicFileAttributes
        Path file = Paths.get(path);
        BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
        this.creationTime = String.valueOf(attr.creationTime());
        this.lastAccessTime = String.valueOf(attr.lastAccessTime());
        this.lastModifiedTime = String.valueOf(attr.lastModifiedTime());
        this.size = String.valueOf(attr.size());
        this.title=String.valueOf(file.getFileName());
    }

    //Encrypts the metadata using the applySha256 hash method
    /*public String encryptMetadata() {
        return StringUtil.applySha256(creationTime+lastAccessTime+lastModifiedTime+size+title);
    }*/

    @Override
    public String toString() {
        return "Metadata{" +
                "title='" + title + '\'' +
                ", path='" + path + '\'' +
                ", creationTime='" + creationTime + '\'' +
                ", lastAccessTime='" + lastAccessTime + '\'' +
                ", lastModifiedTime='" + lastModifiedTime + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}