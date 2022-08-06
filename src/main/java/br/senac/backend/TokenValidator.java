package br.senac.backend;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import br.senac.backend.service.TokenService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TokenValidator implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(TokenValidator.class);

	@Autowired
	private TokenService tokenService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String token = null;
		if (request instanceof HttpServletRequest) {
			String url = ((HttpServletRequest) request).getRequestURL().toString();

			HttpServletRequest httpRequest = (HttpServletRequest) request;
			
			if (httpRequest.getMethod().equals("OPTIONS")) {
				chain.doFilter(request, response);
				return;
			}

			if (!(url != null && url.contains("/api/"))) {
				chain.doFilter(request, response);
				return;
			}

			token = ((HttpServletRequest) request).getHeader("token");

			if (token != null) {
				if (tokenService.validToken(token)) {
					chain.doFilter(request, response);
					return;
				}
			}
		}

		LOGGER.error(":: Token(" + token + "), invalid token, proceed with another login.");
		((HttpServletResponse) response).getWriter().append(
				"{\n" + "    \"code\": 401,\n" + "    \"msg\": \"UNAUTHORIZED\",\n" + "    \"data\": null\n" + "}");
		((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		((HttpServletResponse) response).setContentType("application/json");
		((HttpServletResponse) response).addHeader("Access-Control-Allow-Origin", "*");
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}