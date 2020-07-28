package Work1.Project1.Package.interceptor;

import Work1.Project1.Package.exception.NotFoundException;
import Work1.Project1.Package.interfaces.LoggingService;
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


@Component
public class ImplementInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    LoggingService loggingService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws NotFoundException{


        Enumeration header= request.getHeaderNames();
       while (header.hasMoreElements()) {
           String key = (String) header.nextElement();
            String value = request.getHeader(key);

            if (key.equals("token"))
            {      if(value.equals("1"))
                    {
                       break;
                    }
                  else
                  {
                     //hit new api that say authorization failed
                      String uri="https://e6707348-a2a6-4a80-9d7a-c89850abe893.mock.pstmn.io";
                      RestTemplate restTemplate = new RestTemplate();
                      String result = restTemplate.getForObject(uri, String.class);
                      throw   new NotFoundException(result);
                  }
            }
        }
      if (DispatcherType.REQUEST.name().equals(request.getDispatcherType().name()) && request.getMethod().equals(HttpMethod.GET.name())) {
                loggingService.logRequest(request, null);
            }

            return true;

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }


}
