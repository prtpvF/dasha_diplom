package diplom.by.robot.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import diplom.by.robot.jwt.JwtUtil;
import diplom.by.robot.service.PersonDetailsService;
import diplom.by.robot.util.PersonDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;
    private final PersonDetailsService personDetailsService;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = jwtUtil.extractTokenFromHeader(request);
        try {
            if (token != null) {
                String username = jwtUtil.validateTokenAndRetrieveClaim(token);
                log.info("Token is valid for user: {}", username);
                PersonDetails personDetails = (PersonDetails) personDetailsService.loadUserByUsername(username);
                log.info("User authorities: {}", personDetails.getAuthorities());
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                        (personDetails,null, personDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (JWTVerificationException e) {
            log.error("JWT verification failed: {}", e.getMessage());
            sendErrorResponse(response);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format("авторизуйтесь еще раз"));
    }
}