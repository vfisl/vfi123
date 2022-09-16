package com.example.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.model.service.JWTUserServiceImpl;
import com.example.util.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;

public class JwtRequestFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired 
	private JWTUserServiceImpl jwtUserServiceImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String requestHeader = request.getHeader("Authorization");
		String username = null;
		String jwtToken = null;

		if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
			jwtToken = requestHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("unable to get username");
			} catch (ExpiredJwtException e) {
				System.out.println("token expired");
			}

		}else {
			logger.warn("Invalid header");
		}
		// if token is present then validate name
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails details = jwtUserServiceImpl.loadUserByUsername(username);
		// if token is valid, configure spring security manually
			if(jwtTokenUtil.validateToken(jwtToken, details)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						                details,null,details.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// now set the authentication in security context
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		System.out.println("helloooo");
		filterChain.doFilter(request, response);

	}

}
