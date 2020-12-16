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
        this.creationTime = changeTimeFormat(String.valueOf(attr.creationTime()));
        this.lastAccessTime = changeTimeFormat(String.valueOf(attr.lastAccessTime()));
        this.lastModifiedTime = changeTimeFormat(String.valueOf(attr.lastModifiedTime()));
        this.size = String.valueOf(attr.size());
        this.title = String.valueOf(file.getFileName());
    }

    //When BasicFileAttributes attains the timestamp for creationTime,
    // lastAccessTime and lastModifiedTime it is written like;
    // 2020-12-16T20:46:05.734107Z
    // - To avoid this, the method changeTimeFormat is made
    private String changeTimeFormat(String date){
        String year = date.substring(0,4);
        String month = date.substring(5,7);
        String day = date.substring(8,10);
        String time = date.substring(11,19);
        String newTimeFormat = day + "-" + month + "-" + year +
                " " + time;
        return newTimeFormat;
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