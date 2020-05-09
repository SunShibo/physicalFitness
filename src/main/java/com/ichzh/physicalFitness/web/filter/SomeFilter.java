package com.ichzh.physicalFitness.web.filter;

import org.springframework.stereotype.Component;

import com.ichzh.physicalFitness.conf.LoginConfig;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author audin
 *
 */
@Component
public class SomeFilter implements Filter {
	
	@Resource
	private LoginConfig loginConfig;

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
//		response.getWriter().write("SomeFilter out");
//		response.setContentType("text/plain");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse)response;
		
		// 指定跨域域名设置
		//"*"存在风险，建议指定可信任的域名来接收响应信息，如"http://www.sosoapi.com"
		// resp.addHeader("Access-Control-Allow-Origin", "*");
		// resp.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		Set<String> set = loginConfig.getAllowDomains();
        String originHeads = req.getHeader("Origin");
        if (set.contains(originHeads)) {
        	resp.addHeader("Access-Control-Allow-Origin", originHeads);
        	resp.addHeader("Access-Control-Allow-Methods", loginConfig.getAllowMethods());
        }
		//如果存在自定义的header参数，需要在此处添加，逗号分隔
		resp.addHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, "
				+ "If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, "
				+ "Content-Type, X-E4M-With, token");
		
		// 防止跨站点请求伪造，对Referer验证
		String referer = req.getHeader("Referer");
		StringBuilder sb = new StringBuilder();
		sb.append(req.getScheme()).append("://").append(req.getServerName());
		if(referer != null && !referer.equals("") ){
            if(referer.lastIndexOf(sb.toString()) != 0){
            	//resp.sendRedirect(req.getContextPath()+"/login.jsp");
               // return;
            }
        }
		chain.doFilter(request, response);

	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

}
