package com.FCfactory.filter;

import com.FCfactory.common.BaseContext;
import com.FCfactory.common.R;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        //定义要放行的静态资源

        String[] urls = new String[]{
                "/employee/login",
                "/employ/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI();


        // 如果该拦截匹配，直接放行

        if (check(urls, uri)) {
            log.info("拦截到{},已放行", uri);
            filterChain.doFilter(request, response);
            return;
        }

        //检查登陆情况，若已登录，直接放行
        Object employee = request.getSession().getAttribute("employee");
        if (employee != null) {
            log.info("已登录，id为：{}", request.getSession().getAttribute("employee"));

            Long empid=(Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empid);
            filterChain.doFilter(request, response);
            return;
        }

        Object user = request.getSession().getAttribute("user");
        if (user != null) {
            log.info("已登录，id为：{}", request.getSession().getAttribute("user"));

            Long userid=(Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userid);
            filterChain.doFilter(request, response);
            return;
        }

        //未登录，返回错误信息
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));


    }

    /**
     * 检查请求是否与要放行的匹配
     *
     * @param urls
     * @param uri
     * @return
     */
    private boolean check(String[] urls, String uri) {
        for (String url : urls) {
            if (PATH_MATCHER.match(url, uri)) {
                return true;
            }
        }
        return false;
    }
}
