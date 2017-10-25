package cc.doctor.framework.web.filter;

import cc.doctor.framework.utils.DateUtils;
import cc.doctor.framework.utils.FileUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by doctor on 17-6-15.
 */
public class MultipartFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);
    private ServletFileUpload servletFileUpload;
    private String baseFilePath;

    public String getBaseFilePath() {
        return baseFilePath;
    }

    public void setBaseFilePath(String baseFilePath) {
        this.baseFilePath = baseFilePath;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            if (ServletFileUpload.isMultipartContent((HttpServletRequest) servletRequest)) {
                List<FileItem> fileItems = servletFileUpload.parseRequest((HttpServletRequest) servletRequest);
                for (FileItem fileItem : fileItems) {
                    if (fileItem.isFormField()) {
                        String fileItemString = fileItem.getString();
                        if (fileItemString != null) {
                            servletRequest.setAttribute(fileItem.getFieldName(), new String(fileItemString.getBytes("iso-8859-1"), "utf-8"));
                        }
                    } else {
                        String fileName = fileItem.getName();
                        InputStream inputStream = fileItem.getInputStream();
                        String fieldName = fileItem.getFieldName();
                        String content = Streams.asString(inputStream);
                        String filePath = baseFilePath + "/" + fileName + "." + DateUtils.toYMDHMS(new Date());
                        FileUtils.writeFile(content, filePath);
                        servletRequest.setAttribute(fieldName, filePath);
                    }
                }
            }
        } catch (FileUploadException e) {
            log.error("", e);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
