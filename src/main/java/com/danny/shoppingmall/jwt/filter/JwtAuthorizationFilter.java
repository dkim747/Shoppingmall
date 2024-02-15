package com.danny.shoppingmall.jwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.*;
import com.danny.shoppingmall.jwt.dto.JwtErrors;
import com.danny.shoppingmall.jwt.dto.JwtProperties;
import com.danny.shoppingmall.jwt.service.JwtService;
import com.danny.shoppingmall.user.dto.UserDTO;
import com.danny.shoppingmall.user.repository.UserRepository;
import com.danny.shoppingmall.security.PrincipalDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository,JwtService jwtService) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        try{
            String accessHeader = jwtService.getAccessHeader(request);
            if (accessHeader == null || !accessHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }

            String accessToken = jwtService.getAccessJwtWithoutPrefix(request);
            String userEmail = jwtService.getClaim(accessToken);

            if (userEmail != null) {
                UserDTO userDTO = userRepository.findByUserEmail(userEmail).get().EntityToDTO();
                PrincipalDetails principalDetails = new PrincipalDetails(userDTO);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        principalDetails, null, principalDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (TokenExpiredException e) {
            request.setAttribute(JwtProperties.JWT_EXCEPTION, JwtErrors.EXPIRED_TOKEN);
        } catch (MissingClaimException e) {
            request.setAttribute(JwtProperties.JWT_EXCEPTION, JwtErrors.WRONG_TOKEN);
        } catch (InvalidClaimException e) {
            request.setAttribute(JwtProperties.JWT_EXCEPTION, JwtErrors.WRONG_TOKEN);
        } catch (JWTDecodeException e) {
            request.setAttribute(JwtProperties.JWT_EXCEPTION, JwtErrors.WRONG_TOKEN);
        } catch (JWTVerificationException e) {
            request.setAttribute(JwtProperties.JWT_EXCEPTION, JwtErrors.WRONG_TOKEN);
        }
        filterChain.doFilter(request, response);
    }
}
