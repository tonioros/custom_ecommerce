package org.antonioxocoy.cecommerce.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.antonioxocoy.cecommerce.models.dto.AuthCredentialsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JWTTokenUtil tokenUtil;

    public JWTAuthenticationFilter(JWTTokenUtil tokenUtil) {
        super();
        this.tokenUtil = tokenUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        AuthCredentialsDTO authCredentialsDTO = new AuthCredentialsDTO();
        try {
            authCredentialsDTO = new ObjectMapper().readValue(request.getReader(), AuthCredentialsDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken userPAT = new UsernamePasswordAuthenticationToken(
                authCredentialsDTO.getEmail(),
                authCredentialsDTO.getPassword(),
                Collections.emptyList()
        );
        return getAuthenticationManager().authenticate(userPAT);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {


        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();

        String token = this.tokenUtil.generateAccessToken(userDetails.getUser());
        response.addHeader("Authorization", "Bearer " + token);
        response.getWriter().flush();
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
