package Work1.Project1.Package.interceptor;

import Work1.Project1.Package.exception.CustomException;
import Work1.Project1.Package.interfaces.LoggingService;
import Work1.Project1.Package.util.RestApiManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.DispatcherType;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

import static Work1.Project1.Package.constants.ApplicationConstants.StartTime;


@Component
public class ImplementInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    LoggingService loggingService;

    @Autowired
    RestApiManager restApiManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws CustomException {

        if(!request.getMethod().equals(HttpMethod.POST.name()))
              loggingService.logRequest(request, handler);

        String header_token= request.getHeader("token");   //for authorization , hit another api

        if( header_token==null || ! restApiManager.authorization(header_token))
            throw new CustomException("Unauthoreized Request!!!");


        return true;

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }


}
