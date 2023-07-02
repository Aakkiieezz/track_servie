package servie.track_servie.utils.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import servie.track_servie.entity.AuthUser;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils
{
    // private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    // @Value("${app.jwt-secret}")
    // private String jwtSecret;
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    // public String generateJwtToken(Authentication authentication)
    // {
    //     AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
    //     return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    // }
    // public String getUserNameFromJwtToken(String token)
    // {
    //     return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    // }
    //     public String getUserNameFromJwtToken(String token)
    // {
    //     JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(jwtSecret).build();
    //     return jwtParser.parseClaimsJws(token).getBody().getSubject();
    // }
    // public boolean validateJwtToken(String authToken)
    // {
    //     try
    //     {
    //         Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
    //         return true;
    //     }
    //     catch(SignatureException e)
    //     {
    //         logger.error("Invalid JWT signature: {}", e.getMessage());
    //     }
    //     catch(MalformedJwtException e)
    //     {
    //         logger.error("Invalid JWT token: {}", e.getMessage());
    //     }
    //     catch(ExpiredJwtException e)
    //     {
    //         logger.error("JWT token is expired: {}", e.getMessage());
    //     }
    //     catch(UnsupportedJwtException e)
    //     {
    //         logger.error("JWT token is unsupported: {}", e.getMessage());
    //     }
    //     catch(IllegalArgumentException e)
    //     {
    //         logger.error("JWT claims string is empty: {}", e.getMessage());
    //     }
    //     return false;
    // }

    public String getUserNameFromJwtToken(String token)
    {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token)
    {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSignInKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwtToken(Authentication authentication)
    {
        AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
        // return Jwts.builder()
        //         .setSubject((userPrincipal.getUsername()))
        //         .setIssuedAt(new Date())
        //         .signWith(SignatureAlgorithm.HS512, jwtSecret)
        //         .compact();
        return Jwts.builder().setSubject(userPrincipal.getUsername()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis()+1000*60*24)).signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }
}