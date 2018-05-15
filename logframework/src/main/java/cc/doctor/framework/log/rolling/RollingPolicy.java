package cc.doctor.framework.log.rolling;

import cc.doctor.framework.log.event.DefaultEvent;
import cc.doctor.framework.log.event.Event;
import cc.doctor.framework.log.pattern.PatternParser;
import cc.doctor.framework.log.pattern.converter.Converter;
import cc.doctor.framework.log.rolling.clean.RollingCleanPolicy;
import cc.doctor.framework.log.rolling.clean.SizeRollingCleanPolicy;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public abstract class RollingPolicy {
    protected List<File> files;
    // 绝对路径
    protected String currentFileName;
    protected File currentFile;
    protected FileOutputStream current;
    private static final String DEFAULT_FILE_PATTERN = "/tmp/log/%d{yyyyMMddHHmmssSSS}-rolling.log";
    protected String filePattern = DEFAULT_FILE_PATTERN;
    // 日志清除策略，可以同时采用多种清除策略
    @JSONField(deserialize = false)
    protected List<? extends RollingCleanPolicy> rollingCleanPolicies;

    public void load() {
        files = new LinkedList<>();
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
                current = new FileOutputStream(currentFile, true);
            } catch (FileNotFoundException e) {

            }
        } else {
            newFile();
        }
        startLogClean();
        addShutdownHook();
    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (current != null) {
                        current.flush();
                        current.close();
                    }
                } catch (IOException e) {

                }
            }
        }));
    }

    private void startLogClean() {
        Thread thread = new Thread(new RollingCleanThread());
        thread.setDaemon(true);
        thread.start();
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

    public List<? extends RollingCleanPolicy> getRollingCleanPolicies() {
        return rollingCleanPolicies;
    }

    public void setRollingCleanPolicies(List<? extends RollingCleanPolicy> rollingCleanPolicies) {
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
            if (current != null) {
                current.flush();
                current.close();
            }
            current = new FileOutputStream(currentFile, true);
        } catch (IOException e) {

        }
    }

    // 默认按大小轮转日志，默认按大小清除策略
    public static RollingPolicy defaultRollingPolicy() {
        SizeRollingPolicy sizeRollingPolicy = new SizeRollingPolicy();
        List<SizeRollingCleanPolicy> rollingCleanPolicies = Collections.singletonList(new SizeRollingCleanPolicy());
        sizeRollingPolicy.setRollingCleanPolicies(rollingCleanPolicies);
        return sizeRollingPolicy;
    }

    public FileOutputStream getCurrent() {
        return current;
    }

    class RollingCleanThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                if (rollingCleanPolicies != null && !rollingCleanPolicies.isEmpty()) {
                    for (RollingCleanPolicy rollingCleanPolicy : rollingCleanPolicies) {
                        rollingCleanPolicy.clean(files);
                    }
                }
            }
        }
    }
}
