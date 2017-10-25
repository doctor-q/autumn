package cc.doctor.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by doctor on 2017/3/9.
 */
public class FileUtils {
    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    public static boolean createFileRecursion(String absolutePath) {
        if (absolutePath == null) {
            return false;
        }
        createDirectoryRecursion(absolutePath.substring(0, absolutePath.lastIndexOf("/")));
        File file = new File(absolutePath);
        if (file.exists()) {
            log.warn("File exist:{}", absolutePath);
            return false;
        } else {
            try {
                return file.createNewFile();
            } catch (IOException e) {
                log.error("", e);
                return false;
            }
        }
    }

    public static boolean createDirectoryRecursion(String absolutePath) {
        if (absolutePath == null) {
            return false;
        }
        String[] dirs = absolutePath.split("/");
        StringBuilder currentDir = new StringBuilder("/");
        for (int i = 0; i < dirs.length; i++) {
            currentDir.append(dirs[i]);
            File file = new File(currentDir.toString());
            if (!file.exists() || !file.isDirectory()) {
                if (!file.mkdir()) {
                    log.warn("Make dir error:{}", currentDir);
                    return false;
                }
            }
            currentDir.append("/");
        }
        return true;
    }

    public static void main(String[] args) {
        FileUtils.move("/tmp/test/1", "/tmp/2");
    }

    public static boolean dropDirectory(String directory) {
        if (directory == null) {
            return false;
        }
        File file = new File(directory);
        if (!file.exists()) {
            return true;
        } else if (file.isFile()) {
            return file.delete();
        } else {
            String[] files = file.list();
            if (files != null) {
                for (String fileName : files) {
                    dropDirectory(directory + "/" + fileName);
                }
                return file.delete();
            }
            return false;
        }
    }

    public static boolean exists(String fileName) {
        if (fileName == null) {
            return false;
        }
        File file = new File(fileName);
        return file.exists() && !file.isDirectory();
    }

    public static List<String> list(String dir, List<String> excepts) {
        LinkedList<String> list = new LinkedList<>();
        if (dir == null) {
            return new LinkedList<>();
        }
        File file = new File(dir);
        if (file.exists() && file.isDirectory()) {
            String[] files = file.list();
            if (files != null) {
                for (String fileName : files) {
                    if (excepts == null || !excepts.contains(fileName)) {
                        list.add(fileName);
                    }
                }
                return list;
            }
        }
        return list;
    }

    public static boolean removeFile(String fileName) {
        if (fileName == null) {
            return true;
        }
        File file = new File(fileName);
        if (!file.exists()) {
            return true;
        }
        return file.delete();
    }

    public static boolean move(String from, String to) {
        if (from == null || to == null) {
            return false;
        }
        File fileFrom = new File(from);
        if (!fileFrom.exists()) {
            return false;
        }
        File fileTo = new File(to);
        return fileFrom.renameTo(fileTo);
    }

    public static String readFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            log.error("", e);
            return null;
        }
        return content.toString();
    }

    public static void writeFile(String content, String fileName) throws IOException {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
            bufferedWriter.write(content);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            log.error("", e);
            throw e;
        }
    }
}
