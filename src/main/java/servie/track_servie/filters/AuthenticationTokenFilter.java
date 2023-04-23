package servie.track_servie.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servie.track_servie.entities.AuthUser;
import servie.track_servie.service.JwtUserDetailsService;
import servie.track_servie.utils.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
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
            String authorizationHeader = request.getHeader("Authorization");
            if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer "))
            {
                String token = authorizationHeader.substring(7);
                String username = jwtUtils.getUserNameFromJwtToken(token);
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

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
    {
        // getting either endpoint url or css stylesheet folderPath, but no path containing "thymeleaf"
        String uri = request.getRequestURI();
        if(uri!=null && (uri.startsWith("/templates") || uri.endsWith(".html") || uri.contains("/css/mystyles.css")))
        {
            return true;
        }
        // DispatcherType dispatcherType = request.getDispatcherType();
        // if (dispatcherType != null && dispatcherType.equals(DispatcherType.FORWARD))
        // {
        // // String servletPath = request.getServletPath();
        // // if (servletPath != null && servletPath.startsWith("/thymeleaf/"))
        // // return true;
        // return true;
        // }
        // getting either endpoint url or css stylesheet folderPath, but no path containing "thymeleaf"
        // String servletPath = request.getServletPath();
        // if (servletPath != null && servletPath.startsWith("/thymeleaf/")) {
        //     return true;
        // }
        // String acceptHeader = request.getHeader("Accept");
        // if (acceptHeader != null && acceptHeader.startsWith("text/html")) {
        // return true;
        // }
        // if ((Boolean) request.getAttribute("isthymeleaf"))
        // return true;
        // getting null all the time
        // String headerValue = request.getHeader("X-Thymeleaf-Servlet");
        // if (headerValue != null && headerValue.equals("true"))
        //     return true;
        // getting null all the time
        // String paramValue = request.getParameter("thymeleafServlet");
        // if (paramValue != null && paramValue.equals("true"))
        //     return true;
        // getting null all the time
        // boolean isThymeleafRequest = false;
        // Object thymeleafServletRequestObj = request.getAttribute("thymeleafServletRequest");
        // if (thymeleafServletRequestObj instanceof Boolean) {
        //     isThymeleafRequest = (Boolean) thymeleafServletRequestObj;
        //     return isThymeleafRequest;
        // }
        // getting null all the time
        // boolean isThymeleaf = false;
        // Object thymeleafRequestObj = request.getAttribute("isthymeleaf");
        // if (thymeleafRequestObj instanceof Boolean) {
        //     isThymeleaf = (Boolean) thymeleafRequestObj;
        //     return isThymeleaf;
        // }
        // getting null all the time
        // String thymeleafServlet = (String) request.getAttribute("thymeleafServlet");
        // if (thymeleafServlet != null && thymeleafServlet.equals("thymeleaf"))
        //     return true;
        // Boolean isThymeleafEnabled = env.getProperty("spring.thymeleaf.servlet.enabled", Boolean.class);
        // if(isThymeleafEnabled!=null && isThymeleafEnabled)
        //     return true;
        return Boolean.TRUE.equals(request.getAttribute("SHOULD_NOT_FILTER"));
        // return false;
    }
}