package cc.doctor.framework.log.appender;

import cc.doctor.framework.log.config.ConfigException;
import cc.doctor.framework.log.event.Event;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileAppender extends OutputStreamAppender {
    private String fileName;
    private FileOutputStream fileOutputStream;

    @Override
    public void append(Event event) {
        if (fileOutputStream == null) {
            try {
                fileOutputStream = new FileOutputStream(fileName, true);
            } catch (FileNotFoundException e) {
                throw new ConfigException();
            }
        }
        byte[] encode = encoder.encode(event);
        try {
            fileOutputStream.write(encode);
            fileOutputStream.write(System.lineSeparator().getBytes());
        } catch (IOException e) {

        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
