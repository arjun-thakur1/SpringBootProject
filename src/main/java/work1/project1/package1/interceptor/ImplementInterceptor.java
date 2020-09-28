package work1.project1.package1.interceptor;

import org.springframework.boot.web.servlet.DispatcherType;
import work1.project1.package1.exception.CustomException;
import work1.project1.package1.interfaces.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class ImplementInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    LoggingService loggingService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws CustomException {

        long startTime=System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        if (DispatcherType.REQUEST.name().equals(request.getDispatcherType().name())
                && (request.getMethod().equals(HttpMethod.GET.name())||request.getMethod().equals(HttpMethod.DELETE.name()))) {
            loggingService.logRequest(request, null);
        }
        return true;
    }
}
