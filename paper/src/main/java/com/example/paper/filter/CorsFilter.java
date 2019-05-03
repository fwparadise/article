package com.example.paper.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CorsFilter implements Filter {
    private static final String[] pathChecked=new String[]{"websocket","swagger","druid","favicon","api-doc"};
    private static final String[] pathExclude=new String[]{"/","/csrf"};
    public void destroy() {
    }
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        if (needFilter(request)) {
            response.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest) req).getHeader("Origin"));
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE");
            response.setHeader("Access-Control-Allow-Headers", "Authorization");
        }
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        filterChain.doFilter(request, response);
    }
    public void init(FilterConfig config) throws ServletException {

    }
    private boolean needFilter(HttpServletRequest request){
        for (String path :pathChecked){
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
}