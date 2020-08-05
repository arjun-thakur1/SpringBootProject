package work1.project1.package1.interceptor;

import work1.project1.package1.exception.CustomException;
import work1.project1.package1.interfaces.LoggingService;
import work1.project1.package1.services.UsersService;
import work1.project1.package1.util.RestApiManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class ImplementInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    LoggingService loggingService;

    @Autowired
    RestApiManager restApiManager;

    @Autowired
    UsersService usersService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws CustomException {

        long startTime=System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        if(!request.getMethod().equals(HttpMethod.POST.name()))
         loggingService.logRequest(request, handler);


        /*
        String header_token= request.getHeader("token");   //for authorization , hit another api
        if( header_token==null || ! restApiManager.authorization(header_token))
            throw new CustomException("Unauthorized Request!!!");


      String id = request.getHeader("id");
        String password = request.getHeader("password");
        if(id==null || password==null || id.isEmpty() || password.isEmpty()){
            throw new CustomException("Invalid Credential");
        }

        long user_id = Long.parseLong(id);
        Optional<Users> users=   usersService.get(user_id);
        if(users.isPresent()) {
            Users savedUser = users.get();
            if (!savedUser.get_password().equalsIgnoreCase(password)) {
                throw new CustomException("Invalid Credential");
            }
        }
        else{
            throw new CustomException("Invalid Credential");
        }
*/

        return true;

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }


}
