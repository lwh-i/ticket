package com.lwh.seckill.iterceptor;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author SixBarrel
 * @date 2021/12/13
 */
@Component
public class CorsInterceptor implements Filter{

	/**
	 * options methods
	 */
	private static final String OPTIONS_METHOD = "OPTIONS";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletResponse res = (HttpServletResponse) response;
		res.addHeader("Access-Control-Allow-Credentials", "true");
		res.addHeader("Access-Control-Allow-Origin", "*");
		res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
		res.addHeader("Access-Control-Allow-Headers", "*");
		if (OPTIONS_METHOD.equals(((HttpServletRequest) request).getMethod())) {
			response.getWriter().println("ok");
			return;
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
//@Override
//public void addCorsMappings(CorsRegistry registry) {
//	registry.addMapping("/**")
//			.allowedOriginPatterns("*")
//			.allowedMethods("POST","GET")
//			.maxAge(3600)
//			.allowCredentials(true);
////	super.addCorsMappings(registry);
//}
}
