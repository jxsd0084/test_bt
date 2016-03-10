package com.github.trace.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.function.Supplier;

/**
 * 统一截获聚合的interval设置。
 * <ul>
 * <li>默认按照1m一个点聚合</li>
 * <li>可以通过interval={1m|5m|10m|1h}等进行设置</li>
 * <li>url参数的设置会被转成cookie设置，便于多个请求之间进行透传</li>
 * </ul>
 * <p>
 * Created by lir on 2016/1/23.
 */
public class IntervalInterceptor implements HandlerInterceptor {
    @SuppressWarnings("unchecked")
    private static final ThreadLocal<String> INTERVAL = ThreadLocal.withInitial((Supplier) () -> "1m");
    private static final String INTERVAL_OLD_VALUE = "_OLD_INTERVAL_";
    private static final String INTERVAL_COOKIE_NAME = "interval";

    public static String getTimeInterval() {
        return "group by time(" + INTERVAL.get() + ")";
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String param = request.getParameter(INTERVAL_COOKIE_NAME);
        if (param != null) {
            //同步设置到cookie中
            Cookie c = new Cookie(INTERVAL_COOKIE_NAME, param);
            c.setMaxAge(3600);
            response.addCookie(c);
        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie i : cookies) {
                    if (i.getName().equals(INTERVAL_COOKIE_NAME)) {
                        param = i.getValue();
                        break;
                    }
                }
            }
        }

        if (param != null) {
            request.setAttribute(INTERVAL_OLD_VALUE, INTERVAL.get());
            INTERVAL.set(param);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Object old = request.getAttribute(INTERVAL_OLD_VALUE);
        if (old != null) {
            INTERVAL.set((String) old);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
