package com.musdon.bank.config;

import java.io.IOException;

import org.hibernate.annotations.Comment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;



import ch.qos.logback.core.util.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@Component

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private JwtTokenProvider jwtTokenProvider;
	
	private UserDetailsService userDetailsService;
	
	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
		super();
		this.jwtTokenProvider = jwtTokenProvider;
		this.userDetailsService = userDetailsService;
	}


	
	@Override
	protected void doFilterInternal(@NotNull HttpServletRequest request,@NotNull HttpServletResponse response,@NotNull FilterChain filterChain)
			throws ServletException, IOException {
		String token =getTokenFromRequest(request);
		if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token))
		{
		  String username=jwtTokenProvider.getUsername(token);
		  UserDetails userDetails=userDetailsService.loadUserByUsername(username);
		  UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
				  userDetails,null,userDetails.getAuthorities()
				  );
		  
		  authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		  SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				  
		}
		filterChain.doFilter(request, response);
		
	}

	private String getTokenFromRequest(HttpServletRequest request) {		
		String bearerToken=request.getHeader("Authorization");
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer"))
		{
			return bearerToken.substring(7);
		}
		
	return null;
	}
}

