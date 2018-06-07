package cc.doctor.framework.web.servlet;

import cc.doctor.framework.thrift.ResponseUtils;
import cc.doctor.framework.utils.Container;
import cc.doctor.framework.utils.SerializeUtils;
import cc.doctor.framework.web.exception.ExceptionHandler;
import cc.doctor.framework.web.route.RouteInvoke;
import cc.doctor.framework.web.route.RouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static cc.doctor.framework.utils.Container.container;
import static cc.doctor.framework.web.handler.RequestParser.requestParser;
import static cc.doctor.framework.web.handler.ResponseParser.responseParser;

/**
 * Created by doctor on 2017/3/18.
 */
public class DispatchServlet extends HttpServlet {
    private static final long serialVersionUID = -6726851046106695269L;
    private static final Logger log = LoggerFactory.getLogger(Container.class);
    private RouteService routeService = container.getOrCreateComponent(RouteService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        RouteInvoke routeInvoke = routeService.getInvoke(pathInfo);
        if (routeInvoke != null) {
            try {
                Object invoke = requestParser.invoke(this, req, resp, routeInvoke);
                if (invoke != null) {
                    responseParser.invoke(invoke, routeInvoke, this, req, resp);
                }
            } catch (Throwable e) {
                log.error("", e);
                Class exceptionHandlerClass = routeInvoke.getExceptionHandler();
                if (exceptionHandlerClass != null) {
                    try {
                        ExceptionHandler exceptionHandler = (ExceptionHandler) Container.container.getOrCreateComponent(exceptionHandlerClass);
                        PrintWriter writer = resp.getWriter();
                        writer.write(SerializeUtils.objectToJson(ResponseUtils.errorResponse(exceptionHandler.handleException(e))));
                    } catch (Exception e1) {
                        log.error("", e1);
                    }
                }
            }
        } else {
            resp.sendError(404);
        }
    }


}
