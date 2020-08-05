package work1.project1.package1.services;



import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import work1.project1.package1.interfaces.LoggingService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import static work1.project1.package1.constants.ApplicationConstants.*;

@Component
@Log
public class LogServiceImpl implements LoggingService {

        @Override
        public void logRequest(HttpServletRequest httpServletRequest, Object body) {
           StringBuilder stringBuilder = new StringBuilder();
            Map<String, String> parameters = buildParametersMap(httpServletRequest);

         //   long startTime=System.currentTimeMillis();
          //  httpServletRequest.setAttribute("startTime", startTime);

            stringBuilder.append(REQUEST);
               stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
            stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
            stringBuilder.append("headers=[").append(buildHeadersMap(httpServletRequest)).append("] ");
         stringBuilder.append("startTime=[").append(httpServletRequest.getAttribute("startTime")).append("] ");

            if (!parameters.isEmpty()) {
                stringBuilder.append("parameters=[").append(parameters).append("] ");
            }

         if (body != null) {
                stringBuilder.append("body=[" + body + "]");
         }
        log.info(stringBuilder.toString());
 }

        @Override
        public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(RESPONSE_TIME);
            stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
            stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
            stringBuilder.append("responseHeaders=[").append(buildHeadersMap(httpServletResponse)).append("] ");
            stringBuilder.append("responseBody=[").append(body).append("] ");

            long endTime=System.currentTimeMillis();
            stringBuilder.append("EndTime=[").append(endTime).append("] ");

      long startTime = (Long) httpServletRequest.getAttribute("startTime");
      stringBuilder.append("Timetaken=[").append(endTime-startTime).append("] ");
           log.info(stringBuilder.toString());
        }

        private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
            Map<String, String> resultMap = new HashMap<>();
            Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String key = parameterNames.nextElement();
                String value = httpServletRequest.getParameter(key);
                resultMap.put(key, value);
            }

            return resultMap;
        }

        private Map<String, String> buildHeadersMap(HttpServletRequest request) {
            Map<String, String> map = new HashMap<>();

            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = request.getHeader(key);
                  map.put(key, value);
            }
            return map;
        }

        private Map<String, String> buildHeadersMap(HttpServletResponse response) {
            Map<String, String> map = new HashMap<>();

            Collection<String> headerNames = response.getHeaderNames();
            for (String header : headerNames) {
                map.put(header, response.getHeader(header));
            }

            return map;
        }

    }

 /* */