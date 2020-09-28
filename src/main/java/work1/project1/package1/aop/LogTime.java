package work1.project1.package1.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogTime {

    Logger logger= LoggerFactory.getLogger(this.getClass());

    @Around("@annotation(MethodExecutionTime)")
    public void aspectExecTimeLogging(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        pjp.proceed();
        Long timeTaken= (System.currentTimeMillis()) - start;
        logger.info("time taken by method : " + timeTaken);
    }
}

