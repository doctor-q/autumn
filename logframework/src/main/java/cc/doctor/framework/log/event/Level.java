package cc.doctor.framework.log.event;

public enum Level {
    TRACE(0), ERROR(1), WARN(2), INFO(3), DEBUG(4);
    private int code;

    Level(int code) {
        this.code = code;
    }

    public boolean above(Level level) {
        return code <= level.code;
    }
}
