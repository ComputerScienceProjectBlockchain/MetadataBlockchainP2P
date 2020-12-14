package sample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

    //class to extract metadata for a file provide through a file path
public class Metadata {
    private final String title;
    private final String creationTime;
    private final String lastAccessTime;
    private final String lastModifiedTime;
    String size;

    public Metadata(String path) throws IOException {
            //In order to access the metadata of the file you have to use the classes
            // Path and BasicFileAttributes
            Path file = Paths.get(path);
            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
            //different attributes of a files meta data saved as a string
            this.creationTime = String.valueOf(attr.creationTime());
            this.lastAccessTime = String.valueOf(attr.lastAccessTime());
            this.lastModifiedTime = String.valueOf(attr.lastModifiedTime());
            this.size = String.valueOf(attr.size());
            this.title = String.valueOf(file.getFileName());
    }

        public String getTitle() {
            return title;
        }

        public String getCreationTime() {
            return creationTime;
        }

        public String getLastAccessTime() {
            return lastAccessTime;
        }

        public String getLastModifiedTime() {
            return lastModifiedTime;
        }
    }