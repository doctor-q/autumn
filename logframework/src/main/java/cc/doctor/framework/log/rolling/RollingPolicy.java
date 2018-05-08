package cc.doctor.framework.log.rolling;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public abstract class RollingPolicy {
    protected List<String> files;
    protected String currentFileName;
    protected File currentFile;
    protected FileOutputStream current;
    protected String filePattern;

    public void loadFiles() {
        int i = filePattern.lastIndexOf('/');
        String directory = filePattern.substring(0, i);
        File file = new File(directory);
        if (!file.exists()) {
            try {
                Files.createDirectory(Paths.get(file.toURI()));
            } catch (IOException e) {

            }
        }
        files = Arrays.asList(file.list());
    }

    public String getFilePattern() {
        return filePattern;
    }

    public void setFilePattern(String filePattern) {
        this.filePattern = filePattern;
    }

    public List<String> getFiles() {
        return files;
    }

    public String getCurrentFileName() {
        return currentFileName;
    }

    abstract public void rollover();
}
