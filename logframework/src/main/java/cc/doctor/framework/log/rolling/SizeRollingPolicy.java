package cc.doctor.framework.log.rolling;

import cc.doctor.framework.log.event.DefaultEvent;
import cc.doctor.framework.log.event.Event;
import cc.doctor.framework.log.pattern.PatternParser;
import cc.doctor.framework.log.pattern.converter.Converter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class SizeRollingPolicy extends RollingPolicy {
    // 一个文件的大小
    private Long size;

    @Override
    public void rollover() {
        if (currentFile.length() >= size) {
            List<Converter> converters = PatternParser.getSingleton().parse(filePattern);
            Event event = new DefaultEvent().thread(Thread.currentThread().getName()).logTime(new Date());
            StringBuilder stringBuilder = new StringBuilder();
            for (Converter converter : converters) {
                stringBuilder.append(converter.convert(event));
            }
            currentFileName = stringBuilder.toString();
            currentFile = new File(currentFileName);
            try {
                current.flush();
                current.close();
                current = new FileOutputStream(currentFile);
            } catch (IOException e) {

            }
        }
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
