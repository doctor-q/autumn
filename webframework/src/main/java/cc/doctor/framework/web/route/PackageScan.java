package cc.doctor.framework.web.route;

import cc.doctor.framework.entity.Function;
import cc.doctor.framework.utils.CollectionUtils;
import cc.doctor.framework.utils.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * 包扫描，只能扫描非静态类
 */
public class PackageScan {
    private static final Logger logger = LoggerFactory.getLogger(PackageScan.class);

    public List<Class> scanPackage(String basePackage) {
        List<String> classNames = new LinkedList<>();
        try {
            doScan(basePackage, classNames);
        } catch (IOException e) {
            logger.error("", e);
        }
        return CollectionUtils.transform(classNames, new Function<String, Class>() {
            @Override
            public Class transform(String from) {
                return ReflectUtils.getClassByName(from);
            }
        });
    }

    private String dotToSplash(String dotString) {
        return dotString.replaceAll("[.]", "/");
    }

    private List<String> doScan(String basePackage, List<String> nameList) throws IOException {
        // replace dots with splashes
        String splashPath = dotToSplash(basePackage);

        // get file path
        URL url = Thread.currentThread().getContextClassLoader().getResource(splashPath);
        String filePath = url.getPath();

        // Get classes in that package.
        // If the web server unzips the jar file, then the classes will exist in the form of
        // normal file in the directory.
        // If the web server does not unzip the jar file, then classes will exist in jar file.
        List<String> names = null; // contains the name of the class file. e.g., Apple.class will be stored as "Apple"
        if (isJarFile(filePath)) {
            // jar file
            if (logger.isDebugEnabled()) {
                logger.debug("{} 是一个JAR包", filePath);
            }
            names = readFromJarFile(filePath, splashPath);
        } else {
            // directory
            if (logger.isDebugEnabled()) {
                logger.debug("{} 是一个目录", filePath);
            }
            names = readFromDirectory(filePath);
        }

        for (String name : names) {
            if (isClassFile(name)) {
                nameList.add(toFullyQualifiedName(name, basePackage));
            } else {
                // this is a directory
                // check this directory for more classes
                // do recursive invocation
                doScan(basePackage + "." + name, nameList);
            }
        }

        if (logger.isDebugEnabled()) {
            for (String n : nameList) {
                logger.debug("找到{}", n);
            }
        }

        return nameList;
    }

    private String trimExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        return fileName.substring(0, index);
    }

    /**
     * Convert short class name to fully qualified name.
     * e.g., String -> java.lang.String
     */
    private String toFullyQualifiedName(String shortName, String basePackage) {
        return basePackage + '.' + trimExtension(shortName);
    }

    private List<String> readFromJarFile(String jarPath, String splashedPackageName) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("从JAR包中读取类: {}", jarPath);
        }

        JarInputStream jarIn = new JarInputStream(new FileInputStream(jarPath));
        JarEntry entry = jarIn.getNextJarEntry();

        List<String> nameList = new ArrayList<>();
        while (null != entry) {
            String name = entry.getName();
            if (name.startsWith(splashedPackageName) && isClassFile(name)) {
                nameList.add(name);
            }

            entry = jarIn.getNextJarEntry();
        }

        jarIn.close();

        return nameList;
    }

    private List<String> readFromDirectory(String path) {
        File file = new File(path);
        String[] names = file.list();

        if (null == names) {
            return new LinkedList<>();
        }

        return Arrays.asList(names);
    }

    private boolean isClassFile(String name) {
        return name.endsWith(".class");
    }

    private boolean isJarFile(String name) {
        return name.endsWith(".jar");
    }
}
