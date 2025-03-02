package com.example.atmdemo.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import com.example.atmdemo.security.JWT.JwtAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

public class SessionFilter extends GenericFilterBean {
	private static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String  setCookieHeaderName = "Set-Cookie";
	       String setCookieHeader = ((HttpServletResponse)response).getHeader(setCookieHeaderName);
		  String noHttp =   removeHttpOnlyFlag( setCookieHeader);
		  if( setCookieHeader != null) {
		      logger.info("removed HttpOnly "+noHttp);
		      logger.info("removed HttpOnly "+setCookieHeader.replace("HttpOnly", ""));
		      ((HttpServletResponse)response).addHeader("Set-Cookie",noHttp);
		  }
		 chain.doFilter(request, response);
		
	      //  removeHttpOnlyFlag(response);
	}
	private String removeHttpOnlyFlag(String setCookieHeader) {
		 
   	 if (setCookieHeader != null) {
            setCookieHeader = setCookieHeader.replace("HttpOnly", "");
           
            logger.info("removed HttpOnly "+setCookieHeader);
        }
   	 return setCookieHeader;
	}
}
