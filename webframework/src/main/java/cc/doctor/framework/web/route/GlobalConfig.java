package cc.doctor.framework.web.route;

import cc.doctor.framework.utils.PropertyUtils;
import cc.doctor.framework.web.exception.SimpleResultExceptionHandler;

public class GlobalConfig {
    private String exceptionHandler;
    private String modelViewResolver;
    private String modelViewSuffix;
    private String controllerScanPackage;

    public GlobalConfig() {
        exceptionHandler = PropertyUtils.getString("mvc.global.exceptionhandler", SimpleResultExceptionHandler.class.getName());
        modelViewResolver = PropertyUtils.getString("mvc.global.modelview.resolver", null);
        modelViewSuffix = PropertyUtils.getString("mvc.global.modelview.suffix", null);
        controllerScanPackage = PropertyUtils.getString("mvc.global.controller.scan", null);
    }

    public String getExceptionHandler() {
        return exceptionHandler;
    }

    public void setExceptionHandler(String exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public String getModelViewResolver() {
        return modelViewResolver;
    }

    public void setModelViewResolver(String modelViewResolver) {
        this.modelViewResolver = modelViewResolver;
    }

    public String getModelViewSuffix() {
        return modelViewSuffix;
    }

    public void setModelViewSuffix(String modelViewSuffix) {
        this.modelViewSuffix = modelViewSuffix;
    }

    public String getControllerScanPackage() {
        return controllerScanPackage;
    }

    public void setControllerScanPackage(String controllerScanPackage) {
        this.controllerScanPackage = controllerScanPackage;
    }
}
