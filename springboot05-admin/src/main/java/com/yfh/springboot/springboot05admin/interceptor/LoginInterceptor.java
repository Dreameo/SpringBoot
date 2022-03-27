package com.yfh.springboot.springboot05admin.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 检查登录
 */

@Slf4j
public class LoginInterceptor implements HandlerInterceptor{
    /**
     * 登录方法执行之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截的请求：" + request.getRequestURI());
        /**
         * 登录逻辑检查
         * 返回值：其boolean类型的返回值表示是否拦截或放行，返回true为放行，即调用控制器方法；返回false表示拦截，即不调用控制器方法
         */
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        if (user != null) {
            // 放行
            return true;
        }
        // 拦截住，返回登录页面,重定向，要像session存错误消息
        request.setAttribute("msg", "请先登录!");
        request.getRequestDispatcher("/").forward(request, response);

//        response.sendRedirect("/");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
