package binar.box.security;

import binar.box.domain.Token;
import binar.box.service.TokenService;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Timis Nicu Alexandru on 23-Mar-18.
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private TokenService tokenService;

    public TokenAuthenticationFilter(TokenService tokenService) {
        this.tokenService = tokenService;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(Constants.TOKEN_HEADER_REQUEST);
        if (token == null) {
            throw new LockBridgesException(Constants.AUTHENTICATION_TOKEN_NOT_FOUND);
        }
        Token userToken = tokenService.findOneByToken(token);
        DecodedJWT decodedJWT = tokenService.decodeJwtToken(userToken);
        if (decodedJWT.getClaim(Constants.JWT_PAYLOAD_CLAIM_USER).asLong().equals(userToken.getUser().getId())) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(userToken.getUser().getEmail(), userToken.getUser().getPassword());
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } else {
            SecurityContextHolder.clearContext();
            throw new LockBridgesException(Constants.INVALID_TOKEN);
        }
    }
}