package cc.doctor.framework.log.appender.encode;

import cc.doctor.framework.log.event.Event;

import java.io.UnsupportedEncodingException;

public class EchoEncoder implements Encoder {
    private String charset;

    @Override
    public byte[] encode(Event event) {
        String message = event.getMessage();
        String characterSet = charset;
        if (characterSet == null) {
            characterSet = "UTF-8";

        }
        try {
            return message.getBytes(characterSet);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
