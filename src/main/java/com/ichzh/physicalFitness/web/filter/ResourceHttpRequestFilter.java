package com.ichzh.physicalFitness.web.filter;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Configuration 
public class ResourceHttpRequestFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		String contextPath = httpRequest.getContextPath();
		StringBuffer requestURL = httpRequest.getRequestURL();
		String path = requestURL.substring(requestURL.indexOf(contextPath)+1);
		if (!StringUtils.hasText(path) || isInvalidPath(path)) {
			if (log.isTraceEnabled()) {
				log.trace("Ignoring invalid resource path [" + path + "]");
			}
			return;
		}
		if (path.contains("%")) {
			try {
				// Use URLDecoder (vs UriUtils) to preserve potentially decoded UTF-8 chars
				if (isInvalidPath(URLDecoder.decode(path, "UTF-8"))) {
					if (log.isTraceEnabled()) {
						log.trace("Ignoring invalid resource path with escape sequences [" + path + "].");
					}
					return;
				}
				String decodedPath = processPath(path);
				if (isInvalidPath(decodedPath)) {
					if (log.isTraceEnabled()) {
						log.trace("Ignoring invalid resource path with escape sequences [" + decodedPath + "].");
					}
					return;
				}
			}
			catch (IllegalArgumentException ex) {
				// ignore
			}
		}
		//httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);	
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	protected String processPath(String path) {
		path = StringUtils.replace(path, "\\", "/");
		path = cleanDuplicateSlashes(path);
		return cleanLeadingSlash(path);
	}
	
	private String cleanDuplicateSlashes(String path) {
		StringBuilder sb = null;
		char prev = 0;
		for (int i = 0; i < path.length(); i++) {
			char curr = path.charAt(i);
			try {
				if ((curr == '/') && (prev == '/')) {
					if (sb == null) {
						sb = new StringBuilder(path.substring(0, i));
					}
					continue;
				}
				if (sb != null) {
					sb.append(path.charAt(i));
				}
			}
			finally {
				prev = curr;
			}
		}
		return sb != null ? sb.toString() : path;
	}

	private String cleanLeadingSlash(String path) {
		boolean slash = false;
		for (int i = 0; i < path.length(); i++) {
			if (path.charAt(i) == '/') {
				slash = true;
			}
			else if (path.charAt(i) > ' ' && path.charAt(i) != 127) {
				if (i == 0 || (i == 1 && slash)) {
					return path;
				}
				path = slash ? "/" + path.substring(i) : path.substring(i);
				if (log.isTraceEnabled()) {
					log.trace("Path after trimming leading '/' and control characters: " + path);
				}
				return path;
			}
		}
		return (slash ? "/" : "");
	}

	
	protected boolean isInvalidPath(String path) {
		if (log.isTraceEnabled()) {
			log.trace("Applying \"invalid path\" checks to path: " + path);
		}
		if (path.contains("WEB-INF") || path.contains("META-INF")) {
			if (log.isTraceEnabled()) {
				log.trace("Path contains \"WEB-INF\" or \"META-INF\".");
			}
			return true;
		}
		if (path.contains(":/")) {
			String relativePath = (path.charAt(0) == '/' ? path.substring(1) : path);
			if (ResourceUtils.isUrl(relativePath) || relativePath.startsWith("url:")) {
				if (log.isTraceEnabled()) {
					log.trace("Path represents URL or has \"url:\" prefix.");
				}
				return true;
			}
		}
		if (path.contains("../")) {
			path = StringUtils.cleanPath(path);
			if (path.contains("../")) {
				if (log.isTraceEnabled()) {
					log.trace("Path contains \"../\" after call to StringUtils#cleanPath.");
				}
				return true;
			}
		}
		return false;
	}

	
}
