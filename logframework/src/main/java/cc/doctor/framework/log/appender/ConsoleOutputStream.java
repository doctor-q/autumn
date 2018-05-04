package cc.doctor.framework.log.appender;

import java.io.IOException;
import java.io.OutputStream;

public abstract class ConsoleOutputStream extends OutputStream {

    @Override
    public void write(int b) throws IOException {
        System.out.write(b);
    }
}
