package example.aop;

import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Aspect
@Component
public class LogAop {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("execution(* example.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();

        String requestId = UUID.randomUUID().toString();
        String url = httpServletRequest.getRequestURI(); // 获取请求的URI
        Object[] args = point.getArgs();
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        if (MapUtil.isNotEmpty(parameterMap)){
            log.info("[PLUS]开始请求 => URL[{}],参数:[{}]", url, JsonUtils.toJsonString(parameterMap));
        } else {
            log.info("[PLUS]开始请求 => URL[{}],无参数", url);
        }
        Object result;
        try {
            result = point.proceed();
        } catch (Throwable throwable) {
            log.error("请求错误, id: {}, 错误原因: {}", requestId, throwable.getMessage(), throwable);
            throw throwable;
        }
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        String resultStr = objectMapper.writeValueAsString(result);
        if (resultStr.length() > 1000) {
            resultStr = resultStr.substring(0, 1000) + "..."; // 限制响应结果的输出长度
        }
        log.info("[Join]结束请求 => URL[{}],耗时:[{}]毫秒,返回结果:[{}]" ,httpServletRequest.getMethod() + " " + url, totalTimeMillis,resultStr);
        return result;
    }

}