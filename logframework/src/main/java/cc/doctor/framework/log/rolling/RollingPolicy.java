package cc.doctor.framework.log.rolling;

import cc.doctor.framework.log.event.DefaultEvent;
import cc.doctor.framework.log.event.Event;
import cc.doctor.framework.log.pattern.PatternParser;
import cc.doctor.framework.log.pattern.converter.Converter;
import cc.doctor.framework.log.rolling.clean.RollingCleanPolicy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

public abstract class RollingPolicy {
    protected List<File> files;
    // 绝对路径
    protected String currentFileName;
    protected File currentFile;
    protected FileOutputStream current;
    protected String filePattern;
    // 日志清除策略，可以同时采用多种清除策略
    protected List<RollingCleanPolicy> rollingCleanPolicies;

    public void load() {
        int i = filePattern.lastIndexOf('/');
        String directory = filePattern.substring(0, i);
        final File dir = new File(directory);
        if (!dir.exists()) {
            try {
                Files.createDirectory(Paths.get(dir.toURI()));
            } catch (IOException e) {

            }
        }
        String[] list = dir.list();
        if (list != null && list.length > 0) {
            // 获取最新的文件
            File lastModifyFile = null;
            for (String fileName : list) {
                File file = new File(dir, fileName);
                if (lastModifyFile == null) {
                    lastModifyFile = file;
                } else {
                    if (file.lastModified() > lastModifyFile.lastModified()) {
                        lastModifyFile = file;
                    }
                }
                files.add(file);
            }
            currentFile = lastModifyFile;
            currentFileName = currentFile.getAbsolutePath();
            try {
                current = new FileOutputStream(currentFile);
            } catch (FileNotFoundException e) {

            }
        }
    }

    public String getFilePattern() {
        return filePattern;
    }

    public void setFilePattern(String filePattern) {
        this.filePattern = filePattern;
    }

    public List<File> getFiles() {
        return files;
    }

    public String getCurrentFileName() {
        return currentFileName;
    }

    public List<RollingCleanPolicy> getRollingCleanPolicies() {
        return rollingCleanPolicies;
    }

    public void setRollingCleanPolicies(List<RollingCleanPolicy> rollingCleanPolicies) {
        this.rollingCleanPolicies = rollingCleanPolicies;
    }

    abstract public void rollover();

    protected void newFile() {
        List<Converter> converters = PatternParser.getSingleton().parse(filePattern);
        Event event = new DefaultEvent().thread(Thread.currentThread().getName()).logTime(new Date());
        StringBuilder stringBuilder = new StringBuilder();
        for (Converter converter : converters) {
            stringBuilder.append(converter.convert(event));
        }
        currentFileName = stringBuilder.toString();
        currentFile = new File(currentFileName);
        files.add(currentFile);
        try {
            current.flush();
            current.close();
            current = new FileOutputStream(currentFile);
        } catch (IOException e) {

        }
    }

    class RollingCleanThread implements Runnable {

        @Override
        public void run() {
            if (rollingCleanPolicies != null && rollingCleanPolicies.size() > 0) {
                for (RollingCleanPolicy rollingCleanPolicy : rollingCleanPolicies) {
                    rollingCleanPolicy.clean(files);
                }
            }
        }
    }
}
