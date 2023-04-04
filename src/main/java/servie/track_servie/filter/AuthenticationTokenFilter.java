package servie.track_servie.filter;

// import app.CivicDutyWellness.model.CivicUser;
// import app.CivicDutyWellness.service.JwtUserDetailsService;
// import app.CivicDutyWellness.utils.jwt.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servie.track_servie.entity.AuthUser;
import servie.track_servie.service.JwtUserDetailsService;
import servie.track_servie.utils.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter
{
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private JwtUserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        try
        {
            String jwt = parseJwt(request);
            if(jwt!=null /* && jwtUtils.validateJwtToken(jwt) */)
            {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                AuthUser userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        catch(Exception e)
        {
            logger.error("Cannot authenticate user: {0}", e);
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request)
    {
        String headerAuth = request.getHeader("Authorization");
        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer "))
        {
            return headerAuth.substring(7);
        }
        return null;
    }
}