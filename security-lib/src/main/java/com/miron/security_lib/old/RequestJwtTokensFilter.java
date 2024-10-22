package com.miron.security_lib.old;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miron.security_lib.models.Token;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Objects;
import java.util.function.Function;

public class RequestJwtTokensFilter extends OncePerRequestFilter {
    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/jwt/tokens", HttpMethod.POST.name());
    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();
    private Function<Authentication, Token> refreshTokenFactory = new DefaultRefreshTokenFactory();
    private Function<Token, Token> accessTokenFactory = new DefaultAccessTokenFactory();
    private Function<Token, String> refreshTokenStringSerializer = Objects::toString;
    private Function<Token, String> accessTokenStringSerializer = Objects::toString;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(this.requestMatcher.matches(request)){
            if(this.securityContextRepository.containsContext(request)){
                var context = securityContextRepository.loadDeferredContext(request).get();
                if(context != null && !(context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken)){
                    var refreshToken = refreshTokenFactory.apply(context.getAuthentication());
                    var accessToken = accessTokenFactory.apply(refreshToken);

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    objectMapper.writeValue(response.getWriter(),
                            new Tokens(accessTokenStringSerializer.apply(accessToken),
                                    accessToken.expiresAt().toString(),
                                    refreshTokenStringSerializer.apply(refreshToken),
                                    refreshToken.expiresAt().toString()));
                    return;
                }
            }
            throw new AccessDeniedException("User must be authenticated");
        }
        filterChain.doFilter(request, response);
    }

    public void setRequestMatcher(RequestMatcher requestMatcher) {
        this.requestMatcher = requestMatcher;
    }

    public void setSecurityContextRepository(SecurityContextRepository securityContextRepository) {
        this.securityContextRepository = securityContextRepository;
    }

    public void setRefreshTokenFactory(Function<Authentication, Token> refreshTokenFactory) {
        this.refreshTokenFactory = refreshTokenFactory;
    }

    public void setAccessTokenFactory(Function<Token, Token> accessTokenFactory) {
        this.accessTokenFactory = accessTokenFactory;
    }

    public void setRefreshTokenStringSerializer(Function<Token, String> refreshTokenStringSerializer) {
        this.refreshTokenStringSerializer = refreshTokenStringSerializer;
    }

    public void setAccessTokenStringSerializer(Function<Token, String> accessTokenStringSerializer) {
        this.accessTokenStringSerializer = accessTokenStringSerializer;
    }
}
