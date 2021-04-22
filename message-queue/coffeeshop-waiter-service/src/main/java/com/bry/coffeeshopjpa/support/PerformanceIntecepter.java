package com.bry.coffeeshopjpa.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StopWatch;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PerformanceIntecepter implements HandlerInterceptor {
    private ThreadLocal<StopWatch> tl = new ThreadLocal<>();

    @Override public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String method = handler.getClass().getSimpleName();
        if (handler instanceof HandlerMethod) {
            String beanType = ((HandlerMethod) handler).getBeanType().getName();
            String methodname = ((HandlerMethod) handler).getMethod().getName();
            method = beanType + "." + methodname;
        }
        log.info("pre handler {}", method);
        StopWatch sw = new StopWatch();
        tl.set(sw);
        sw.start();
        return true;
    }

    @Override public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        tl.get().stop();
        tl.get().start();
        log.info("post handler");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

        StopWatch sw = tl.get();
        sw.stop();
        log.info("after completion, execute total {} ms, handle {} ms, after completeion {} ms",
                sw.getTotalTimeMillis(),
                sw.getTotalTimeMillis() - sw.getLastTaskTimeMillis(),
                sw.getLastTaskTimeMillis());
    }
}
