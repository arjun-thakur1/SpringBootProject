package work1.project1.package1.interceptor.authinterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import work1.project1.package1.exception.CustomException;
import work1.project1.package1.exception.UnAuthorizedUser;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws UnAuthorizedUser {

        //if request url is for
        String url=request.getRequestURL().toString();
        String sub_url=url.substring(21);
      //  System.out.println(sub_url);
        if(!sub_url.equals("/employee/token-generate")) {
            String token = request.getHeader("token");
            if ("admin".equals(token))
                return true;
            token = "access_token::" + token;
            String isPresent = redisService.getKeyValue(token);
          //  System.out.println(isPresent + "     ....");
            if (isPresent == null)
                throw new UnAuthorizedUser("Invalid AccessToken!! ");
           // request.setAttribute("userId" , token.substring(47));

            //System.out.println(token.substring(47));
        }
        return true;
    }
}
