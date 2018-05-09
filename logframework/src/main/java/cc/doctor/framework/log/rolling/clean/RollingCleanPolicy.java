package cc.doctor.framework.log.rolling.clean;

import java.io.File;
import java.util.List;

public interface RollingCleanPolicy {
    void clean(List<File> files);
}
