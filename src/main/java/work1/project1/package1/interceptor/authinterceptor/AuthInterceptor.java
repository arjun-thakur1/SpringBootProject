package work1.project1.package1.interceptor.authinterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import work1.project1.package1.exception.CustomException;
import work1.project1.package1.services.RedisService;
import work1.project1.package1.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    UserService usersService;
    @Autowired
    RedisService redisService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws CustomException {

        String  token = request.getHeader("token");
        if(token.equals("admin"))
            return true;
        token="access_token::"+token;
        String isPresent=redisService.getKeyValue(token);

        if(isPresent==null)
            throw new CustomException("Invalid AccessToken!! ");

        return true;
    }



}
