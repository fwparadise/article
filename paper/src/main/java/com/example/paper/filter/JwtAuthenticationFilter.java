package com.example.paper.filter;

import com.example.paper.utils.JwtUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtAuthenticationFilter implements Filter {
    private static final String[] pathChecked=new String[]{"websocket","sign","druid","favicon","swagger","api-doc"};
    private static final String[] pathExclude=new String[]{"/","/csrf"};
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        try {
            if (isProtectedUrl(request)) {
                String token = request.getHeader("Authorization");
                //检查jwt令牌, 如果令牌不合法或者过期, 里面会直接抛出异常, 下面的catch部分会直接返回
                JwtUtil.validateToken(token);
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }
        //如果jwt令牌通过了检测, 那么就把request传递给后面的RESTful api
        filterChain.doFilter(request, response);
    }


    //我们只对地址 /api 开头的api检查jwt. 不然的话登录/login也需要jwt
    private boolean isProtectedUrl(HttpServletRequest request) {
        for(String path:pathChecked){
            if (request.getServletPath().contains(path)){
                return false;
            }
        }
        for (String path:pathExclude){
            if (request.getServletPath().equals(path)){
                return false;
            }
        }
        return true;
    }

    public void init(FilterConfig config) throws ServletException {

    }
}